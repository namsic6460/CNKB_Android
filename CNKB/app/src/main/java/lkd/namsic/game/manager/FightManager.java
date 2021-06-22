package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import lkd.namsic.game.Config;
import lkd.namsic.game.Emoji;
import lkd.namsic.game.Variable;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Boss;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class FightManager {

    private static final FightManager instance = new FightManager();

    public static FightManager getInstance() {
        return instance;
    }

    public boolean tryFight(@NonNull Player self, @NonNull String targetStr) {
        MapClass map = Config.getMapData(self.getLocation());

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
                this.startFight(self, player);
            } else {
                return false;
            }
        } else {
            Map<Id, Set<Long>> enemies = new HashMap<>();
            String[] locationStr = targetStr.split("-");

            int fieldX, fieldY;
            Location location = null;

            try {
                fieldX = Integer.parseInt(locationStr[0]);
                fieldY = Integer.parseInt(locationStr[1]);

                location = new Location(0, 0, fieldX, fieldY);
            } catch (NumberFormatException | NumberRangeException ignore) {}

            if(location != null) {
                Set<Long> bossSet = new HashSet<>();
                Boss boss;
                for (long bossId : map.getEntity(Id.BOSS)) {
                    boss = Config.getData(Id.BOSS, bossId);

                    if (boss.getLocation().equalsField(location)) {
                        if(self.canFight(boss)) {
                            bossSet.add(bossId);
                        } else {
                            return false;
                        }
                    }
                }

                Set<Long> monsterSet = new HashSet<>();
                Monster monster;
                for(long monsterId : map.getEntity(Id.MONSTER)) {
                    monster = Config.getData(Id.MONSTER, monsterId);

                    if (monster.getLocation().equalsField(location)) {
                        if(self.canFight(monster)) {
                            monsterSet.add(monsterId);
                        } else {
                            return false;
                        }
                    }
                }

                int bossSize = bossSet.size();
                int monsterSize = monsterSet.size();

                if(bossSize + monsterSize == 0) {
                    throw new WeirdCommandException("해당 위치에는 몬스터 또는 보스가 존재하지 않습니다");
                }

                if(bossSize != 0) {
                    enemies.put(Id.BOSS, bossSet);
                }

                if(monsterSize != 0) {
                    enemies.put(Id.MONSTER, monsterSet);
                }

                this.startFight(self, enemies);
            } else {
                throw new WeirdCommandException("알 수 없는 대상입니다");
            }
        }

        return true;
    }

    public void startFight(@NonNull Player self, @NonNull Player enemy) {
        Map<Id, Set<Long>> enemies = new HashMap<>();

        Set<Long> set = new HashSet<>();
        set.add(enemy.getId().getObjectId());
        enemies.put(Id.PLAYER, set);

        this.startFight(self, enemies);
    }

    public void startFight(@NonNull Player self, @NonNull Map<Id, Set<Long>> enemies) {
        int size = enemies.size();
        if (size == 0) {
            throw new NumberRangeException(size, 1);
        }

        if (size == 1 && enemies.containsKey(Id.PLAYER)) {
            Player enemy = Config.getData(Id.PLAYER, (long) enemies.get(Id.PLAYER).toArray()[0]);
            self.replyPlayer(enemy.getName() + " (와/과)의 전투가 시작되었습니다");
        } else {
            StringBuilder innerMsg = new StringBuilder("---적 목록---");

            Id id;
            Entity enemy;
            for (Map.Entry<Id, Set<Long>> entry : enemies.entrySet()) {
                id = entry.getKey();

                for (long objectId : entry.getValue()) {
                    enemy = Config.getData(id, objectId);
                    innerMsg.append("\n")
                            .append(enemy.getName());
                }
            }

            self.replyPlayer("적 " + size + "명과의 전투가 시작되었습니다", innerMsg.toString());
        }

        self.setVariable(Variable.IS_TURN, false);

        Doing doing = self.getDoing();
        if (doing.equals(Doing.ADVENTURE)) {
            self.setPrevDoing(Doing.NONE);
        } else {
            self.setPrevDoing(doing);
        }

        self.setDoing(Doing.FIGHT);
        self.addLog(LogData.FIGHT, 1);

        long objectId = self.getId().getObjectId();
        int enemyCount = 0;

        Id enemyId;
        for (Map.Entry<Id, Set<Long>> entry : enemies.entrySet()) {
            enemyCount++;
            enemyId = entry.getKey();

            Entity enemy = null;
            for (long enemyObjectId : entry.getValue()) {
                try {
                    enemy = Config.loadObject(enemyId, enemyObjectId);

                    if (enemy.getDoing().equals(Doing.FIGHT)) {
                        StringBuilder innerMsg = new StringBuilder("---추가된 적 목록---");

                        Set<Player> playerSet = new HashSet<>();
                        if (enemyId.equals(Id.PLAYER)) {
                            playerSet.add((Player) enemy);
                        }

                        for (Map.Entry<Id, ConcurrentHashSet<Long>> newEntry : enemy.getEnemies().entrySet()) {
                            Id newEnemyId = newEntry.getKey();

                            for (long newEnemyObjectId : newEntry.getValue()) {
                                enemyCount++;
                                Entity newEnemy = null;

                                try {
                                    newEnemy = Config.loadObject(newEnemyId, newEnemyObjectId);

                                    self.addEnemy(newEnemyId, newEnemyObjectId);
                                    newEnemy.addEnemy(Id.PLAYER, objectId);

                                    if (newEnemyId.equals(Id.PLAYER)) {
                                        playerSet.add((Player) newEnemy);
                                    }

                                    innerMsg.append("\n")
                                            .append(Emoji.LIST)
                                            .append(newEnemy.getName());
                                } finally {
                                    if (newEnemy != null) {
                                        Config.unloadObject(newEnemy);
                                    }
                                }
                            }
                        }

                        self.addEnemy(enemyId, enemyObjectId);
                        enemy.addEnemy(Id.PLAYER, objectId);

                        Player.replyPlayers(playerSet, self.getName() + "(이/가) 전투에 난입했습니다!");

                        self.addLog(LogData.FIGHT_ENEMY, enemyCount);
                        self.replyPlayer(enemy.getRealName() + "(와/과) 전투중인 다른 적들이 본인을 경계하기 시작했습니다",
                                innerMsg.toString());

                        return;
                    } else {
                        self.addEnemy(enemy.getId().getId(), enemy.getId().getObjectId());
                        enemy.addEnemy(Id.PLAYER, objectId);

                        if (enemy.getId().getId().equals(Id.PLAYER)) {
                            Player player = (Player) enemy;

                            if (player.getDoing().equals(Doing.ADVENTURE)) {
                                player.setVariable(Variable.ADVENTURE_FIGHT, true);
                                player.setPrevDoing(Doing.NONE);
                            } else {
                                player.setPrevDoing(player.getDoing());
                            }

                            player.setVariable(Variable.IS_TURN, false);
                            player.addLog(LogData.FIGHT, 1);
                            player.addLog(LogData.FIGHT_ENEMY, enemyCount);

                            player.replyPlayer(self.getName() + " (와/과) 의 전투가 시작되었습니다");
                        }

                        enemy.setDoing(Doing.FIGHT);
                    }
                } finally {
                    if (enemy != null) {
                        Config.unloadObject(enemy);
                    }
                }
            }
        }

        self.addLog(LogData.FIGHT_ENEMY, enemyCount);

        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.e("Player.fightThread", e);
                throw new RuntimeException(e.getMessage());
            }

            //전투 중 난입을 고려하여 항상 초기화
            Set<Entity> entitySet = new HashSet<>();
            Set<Player> playerSet = new HashSet<>();

            //순서가 보장되어야 한다
            Map<Object[], Integer> atsMap = new LinkedHashMap<>();
            int ats;
            int sum = 0;

            try {
                for (Map.Entry<Id, ConcurrentHashSet<Long>> entry : self.getEnemies().entrySet()) {
                    Entity entity;
                    Id entityId = entry.getKey();

                    for (long entityObjectId : entry.getValue()) {
                        entity = Config.loadObject(entityId, entityObjectId);
                        entitySet.add(entity);

                        if (entityId.equals(Id.PLAYER)) {
                            playerSet.add((Player) entity);
                        }

                        ats = entity.getStat(StatType.ATS);
                        atsMap.put(new Object[]{entityId, entityObjectId}, ats);

                        sum += ats;
                    }
                }

                Config.loadObject(Id.PLAYER, objectId);

                entitySet.add(self);
                playerSet.add(self);

                ats = self.getStat(StatType.ATS);
                atsMap.put(new Object[]{Id.PLAYER, objectId}, ats);

                sum += ats;

                int playerSize = playerSet.size();
                if (playerSize == 0 || (playerSize == 1 && entitySet.size() == 1)) {
                    for (Entity entity : entitySet) {
                        this.endFight(entity);
                    }

                    if (playerSize == 1) {
                        self.replyPlayer("전투에서 승리하였습니다");
                    }

                    return;
                }

                Entity attacker = null;

                int randomValue = random.nextInt(sum);
                int currentWeight = 0;
                for (Map.Entry<Object[], Integer> entry : atsMap.entrySet()) {
                    Object[] key = entry.getKey();
                    ats = entry.getValue();

                    if (randomValue < currentWeight + ats) {
                        //이미 로드되어있기 때문에 데이터만 가져온다
                        attacker = Config.getData((Id) key[0], (long) key[1]);
                        break;
                    } else {
                        currentWeight += ats;
                    }
                }

                if (attacker == null) {
                    throw new NullPointerException();
                }

                if (attacker.getId().getId().equals(Id.PLAYER)) {
                    Player player = (Player) attacker;
                    player.setVariable(Variable.IS_TURN, true);

                    int index = 1;
                    HashMap<Integer, Entity> targets = new HashMap<>();

                    StringBuilder innerMsg = new StringBuilder("---행동 목록---\n")
                            .append(Emoji.LIST)
                            .append(" (전투/fight/f) (공격/attack/a) [{대상}] - 대상을 공격합니다(대상 미입력 시 1번을 공격합니다)\n")
                            .append(Emoji.LIST)
                            .append(" (전투/fight/f) (방어/defence/d) - 50% 확률로 다음 피해의 물리 데미지를 막고 일정량 회복합니다\n")
                            .append(Emoji.LIST)
                            .append(" (전투/fight/f) (대기/wait/w) - 아무 행동도 하지 않습니다\n")
                            .append(Emoji.LIST)
                            .append(" (전투/fight/f) (도망/도주/run/r) - 50% 확률로 도주합니다\n(예시: ")
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

                    for (Entity enemy : list) {
                        if (enemy.equals(attacker)) {
                            continue;
                        }

                        targets.put(index, enemy);
                        innerMsg.append("\n")
                                .append(index++)
                                .append(". ")
                                .append(enemy.getName())
                                .append(" - 남은 체력 : [ ")
                                .append(enemy.getDisplayHp())
                                .append(" ]");
                    }

                    player.setVariable(Variable.FIGHT_WAIT_TYPE, FightWaitType.NONE);
                    player.setVariable(Variable.FIGHT_TARGET_INDEX, 0);
                    player.setVariable(Variable.FIGHT_TARGET_MAX_INDEX, index - 1);

                    player.replyPlayer("당신의 턴입니다\n대상과 행동을 30초 이내에 선택해주세요",
                            innerMsg.toString());
                    Player.replyPlayersExcept(playerSet, attacker.getName() + "의 턴입니다" +
                            "\n유저의 턴을 기다려주세요(최대 30초)", player);

                    synchronized (player) {
                        while (true) {
                            try {
                                player.wait(Config.FIGHT_WAIT_TIME);
                            } catch (InterruptedException e) {
                                Logger.e("Entity.fightThread", e);
                                throw new RuntimeException(e.getMessage());
                            }

                            player.notifyAll();

                            Boolean isFightResponse = player.getObjectVariable(Variable.IS_FIGHT_RESPONSE, true);
                            if (isFightResponse) {
                                self.removeVariable(Variable.IS_FIGHT_RESPONSE);
                                break;
                            }
                        }
                    }

                    player.setVariable(Variable.IS_TURN, false);
                    FightWaitType response = player.getObjectVariable(Variable.FIGHT_WAIT_TYPE, FightWaitType.NONE);
                    int targetIndex = player.getVariable(Variable.FIGHT_TARGET_INDEX);

                    if (response.equals(FightWaitType.NONE)) {
                        response = FightWaitType.ATTACK;
                        targetIndex = random.nextInt(index);
                        player.replyPlayer("턴이 종료되어 랜덤한 적을 공격합니다");
                    }

                    boolean success = random.nextBoolean();
                    switch (response) {
                        case WAIT:
                            Player.replyPlayersExcept(playerSet, player.getNickName() +
                                    "(은/는) 아무 행동도 하지 않고 턴을 넘겼습니다", player);
                            player.replyPlayer("턴을 종료했습니다");

                            break;
                        case ATTACK:
                            Entity target = Objects.requireNonNull(targets.get(targetIndex));
                            attacker.removeVariable(Variable.FIGHT_TARGET_INDEX);
                            boolean isDeath = player.attack(target, false);

                            String targetName = target.getRealName();
                            String msg = player.getNickName() + "(이/가) " + targetName + "(을/를) 공격";

                            Set<Player> excepts = new HashSet<>();
                            excepts.add(player);

                            if (target.getId().getId().equals(Id.PLAYER)) {
                                excepts.add((Player) target);
                            }

                            if (isDeath) {
                                msg += "하여 " + targetName + "(을/를) 처치했습니다!";
                                this.endFight(target);
                            } else {
                                msg += "했습니다\n\n" + targetName + "의 남은 체력\n" + target.getDisplayHp();
                            }

                            Player.replyPlayersExcepts(playerSet, msg, excepts);

                            if (isDeath && target.equals(self)) {
                                for (Entity entity : entitySet) {
                                    this.endFight(entity);
                                }

                                Player.replyPlayersExcept(playerSet, "전투를 시작한 유저가 사망하여 전투가 종료되었습니다", self);
                                return;
                            }

                            break;
                        case DEFENCE:
                            if (success) {
                                player.setVariable(Variable.IS_DEFENCING, true);
                                player.replyPlayer("방어 태세로 전환했습니다");
                                Player.replyPlayersExcept(playerSet, player.getNickName() +
                                        "(이/가) 방어 태세로 전환헀습니다", player);
                            } else {
                                player.replyPlayer("방어 태세로의 전환에 실패했습니다");
                                Player.replyPlayersExcept(playerSet, player.getNickName() +
                                        "(은/는) 방어 태세로의 전환을 시도했으나 실패했습니다", player);
                            }

                            break;
                        case RUN:
                            if (success) {
                                this.endFight(attacker);
                                player.replyPlayer("도주에 성공하였습니다");
                                Player.replyPlayersExcept(playerSet, player.getNickName() +
                                        "(이/가) 도주에 성공헀습니다", player);

                                if (attacker.equals(self)) {
                                    for (Entity entity : entitySet) {
                                        this.endFight(entity);
                                    }

                                    Player.replyPlayersExcept(playerSet, "전투를 시작한 유저가 도주하여 전투가 종료되었습니다", self);
                                    return;
                                }
                            } else {
                                player.replyPlayer("도주에 실패하였습니다");
                                Player.replyPlayersExcept(playerSet, player.getNickName() +
                                        "(은/는) 도주를 시도헀으나 실패했습니다", player);
                            }

                            break;
                    }
                } else {
                    Object[] patternData = ((Monster) attacker).onTurn(playerSet);
                    Player player = (Player) patternData[0];
                    int pattern = (int) patternData[1];

                    if (player == null) {
                        Player.replyPlayers(playerSet, attacker.getName() + "(은/는) 아무 행동도 하지 않고 턴을 넘겼습니다");
                    } else {
                        if (pattern == 0) {
                            boolean isDeath = attacker.attack(player, false);
                            String msg = attacker.getName() + "(이/가) " + player.getName() + "(을/를) 공격했습니다";

                            if (isDeath) {
                                msg += "\n" + player.getName() + "(이/가) 사망했습니다";
                                this.endFight(player);
                            } else {
                                msg += "\n[ " + player.getName() + "의 남은 체력 ]\n" + player.getDisplayHp();
                            }

                            Player.replyPlayersExcept(playerSet, msg, player);

                            if (isDeath && player.equals(self)) {
                                for (Entity entity : entitySet) {
                                    this.endFight(entity);
                                }

                                Player.replyPlayersExcept(playerSet, "전투를 시작한 유저가 사망하여 전투가 종료되었습니다", self);
                                break;
                            }
                        } else {
                            //TODO: 스킬
                            throw new RuntimeException("TODO - Unreachable");
                        }
                    }
                }
            } finally {
                for (Entity entity : entitySet) {
                    Config.unloadObject(entity);
                }
            }
        }
    }

    public void fightCommand(@NonNull Player self, @NonNull String command, @Nullable String subCommand) {
        boolean isTurn = self.getObjectVariable(Variable.IS_TURN, false);
        if(!isTurn) {
            self.replyPlayer("현재 당신의 턴이 아닙니다.\n턴이 종료될 때까지 기다려주세요");
            return;
        }
        
        FightWaitType response = FightWaitType.parseWaitType(command);

        if(response.equals(FightWaitType.ATTACK)) {
            if(subCommand == null) {
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
        }

        self.setVariable(Variable.FIGHT_WAIT_TYPE, response);

        synchronized (self) {
            self.setVariable(Variable.IS_FIGHT_RESPONSE, true);
            self.notifyAll();
        }
    }

    public void endFight(@NonNull Entity self) {
        Id id = self.getId().getId();
        long objectId = self.getId().getObjectId();

        self.removeVariable(Variable.IS_DEFENCING);

        if(id.equals(Id.PLAYER)) {
            self.setDoing(((Player) self).getPrevDoing());
        } else {
            self.setDoing(Doing.NONE);
        }

        if(self.getEnemies().isEmpty()) {
            return;
        }

        Id enemyId;
        Map<Id, Set<Long>> copyMap = new HashMap<>(self.getEnemies());
        for(Map.Entry<Id, Set<Long>> entry : copyMap.entrySet()) {
            enemyId = entry.getKey();

            Entity enemy = null;
            for(long enemyObjectId : entry.getValue()) {
                try {
                    enemy = Config.loadObject(enemyId, enemyObjectId);

                    self.removeEnemy(enemyId, enemyObjectId);
                    enemy.removeEnemy(id, objectId);
                } finally {
                    if(enemy != null) {
                        Config.unloadObject(enemy);
                    }
                }
            }
        }
    }

}
