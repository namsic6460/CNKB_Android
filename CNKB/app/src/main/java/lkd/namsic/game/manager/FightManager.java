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
import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.base.WrappedObject;
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
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.event.FightEndEvent;
import lkd.namsic.game.event.SelfTurnEvent;
import lkd.namsic.game.event.StartFightEvent;
import lkd.namsic.game.event.TurnEvent;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Skill;
import lkd.namsic.setting.Logger;

public class FightManager {

    private static final FightManager instance = new FightManager();

    private final Map<Long, Long> runPreventMap = new ConcurrentHashMap<>();
    private final Map<Long, Long> pvpPreventMap = new ConcurrentHashMap<>();

    public final Map<IdClass, Long> fightId = new ConcurrentHashMap<>();
    public final Map<Long, Set<Entity>> entityMap = new ConcurrentHashMap<>();
    public final Map<Long, Set<Player>> playerMap = new ConcurrentHashMap<>();
    public final Map<Long, Map<Integer, Entity>> targetMap = new ConcurrentHashMap<>();
    public final Map<Entity, List<Entity>> castingMap = new ConcurrentHashMap<>();
    public final Map<Entity, List<Entity>> castedMap = new ConcurrentHashMap<>();

    public static FightManager getInstance() {
        return instance;
    }

    public boolean tryFight(@NonNull Player self, @NonNull String targetStr, boolean isFightOne) {
        GameMap map = Config.getMapData(self.getLocation());

        if (MapType.cityList().contains(map.getMapType())) {
            throw new WeirdCommandException("??????????????? ????????? ??? ??? ????????????");
        }

        Long playerId = Config.PLAYER_ID.get(targetStr);
        if (playerId != null) {
            if (self.getId().getObjectId() == playerId) {
                throw new WeirdCommandException("?????? ???????????? ????????? ??? ????????????");
            }

            Player player = Config.getData(Id.PLAYER, playerId);

            if (!self.getLocation().equalsMap(player.getLocation())) {
                throw new WeirdCommandException("?????? ??????????????? ?????? ?????? ?????? ????????????");
            }

            if (player.getCurrentTitle().equals("?????????")) {
                String msg = "???????????? ???????????? ?????????...";

                if (self.getStat(StatType.HP) > 1) {
                    self.addBasicStat(StatType.HP, -1);
                    msg += "\n?????? 1???????????? ??????????????????";
                }

                throw new WeirdCommandException(msg);
            }

            if (self.canFight(player)) {
                long currentTime = System.currentTimeMillis();

                long id = self.getId().getObjectId();
                long preventTime = runPreventMap.getOrDefault(id, 0L);
                if (preventTime > currentTime) {
                    throw new WeirdCommandException("???????????? ????????? ??? 30??? ????????? ????????? ??? ????????????");
                } else if (preventTime != 0) {
                    runPreventMap.remove(id);
                }

                preventTime = runPreventMap.getOrDefault(playerId, 0L);
                if (preventTime > currentTime) {
                    throw new WeirdCommandException("?????? ????????? ?????? ????????? ??????????????????\n(?????? ?????? ??? 30?????? PvP ??????)");
                } else if (preventTime != 0) {
                    runPreventMap.remove(playerId);
                }

                preventTime = pvpPreventMap.getOrDefault(id, 0L);
                if (preventTime != 0) {
                    runPreventMap.remove(id);
                }

                preventTime = pvpPreventMap.getOrDefault(playerId, 0L);
                if (preventTime > currentTime) {
                    throw new WeirdCommandException("?????? ????????? ?????? ????????? ??????????????????\n(PvP ?????? ??? 30?????? PvP ??????)");
                } else if (preventTime != 0) {
                    pvpPreventMap.remove(playerId);
                }

                if (self.getLv() / 2 > player.getLv()) {
                    throw new WeirdCommandException("?????? ????????? ????????? ????????? ????????? ????????? ??????????????? ????????? ??? ????????????\n\n" +
                            "?????? ????????? ???????????????\n-?????? ??? ?????????-");
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
            if (size < index || index < 0) {
                index -= size;
                set = map.getEntity(Id.BOSS);

                if (set.size() < index) {
                    throw new WeirdCommandException("??? ??? ?????? ???????????????");
                } else {
                    id = Id.BOSS;
                }
            }

            List<Long> list = new ArrayList<>(set);
            Collections.sort(list);

            Entity target = Config.getData(id, list.get(index - 1));
            if (!self.canFight(target)) {
                throw new WeirdCommandException("????????? ??? ??? ?????? ????????????");
            }

            this.startFight(self, target, isFightOne);
        }

        return true;
    }

    public void startFight(@NonNull Player self, @NonNull Entity enemy, boolean isFightOne) {
        self.addLog(LogData.FIGHT, 1);

        self = Config.loadObject(self.getId().getId(), self.getId().getObjectId());
        enemy = Config.loadObject(enemy.getId().getId(), enemy.getId().getObjectId());

        this.setPrevDoing(self);
        this.setDoing(self, isFightOne);

        if (this.checkInject(self, enemy, isFightOne)) {
            return;
        }

        long fightId = self.getId().getObjectId();
        String is = " (???/???) ";

        Random random = new Random();

        Set<Entity> entitySet = this.getEntitySet(fightId);
        Set<Player> playerSet = this.getPlayerSet(fightId);
        while (true) {
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

            boolean casting = this.castingMap.containsKey(attacker);

            Map<Integer, Entity> targets = new HashMap<>();
            this.checkTarget(attacker, entitySet, targets, casting);
            this.targetMap.put(fightId, targets);

            WrappedObject<Entity> wrappedAttacker = new WrappedObject<>(attacker);

            String eventName;

            for(Entity entity : entitySet) {
                eventName = TurnEvent.getName();
                TurnEvent.handleEvent(entity, entity.getEvent().get(eventName), entity.getEquipEvents(eventName), wrappedAttacker);
            }

            eventName = SelfTurnEvent.getName();
            SelfTurnEvent.handleEvent(attacker, attacker.getEvent().get(eventName), attacker.getEquipEvents(eventName), wrappedAttacker);

            if(!attacker.equals(wrappedAttacker.get())) {
                attacker = wrappedAttacker.get();
                SelfTurnEvent.handleEvent(attacker, attacker.getEvent().get(eventName),
                        attacker.getEquipEvents(eventName), wrappedAttacker);

                casting = this.castingMap.containsKey(attacker);

                targets = new HashMap<>();
                this.checkTarget(self, entitySet, targets, casting);
            }

            if(checkEnd(self, entitySet, playerSet, new HashSet<>())) {
                return;
            }

            Set<Player> exceptSet = new HashSet<>();

            if (attacker.getId().getId().equals(Id.PLAYER)) {
                player = (Player) attacker;

                player.setVariable(Variable.IS_TURN, true);

                Player.replyPlayersExcept(playerSet, attacker.getName() + "??? ????????????" +
                        "\n????????? ?????? ??????????????????(?????? 30???)", player);

                player.setVariable(Variable.FIGHT_WAIT_TYPE, FightWaitType.NONE);
                player.setVariable(Variable.FIGHT_TARGET_INDEX, 0);
                player.setVariable(Variable.FIGHT_TARGET_MAX_INDEX, entitySet.size() - 1);

                synchronized (player) {
                    while (true) {
                        if (casting) {
                            response = FightWaitType.WAIT;
                            player.replyPlayer("???????????? ?????? ?????? ??????????????????");

                            break;
                        }

                        int entitySize = entitySet.size();

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
                                player.replyPlayer("????????? ???????????? ????????? ?????? ???????????????");

                                response = FightWaitType.ATTACK;
                                player.setVariable(Variable.FIGHT_TARGET_INDEX, random.nextInt(entitySize - 1) + 1);
                            }

                            boolean usedItem = player.getObjectVariable(Variable.FIGHT_USED_ITEM, false);
                            if (response.equals(FightWaitType.ITEM)) {
                                if (usedItem) {
                                    player.replyPlayer("???????????? ?????? ????????? ??????????????????");
                                    continue;
                                }

                                player.setVariable(Variable.FIGHT_USED_ITEM, true);
                            } else {
                                if (usedItem) {
                                    player.removeVariable(Variable.FIGHT_USED_ITEM);
                                }
                            }

                            player.removeVariable(Variable.IS_FIGHT_RESPONSE);
                            player.removeVariable(Variable.IS_TURN);

                            if (response.equals(FightWaitType.ATTACK)) {
                                target = Objects.requireNonNull(targets.get(player.getVariable(Variable.FIGHT_TARGET_INDEX)));
                                player.removeVariable(Variable.FIGHT_TARGET_INDEX);
                            }

                            break;
                        }
                    }
                }
            } else {
                if (casting) {
                    response = FightWaitType.WAIT;
                } else {
                    Object[] patternData = ((Monster) attacker).onTurn(playerSet, exceptSet);
                    response = (FightWaitType) patternData[0];
                    target = (Entity) patternData[1];
                }
            }

            String baseMsg, msg;

            boolean success = random.nextBoolean();
            switch (response) {
                case WAIT:
                    baseMsg = "?????? ??????????????????";
                    msg = attacker.getFightName() + " (???/???) ?????? ????????? ?????? ?????? " + baseMsg;

                    if (player != null) {
                        if(!casting) {
                            player.replyPlayer(baseMsg);
                        }

                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                case ATTACK:
                    if (target == null) {
                        throw new NullPointerException("Target is null");
                    }

                    attacker.attack(target, true);

                    if (target.getId().getId().equals(Id.PLAYER)) {
                        exceptSet.add((Player) target);
                    }


                    if (player != null) {
                        exceptSet.add(player);
                    }

                    String targetName = target.getFightName();
                    Player.replyPlayersExcepts(playerSet, attacker.getFightName() + is + targetName +
                            " (???/???) ??????????????????\n\n" + targetName + "??? ?????? ??????\n" + target.getDisplayHp(), exceptSet);

                    break;

                case DEFENCE:
                    if (success) {
                        attacker.setVariable(Variable.IS_DEFENCING, true);
                        baseMsg = "?????? ????????? ??????????????????";
                    } else {
                        baseMsg = "?????? ???????????? ????????? ??????????????????";
                    }

                    msg = attacker.getFightName() + is + baseMsg;
                    if (player != null) {
                        player.replyPlayer(baseMsg);
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    break;

                case RUN:
                    if (success) {
                        baseMsg = "????????? ??????????????????";
                    } else {
                        baseMsg = "????????? ??????????????????";
                    }

                    msg = attacker.getFightName() + is + baseMsg;
                    if (player != null) {
                        player.replyPlayer(baseMsg);
                        Player.replyPlayersExcept(playerSet, msg, player);
                    } else {
                        Player.replyPlayers(playerSet, msg);
                    }

                    if (success) {
                        runPreventMap.put(attacker.getId().getObjectId(), System.currentTimeMillis() + Config.PREVENT_RUN_FIGHT_TIME);

                        if (attacker.equals(self)) {
                            Player.replyPlayersExcept(playerSet, "????????? ????????? ????????? ???????????? ????????? ?????????????????????", self);

                            for (Entity entity : new HashSet<>(entitySet)) {
                                this.endFight(entity);
                            }

                            return;
                        } else {
                            this.endFight(attacker);
                        }
                    }

                    break;

                case ITEM:
                case EQUIP:
                case SKILL:
                    long objectId = attacker.getObjectVariable(Variable.FIGHT_OBJECT_ID, ItemList.NONE.getId());
                    String other = attacker.getObjectVariable(Variable.FIGHT_OTHER);

                    if (response.equals(FightWaitType.SKILL)) {
                        SkillManager.getInstance().use(attacker, objectId, other, playerSet);
                    } else {
                        if (response.equals(FightWaitType.ITEM)) {
                            ItemManager.getInstance().use(attacker, objectId, other, 1);
                            baseMsg = ItemList.findById(objectId) + " (???/???) ??????????????????";
                        } else {
                            EquipManager.getInstance().use(attacker, objectId, other);
                            baseMsg = EquipList.findById(objectId) + " (???/???) ??????????????????";
                        }

                        msg = attacker.getFightName() + is + baseMsg;
                        if (player != null) {
                            Player.replyPlayersExcept(playerSet, msg, player);
                        } else {
                            Player.replyPlayers(playerSet, msg);
                        }
                    }

                    break;

                default:
                    throw new RuntimeException();
            }

            int waitTurn;
            long skillId;
            Skill skill;
            SkillUse use;
            Entity caster;

            List<Entity> targetList;
            for (Map.Entry<Entity, List<Entity>> entry : new HashMap<>(this.castingMap).entrySet()) {
                caster = entry.getKey();
                waitTurn = caster.getVariable(Variable.FIGHT_SKILL_CAST_WAIT_TURN);

                if (waitTurn == 0) {
                    targetList = entry.getValue();

                    skillId = caster.getObjectVariable(Variable.FIGHT_CASTING_SKILL, SkillList.NONE.getId());
                    skill = Config.getData(Id.SKILL, skillId);
                    use = Objects.requireNonNull(skill.getSkillUse());

                    use.useSkill(caster, targetList);

                    exceptSet.clear();
                    if (caster.getId().getId().equals(Id.PLAYER)) {
                        exceptSet.add((Player) caster);
                    }

                    for (Entity skillTarget : targetList) {
                        if (skillTarget.getId().getId().equals(Id.PLAYER)) {
                            exceptSet.add((Player) skillTarget);
                        }
                    }

                    Player.replyPlayersExcepts(playerSet, caster.getFightName() + is + skill.getName() +
                            " ??? ???????????? ??????????????????!", exceptSet);

                    caster.removeVariable(Variable.FIGHT_SKILL_CAST_WAIT_TURN);
                    caster.removeVariable(Variable.FIGHT_CASTING_SKILL);

                    List<Entity> castingList = this.castingMap.remove(caster);

                    if(castingList != null) {
                        List<Entity> castedList;

                        for (Entity casted : castingList) {
                            castedList = Objects.requireNonNull(this.castedMap.get(casted));

                            if(castedList.size() == 1) {
                                this.castedMap.remove(casted);
                            } else {
                                castedList.remove(caster);
                            }
                        }
                    }
                } else {
                    caster.setVariable(Variable.FIGHT_SKILL_CAST_WAIT_TURN, waitTurn - 1);
                }
            }

            if(checkEnd(self, entitySet, playerSet, exceptSet)) {
                return;
            }
        }
    }

    public boolean checkEnd(@NonNull Player self, @NonNull Set<Entity> entitySet,
                            @NonNull Set<Player> playerSet, @NonNull Set<Player> exceptSet) {
        IdClass killerId;
        Entity killer;
        for (Entity entity : new HashSet<>(entitySet)) {
            if (entity.getKiller() != null) {
                killerId = entity.getKiller();
                killer = Config.getData(killerId.getId(), killerId.getObjectId());

                if(entity.getId().getId().equals(Id.PLAYER)) {
                    exceptSet.add((Player) entity);
                }

                if(killerId.getId().equals(Id.PLAYER)) {
                    pvpPreventMap.put(entity.getId().getObjectId(), System.currentTimeMillis() + Config.PREVENT_PVP_FIGHT_TIME);
                    exceptSet.add((Player) killer);
                }

                Player.replyPlayersExcepts(playerSet, entity.getFightName() + " (???/???) " +
                        killer.getFightName() + " ??? ?????? ??????????????????", exceptSet);

                entity.setKiller(null);

                if (entity.equals(self)) {
                    Player.replyPlayersExcept(playerSet, "????????? ????????? ????????? ???????????? ????????? ?????????????????????", self);

                    for (Entity entity_ : new HashSet<>(entitySet)) {
                        this.endFight(entity_);
                    }

                    return true;
                } else {
                    this.endFight(entity);
                }
            }
        }

        if (entitySet.size() == 1) {
            if (playerSet.size() == 1) {
                self.replyPlayer("???????????? ?????????????????????");
            }

            this.endFight(self);
            return true;
        }

        return false;
    }

    private void checkTarget(@NonNull Entity self, @NonNull Set<Entity> entitySet,
                             @NonNull Map<Integer, Entity> targets, boolean casting) {
        boolean print = false;
        Player player = null;
        if(self.getId().getId().equals(Id.PLAYER)) {
            player = (Player) self;
            print = !casting;
        }

        List<Entity> list = new ArrayList<>(entitySet);
        list.sort((o1, o2) -> {
            boolean isPlayer1 = o1.getId().getId().equals(Id.PLAYER);
            boolean isPlayer2 = o2.getId().getId().equals(Id.PLAYER);

            if ((isPlayer1 && isPlayer2) || !(isPlayer1 || isPlayer2)) {
                return o1.getRealName().compareTo(o2.getRealName());
            }

            return isPlayer1 ? -1 : 1;
        });

        StringBuilder innerBuilder = null;

        if(print) {
            innerBuilder = new StringBuilder("---?????? ??????---\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/attack/a) [{??????}] - ????????? ???????????????(?????? ????????? ??? 1?????? ???????????????)\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/defence/d) - 50% ????????? ?????? ????????? ?????? ???????????? ?????? ????????? ???????????????\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/wait/w) - ?????? ????????? ?????? ????????????\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (?????????/item/i) {????????? ??????[.{??????}]} - ???????????? ???????????????(?????? ?????? ??????)\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/equip/e) {?????? ??????[.{??????}]} - ????????? ???????????????\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/skill/s) {?????? ??????[.{??????}]} - ????????? ???????????????\n")
                    .append(Emoji.LIST)
                    .append(" (??????/fight/f) (??????/??????/run/r) - 50% ????????? ???????????????\n\n??????: ")
                    .append(Emoji.focus("n ?????? ?????? 1"))
                    .append("\n\n---?????? ??????---");
        }

        int index = 1;
        for (Entity entity : list) {
            if (entity.equals(self)) {
                continue;
            }

            if(print) {
                innerBuilder.append("\n")
                        .append(index)
                        .append(". ")
                        .append(entity.getName())
                        .append(" - ?????? ?????? : [ ")
                        .append(entity.getDisplayHp())
                        .append(" ]");

                targets.put(index, entity);
            } else {
                if(player == null) {
                    if (entity.getId().getId().equals(Id.PLAYER)) {
                        targets.put(index, entity);
                    }
                } else {
                    targets.put(index, entity);
                }
            }

            index++;
        }

        if(print) {
            player.replyPlayer("????????? ????????????\n????????? ????????? 30??? ????????? ??????????????????", innerBuilder.toString());
        }
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
        if (isFightOne) {
            self.setDoing(Doing.FIGHT_ONE);
        } else {
            self.setDoing(Doing.FIGHT);
        }
    }

    private boolean checkInject(@NonNull Player self, @NonNull Entity enemy, boolean isFightOne) {
        if (Doing.fightList().contains(enemy.getDoing())) {
            StringBuilder innerBuilder = new StringBuilder("---??? ??????---");

            long fightId = this.getFightId(enemy.getId());
            Set<Entity> entitySet = this.getEntitySet(fightId);
            Set<Player> playerSet = this.getPlayerSet(fightId);

            this.fightId.put(self.getId(), fightId);

            for (Entity entity : entitySet) {
                innerBuilder.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(entity.getFightName());
            }

            entitySet.add(self);
            playerSet.add(self);

            self.replyPlayer("????????? ??????????????????\n??? ????????? ??????????????? ??????????????????", innerBuilder.toString());
            Player.replyPlayers(playerSet, self.getName() + " (???/???) ????????? ??????????????????!");

            Config.unloadObject(enemy);

            return true;
        } else {
            long objectId = self.getId().getObjectId();

            this.fightId.put(self.getId(), objectId);
            this.fightId.put(enemy.getId(), objectId);

            Set<Entity> entitySet = new HashSet<>();
            entitySet.add(self);
            entitySet.add(enemy);
            this.entityMap.put(objectId, entitySet);

            Set<Player> playerSet = new HashSet<>();
            playerSet.add(self);

            if (enemy.getId().getId().equals(Id.PLAYER)) {
                Player player = (Player) enemy;

                playerSet.add(player);

                this.setPrevDoing(player);

                player.addLog(LogData.FIGHT, 1);
                player.replyPlayer(self.getName() + " (???/???) ??? ????????? ?????????????????????");
            }

            this.setDoing(enemy, isFightOne);
            this.playerMap.put(objectId, playerSet);

            String eventName = StartFightEvent.getName();
            StartFightEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName), enemy, true);
            StartFightEvent.handleEvent(enemy, enemy.getEvent().get(eventName), enemy.getEquipEvents(eventName), self, false);

            self.replyPlayer(enemy.getName() + " (???/???) ??? ????????? ?????????????????????");

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
        if (!isTurn) {
            self.replyPlayer("?????? ????????? ?????? ????????????.\n?????? ????????? ????????? ??????????????????");
            return;
        }

        FightWaitType response = FightWaitType.parseWaitType(command);

        if (response.equals(FightWaitType.ATTACK)) {
            if (subCommand.equals("")) {
                self.setVariable(Variable.FIGHT_TARGET_INDEX, 1);
            } else {
                int index = Integer.parseInt(subCommand);
                int maxIndex = self.getVariable(Variable.FIGHT_TARGET_MAX_INDEX);

                if (index > maxIndex) {
                    throw new WeirdCommandException("1 ?????? " + maxIndex + " ????????? ?????? ??????????????????");
                }

                self.setVariable(Variable.FIGHT_TARGET_INDEX, index);
                self.removeVariable(Variable.FIGHT_TARGET_MAX_INDEX);
            }
        } else if (response.equals(FightWaitType.DEFENCE)) {
            boolean isDefencing = self.getObjectVariable(Variable.IS_DEFENCING, false);

            if (isDefencing) {
                self.replyPlayer("?????? ?????????????????????");
                return;
            }
        } else if (response.equals(FightWaitType.ITEM) || response.equals(FightWaitType.EQUIP) || response.equals(FightWaitType.SKILL)) {
            String objectName;
            String other = null;

            String[] split = subCommand.split("\\.");
            if (split.length == 2) {
                objectName = split[0];
                other = split[1];
            } else {
                objectName = subCommand;
            }

            long objectId;
            if (response.equals(FightWaitType.ITEM)) {
                objectId = ItemManager.getInstance().checkUse(self, objectName, other,1);

                if (objectId >= ItemList.LOW_EXP_POTION.getId() && objectId <= ItemList.HIGH_EXP_POTION.getId()) {
                    throw new WeirdCommandException("??????????????? ????????? ????????? ????????? ??? ????????????");
                }
            } else if (response.equals(FightWaitType.EQUIP)) {
                objectId = EquipManager.getInstance().checkUse(self, EquipType.findByName(objectName), other);
            } else {
                objectId = SkillManager.getInstance().checkUse(self, objectName, other);
            }

            self.setVariable(Variable.FIGHT_OBJECT_ID, objectId);
            self.setVariable(Variable.FIGHT_OTHER, other);
        }

        self.setVariable(Variable.FIGHT_WAIT_TYPE, response);

        synchronized (self) {
            self.setVariable(Variable.IS_FIGHT_RESPONSE, true);
            self.notifyAll();
        }
    }

    private void endFight(@NonNull Entity self) {
        String eventName = FightEndEvent.getName();
        FightEndEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName));

        long fightId = this.getFightId(self.getId());
        Set<Entity> entitySet = this.getEntitySet(fightId);
        Set<Player> playerSet = this.getPlayerSet(fightId);

        this.fightId.remove(self.getId());
        entitySet.remove(self);

        List<Entity> castedList = this.castedMap.remove(self);
        if (castedList != null) {
            List<Entity> castingList;

            for (Entity caster : castedList) {
                castingList = Objects.requireNonNull(this.castingMap.get(caster));

                if(castingList.size() == 1) {
                    this.castingMap.remove(caster);

                    if (caster.getId().getId().equals(Id.PLAYER)) {
                        ((Player) caster).replyPlayer("?????? ???????????? ?????????????????????");
                    }
                } else {
                    castingList.remove(self);
                }
            }
        }

        self.removeVariable(Variable.IS_DEFENCING);
        self.removeVariable(Variable.IS_TURN);

        if (self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            player.setDoing(player.getPrevDoing());
            playerSet.remove(player);
        } else {
            self.setDoing(Doing.NONE);
        }

        if (entitySet.isEmpty()) {
            this.entityMap.remove(fightId);
            this.playerMap.remove(fightId);
            this.targetMap.remove(fightId);
        }

        Config.unloadObject(self);
    }

    public long getFightId(IdClass id) {
        return Objects.requireNonNull(this.fightId.get(id));
    }

    @NonNull
    public Set<Entity> getEntitySet(long fightId) {
        return Objects.requireNonNull(this.entityMap.get(fightId));
    }

    @NonNull
    public Set<Player> getPlayerSet(long fightId) {
        return Objects.requireNonNull(this.playerMap.get(fightId));
    }

    @NonNull
    public Map<Integer, Entity> getTargetMap(long fightId) {
        return Objects.requireNonNull(this.targetMap.get(fightId));
    }

}
