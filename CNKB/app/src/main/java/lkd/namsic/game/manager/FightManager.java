package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class FightManager {

    private static final FightManager instance = new FightManager();

    //TODO
    private final Map<Long, Long> preventMap = new ConcurrentHashMap<>();

    private final Map<Long, Long> fightId = new ConcurrentHashMap<>();
    private final Map<Long, Set<Entity>> entitySet = new ConcurrentHashMap<>();
    private final Map<Long, Set<Player>> playerSet = new ConcurrentHashMap<>();

    public static FightManager getInstance() {
        return instance;
    }

    public boolean tryFight(@NonNull Player self, @NonNull String targetStr, boolean isFightOne) {
        GameMap map = Config.getMapData(self.getLocation());

        if(MapType.cityList().contains(map.getMapType())) {
            throw new WeirdCommandException("마을에서는 전투를 할 수 없습니다");
        }

        Long playerId = Config.PLAYER_ID.get(targetStr);
        if(playerId != null) {
            if(self.getId().getObjectId() == playerId) {
                throw new WeirdCommandException("자기 자신과는 전투할 수 없습니다");
            }

            Player player = Config.getData(Id.PLAYER, playerId);

            if(!self.getLocation().equalsMap(player.getLocation())) {
                throw new WeirdCommandException("해당 플레이어와 같은 맵에 있지 않습니다");
            }

            if(self.canFight(player)) {
                long currentTime = System.currentTimeMillis();

                long id = self.getId().getObjectId();
                long preventTime = preventMap.getOrDefault(id, 0L);
                if(preventTime > currentTime) {
                    throw new WeirdCommandException("전투에서 도주한 후 30초 동안은 전투할 수 없습니다");
                } else if(preventTime != 0) {
                    preventMap.remove(id);
                }

                preventTime = preventMap.getOrDefault(playerId, 0L);
                if(preventTime > currentTime) {
                    throw new WeirdCommandException("해당 대상은 현재 전투가 불가능합니다\n(도주 성공 후 30초간 PvP 불가)");
                } else if(preventTime != 0) {
                    preventMap.remove(playerId);
                }
                
                if(self.getLv().get() / 2 > player.getLv().get()) {
                    throw new WeirdCommandException("본인 레벨의 절반에 미치지 못하는 레벨의 플레이어는 공격할 수 없습니다\n\n" +
                            "뉴비 학살을 멈춰주세요\n-공익 봇 협의회-");
                }

                this.startFight(self, player, isFightOne);
            } else {
                return false;
            }
        } else {
            Id id = Id.MONSTER;
            int index = Integer.parseInt(targetStr);

            Set<Long> set = map.getEntity(Id.MONSTER);
            int size = set.size();
            if(size < index || index < 0) {
                index -= size;
                set = map.getEntity(Id.BOSS);

                if(set.size() < index) {
                    throw new WeirdCommandException("알 수 없는 대상입니다");
                } else {
                    id = Id.BOSS;
                }
            }

            List<Long> list = new ArrayList<>(set);
            Collections.sort(list);

            Entity target = Config.getData(id, list.get(index - 1));
            if(!self.canFight(target)) {
                throw new WeirdCommandException("전투를 할 수 없는 적입니다");
            }

            this.startFight(self, target, isFightOne);
        }

        return true;
    }

    public void startFight(@NonNull Player self, @NonNull Entity enemy, boolean isFightOne) {
        self.addLog(LogData.FIGHT, 1);

        if (this.checkInject(self, enemy)) {
            return;
        }

        this.setPrevDoing(self);
        this.setDoing(self, isFightOne);

        if(enemy.getId().getId().equals(Id.PLAYER)) {
            this.setPrevDoing((Player) enemy);
        }

        this.setDoing(enemy, isFightOne);

        long fightId = self.getId().getObjectId();
        String is = " (이/가) ";

        Random random = new Random();

        Set<Entity> entitySet = this.getEntitySet(fightId);
        Set<Player> playerSet = this.getPlayerSet(fightId);
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.e("FightManager", e);
                throw new RuntimeException(e.getMessage());
            }

            Entity attacker = getAttacker(entitySet, random);

            Player player = null;
            FightWaitType response;
            Entity target = null;

            if(attacker.getId().getId().equals(Id.PLAYER)) {
                player = (Player) attacker;

                player.setVariable(Variable.IS_TURN, true);

                Map<Integer, Entity> targets = new HashMap<>();
                this.printFightMsg(player, entitySet, targets);

                Player.replyPlayersExcept(playerSet, attacker.getName() + "의 턴입니다" +
                        "\n유저의 턴을 기다려주세요(최대 30초)", player);

                player.setVariable(Variable.FIGHT_WAIT_TYPE, FightWaitType.NONE);
                player.setVariable(Variable.FIGHT_TARGET_INDEX, 0);
                player.setVariable(Variable.FIGHT_TARGET_MAX_INDEX, entitySet.size() - 1);

                synchronized (player) {
                    while (true) {
                        try {
                            player.wait(Config.FIGHT_WAIT_TIME);
                        } catch (InterruptedException e) {
                            Logger.e("FightManager", e);
                            throw new RuntimeException(e.getMessage());
                        }

                        player.notifyAll();

                        Boolean isFightResponse = player.getObjectVariable(Variable.IS_FIGHT_RESPONSE, true);
                        if (isFightResponse) {
                            response = player.getObjectVariable(Variable.FIGHT_WAIT_TYPE, FightWaitType.NONE);

                            if (response.equals(FightWaitType.NONE)) {
                                player.replyPlayer("시간이 초과되어 랜덤한 적을 공격합니다");

                                response = FightWaitType.ATTACK;
                                player.setVariable(Variable.FIGHT_TARGET_INDEX, random.nextInt(entitySet.size() - 1) + 1);
                            }

                            boolean usedItem = player.getObjectVariable(Variable.FIGHT_USED_ITEM, false);
                            if (response.equals(FightWaitType.ITEM)) {
                                if (usedItem) {
                                    player.replyPlayer("아이템은 연속 사용이 불가능합니다");
                                    continue;
                                }

                                player.setVariable(Variable.FIGHT_USED_ITEM, true);
                            } else {
                                if (usedItem) {
                                    player.removeVariable(Variable.FIGHT_USED_ITEM);
                                }
                            }

                            self.removeVariable(Variable.IS_FIGHT_RESPONSE);
                            self.removeVariable(Variable.IS_TURN);

                            if (response.equals(FightWaitType.ATTACK)) {
                                target = Objects.requireNonNull(targets.get(player.getVariable(Variable.FIGHT_TARGET_INDEX)));
                                player.removeVariable(Variable.FIGHT_TARGET_INDEX);
                            }

                            break;
                        }
                    }
                }
            } else {
                Object[] patternData = ((Monster) attacker).onTurn(playerSet);
                response = (FightWaitType) patternData[0];
                target = (Entity) patternData[1];
            }


            String baseMsg, msg;
            Set<Player> exceptSet = new HashSet<>();

            boolean success = random.nextBoolean();
                switch (response) {
                case WAIT:
                    baseMsg = "턴을 종료했습니다";
                    msg = attacker.getFightName() + " (은/는) 아무 행동도 하지 않고 " + baseMsg;

                    if(player != null) {
                        player.replyPlayer(baseMsg);
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                case ATTACK:
                    if(target == null) {
                        throw new NullPointerException("Target is null");
                    }

                    boolean isDeath = attacker.attack(target, false);

                    String hpBar;
                    if(isDeath) {
                        hpBar = target.getDisplayHp(0);
                    } else {
                        hpBar = target.getDisplayHp();
                    }

                    if(target.getId().getId().equals(Id.PLAYER)) {
                        exceptSet.add((Player) target);
                    }


                    if(player != null) {
                        exceptSet.add(player);
                    }

                    String targetName = target.getFightName();
                    Player.replyPlayersExcepts(playerSet, attacker.getFightName() + is + targetName +
                            " (을/를) 공격했습니다\n\n" + targetName + "의 남은 체력\n" + hpBar, exceptSet);

                    break;

                case DEFENCE:
                    if (success) {
                        attacker.setVariable(Variable.IS_DEFENCING, true);
                        baseMsg = "방어 태세로 전환헀습니다";
                    } else {
                        baseMsg = "방어 태세로의 전환에 실패했습니다";
                    }

                    msg = attacker.getFightName() + is + baseMsg;
                    if(player != null) {
                        player.replyPlayer(baseMsg);
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                case RUN:
                    if (success) {
                        baseMsg = "도주에 성공했습니다";
                    } else {
                        baseMsg = "도주에 실패했습니다";
                    }

                    msg = attacker.getFightName() + is + baseMsg;
                    if(player != null) {
                        player.replyPlayer(baseMsg);
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    if (attacker.equals(self)) {
                        for (Entity entity : new HashSet<>(entitySet)) {
                            this.endFight(entity);
                        }

                        Player.replyPlayersExcept(playerSet, "전투를 시작한 유저가 도주하여 전투가 종료되었습니다", self);
                        return;
                    } else {
                        this.endFight(attacker);
                    }

                    break;

                case ITEM:
                    long itemId = attacker.getObjectVariable(Variable.FIGHT_ITEM_ID, ItemList.NONE.getId());

                    ItemManager.getInstance().use(attacker, itemId, 1);

                    baseMsg = ItemList.findById(itemId) + " (을/를) 사용했습니다";
                    msg = attacker.getFightName() + is + baseMsg;
                    if(player != null) {
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                case EQUIP:
                    long equipId = attacker.getObjectVariable(Variable.FIGHT_ITEM_ID, EquipList.NONE.getId());

                    EquipManager.getInstance().use(attacker, equipId);

                    baseMsg = EquipList.findById(equipId) + " (을/를) 사용했습니다";
                    msg = attacker.getFightName() + is + baseMsg;
                    if(player != null) {
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                default:
                    throw new RuntimeException();
            }

            IdClass killerId;
            Entity killer;
            for(Entity entity : new HashSet<>(entitySet)) {
                if(entity.getKiller() != null) {
                    killerId = entity.getKiller();
                    killer = Config.getData(killerId.getId(), killerId.getObjectId());

                    Player.replyPlayersExcepts(playerSet, entity.getFightName() + is +
                            killer.getFightName() + " 에 의해 사망했습니다", exceptSet);

                    entity.setKiller(null);

                    if(entity.equals(self)) {
                        for(Entity entity_ : entitySet) {
                            this.endFight(entity_);
                        }

                        Player.replyPlayersExcept(playerSet, "전투를 시작한 유저가 사망하여 전투가 종료되었습니다", self);
                        return;
                    } else {
                        this.endFight(entity);
                    }
                }
            }

            if(entitySet.size() == 1) {
                if(playerSet.size() == 1) {
                    self.replyPlayer("전투에서 승리하였습니다");
                }

                this.endFight(self);
                return;
            }
        }
    }

    private void printFightMsg(@NonNull Player self, @NonNull Set<Entity> entitySet, @NonNull Map<Integer, Entity> targets) {
        int index = 1;

        StringBuilder innerBuilder = new StringBuilder("---행동 목록---\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (공격/attack/a) [{대상}] - 대상을 공격합니다(대상 미입력 시 1번을 공격합니다)\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (방어/defence/d) - 50% 확률로 다음 피해의 물리 데미지를 막고 일정량 회복합니다\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (대기/wait/w) - 아무 행동도 하지 않습니다\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (아이템/item/i) {아이템 이름} - 아이템을 사용합니다(연속 사용 불가)\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (장비/equip/e) {장비 부위} - 장비를 사용합니다\n")
                .append(Emoji.LIST)
                .append(" (전투/fight/f) (도망/도주/run/r) - 50% 확률로 도주합니다\n\n예시: ")
                .append(Emoji.focus("n 전투 공격 1"))
                .append("\n\n---대상 목록---");

        List<Entity> list = new ArrayList<>(entitySet);
        list.sort((o1, o2) -> {
            boolean isPlayer1 = o1.getId().getId().equals(Id.PLAYER);
            boolean isPlayer2 = o2.getId().getId().equals(Id.PLAYER);

            if ((isPlayer1 && isPlayer2) || !(isPlayer1 || isPlayer2)) {
                return o1.getRealName().compareTo(o2.getRealName());
            }

            return isPlayer1 ? -1 : 1;
        });

        for (Entity entity : list) {
            if (entity.equals(self)) {
                continue;
            }

            targets.put(index, entity);
            innerBuilder.append("\n")
                    .append(index++)
                    .append(". ")
                    .append(entity.getName())
                    .append(" - 남은 체력 : [ ")
                    .append(entity.getDisplayHp())
                    .append(" ]");
        }

        self.replyPlayer("당신의 턴입니다\n대상과 행동을 30초 이내에 선택해주세요", innerBuilder.toString());
    }

    private void setPrevDoing(@NonNull Player self) {
        Doing doing = self.getDoing();
        if (doing.equals(Doing.ADVENTURE)) {
            self.setPrevDoing(Doing.NONE);
        } else {
            self.setPrevDoing(doing);
        }
    }

    private void setDoing(@NonNull Entity self, boolean isFightOne) {
        if(isFightOne) {
            self.setDoing(Doing.FIGHT_ONE);
        } else {
            self.setDoing(Doing.FIGHT);
        }
    }

    private boolean checkInject(@NonNull Player self, @NonNull Entity enemy) {
        Id id = self.getId().getId();
        long objectId = self.getId().getObjectId();

        Id enemyId = enemy.getId().getId();
        long enemyObjectId = enemy.getId().getObjectId();

        self = Config.loadObject(id, objectId);
        enemy = Config.loadObject(enemyId, enemyObjectId);

        if (Doing.fightList().contains(enemy.getDoing())) {
            StringBuilder innerBuilder = new StringBuilder("---적 목록---");

            long fightId = this.getFightId(enemyObjectId);
            Set<Entity> entitySet = this.getEntitySet(fightId);
            Set<Player> playerSet = this.getPlayerSet(fightId);

            for(Entity entity : entitySet) {
                innerBuilder.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(entity.getFightName());
            }

            entitySet.add(self);
            playerSet.add(self);

            self.replyPlayer("전투에 난입했습니다\n적 목록은 전체보기로 확인해주세요", innerBuilder.toString());
            Player.replyPlayers(playerSet, self.getName() + " (이/가) 전투에 난입했습니다!");

            Config.unloadObject(enemy);

            return true;
        } else {
            fightId.put(objectId, objectId);
            fightId.put(enemyObjectId, objectId);

            Set<Entity> entitySet = new HashSet<>();
            entitySet.add(self);
            entitySet.add(enemy);
            this.entitySet.put(objectId, entitySet);

            Set<Player> playerSet = new HashSet<>();
            playerSet.add(self);

            if(enemy.getId().getId().equals(Id.PLAYER)) {
                Player player = (Player) enemy;

                playerSet.add(player);

                player.addLog(LogData.FIGHT, 1);
                player.replyPlayer(self.getName() + " (와/과) 의 전투가 시작되었습니다");
            }

            this.playerSet.put(objectId, playerSet);

            self.replyPlayer(enemy.getName() + " (와/과) 의 전투가 시작되었습니다");

            return false;
        }
    }

    @NonNull
    private Entity getAttacker(@NonNull Set<Entity> entitySet, @NonNull Random random) {
        Map<Entity, Integer> atsMap = new LinkedHashMap<>();
        int ats;
        int sum = 0;

        for (Entity entity : entitySet) {
            ats = entity.getStat(StatType.ATS);
            atsMap.put(entity, ats);

            sum += ats;
        }

        int randomValue = random.nextInt(sum);
        int currentWeight = 0;
        for (Map.Entry<Entity, Integer> entry : atsMap.entrySet()) {
            ats = entry.getValue();

            if (randomValue < currentWeight + ats) {
                return entry.getKey();
            } else {
                currentWeight += ats;
            }
        }

        throw new NullPointerException("Attacker is null - " + atsMap.toString() + ", " + sum);
    }

    public void fightCommand(@NonNull Player self, @NonNull String command, @NonNull String subCommand) {
        boolean isTurn = self.getObjectVariable(Variable.IS_TURN, false);
        if(!isTurn) {
            self.replyPlayer("현재 당신의 턴이 아닙니다.\n턴이 종료될 때까지 기다려주세요");
            return;
        }
        
        FightWaitType response = FightWaitType.parseWaitType(command);

        if(response.equals(FightWaitType.ATTACK)) {
            if(subCommand.equals("")) {
                self.setVariable(Variable.FIGHT_TARGET_INDEX, 1);
            } else {
                int index = Integer.parseInt(subCommand);
                int maxIndex = self.getVariable(Variable.FIGHT_TARGET_MAX_INDEX);
                
                if(index > maxIndex) {
                    throw new WeirdCommandException("1 부터 " + maxIndex + " 사이의 적을 선택해주세요");
                }

                self.setVariable(Variable.FIGHT_TARGET_INDEX, index);
                self.removeVariable(Variable.FIGHT_TARGET_MAX_INDEX);
            }
        } else if(response.equals(FightWaitType.DEFENCE)) {
            boolean isDefencing = self.getObjectVariable(Variable.IS_DEFENCING, false);

            if(isDefencing) {
                self.replyPlayer("이미 방어태세입니다");
                return;
            }
        } else if(response.equals(FightWaitType.ITEM)) {
            long itemId = ItemManager.getInstance().checkUse(self, subCommand, 1);

            if(itemId >= ItemList.LOW_EXP_POTION.getId() && itemId <= ItemList.HIGH_EXP_POTION.getId()) {
                throw new WeirdCommandException("전투중에는 경험치 포션을 사용할 수 없습니다");
            }

            self.setVariable(Variable.FIGHT_ITEM_ID, itemId);
        } else if(response.equals(FightWaitType.EQUIP)) {
            long equipId = EquipManager.getInstance().checkUse(self, EquipType.findByName(subCommand));
            self.setVariable(Variable.FIGHT_ITEM_ID, equipId);
        }

        self.setVariable(Variable.FIGHT_WAIT_TYPE, response);

        synchronized (self) {
            self.setVariable(Variable.IS_FIGHT_RESPONSE, true);
            self.notifyAll();
        }
    }

    private void endFight(@NonNull Entity self) {
        Id id = self.getId().getId();
        long objectId = self.getId().getObjectId();

        long fightId = this.getFightId(objectId);
        Set<Entity> entitySet = this.getEntitySet(fightId);
        Set<Player> playerSet = this.getPlayerSet(fightId);

        this.fightId.remove(objectId);
        entitySet.remove(self);

        self.removeVariable(Variable.IS_DEFENCING);
        self.removeVariable(Variable.IS_TURN);

        if(id.equals(Id.PLAYER)) {
            Player player = (Player) self;

            player.setDoing(player.getPrevDoing());
            playerSet.remove(player);
        } else {
            self.setDoing(Doing.NONE);
        }

        if(entitySet.isEmpty()) {
            this.entitySet.remove(fightId);
            this.playerSet.remove(fightId);
        }

        Config.unloadObject(self);
    }

    public long getFightId(long objectId) {
        return Objects.requireNonNull(fightId.get(objectId));
    }

    @NonNull
    public Set<Entity> getEntitySet(long fightId) {
        return Objects.requireNonNull(entitySet.get(fightId));
    }

    @NonNull
    public Set<Player> getPlayerSet(long fightId) {
        return Objects.requireNonNull(playerSet.get(fightId));
    }

}
