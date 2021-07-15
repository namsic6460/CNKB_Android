package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.object_list.MapList;
import lkd.namsic.game.event.MoveEvent;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Boss;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Equipment;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Player;

public class MoveManager {

    private static final MoveManager instance = new MoveManager();

    public static MoveManager getInstance() {
        return instance;
    }

    public boolean setField(@NonNull Entity self, int fieldX, int fieldY) {
        return setField(self, fieldX, fieldY, self.getFieldDistance(new Location(0, 0, fieldX, fieldY)));
    }

    public boolean setField(@NonNull Entity self, int fieldX, int fieldY, int distance) {
        if(distance < 0) {
            throw new NumberRangeException(distance, 0);
        }

        boolean isCancelled = MoveEvent.handleEvent(self, self.getEvents(MoveEvent.class.getName()), distance, true);

        if(!isCancelled) {
            if (self.getId().getId().equals(Id.PLAYER)) {
                ((Player) self).addLog(LogData.FIELD_MOVE_DISTANCE, distance);
            }

            self.getLocation().setField(fieldX, fieldY);

            GameMap map = Config.loadMap(self.getLocation());

            boolean gotGold = false;
            boolean gotItem = false;
            boolean gotEquip = false;
            StringBuilder innerBuilder = new StringBuilder("획득한 골드: ");

            long money = map.getMoney(self.getLocation());
            if (money != 0) {
                gotGold = true;

                self.addMoney(money);
                map.getMoney().remove(self.getLocation());
            }

            innerBuilder.append(money)
                    .append("G");

            innerBuilder.append("\n\n---획득한 아이템---");

            Map<Long, Integer> itemMap = map.getItem().get(self.getLocation());
            if (itemMap != null) {
                long itemId;
                int count;
                Item item;

                for (Map.Entry<Long, Integer> entry : itemMap.entrySet()) {
                    itemId = entry.getKey();
                    count = entry.getValue();

                    gotItem = true;
                    self.addItem(itemId, count, false);

                    item = Config.getData(Id.ITEM, itemId);
                    innerBuilder.append("\n")
                            .append(item.getName())
                            .append(" ")
                            .append(count)
                            .append("개");
                }

                map.getItem().remove(self.getLocation());
            }

            if (!gotItem) {
                innerBuilder.append("\n획득한 아이템이 없습니다");
            }

            innerBuilder.append("\n\n---획득한 장비---");

            Set<Long> equipSet = map.getEquip().get(self.getLocation());
            if (equipSet != null) {
                Equipment equipment;

                for (long equipId : equipSet) {
                    gotEquip = true;
                    self.addEquip(equipId);

                    equipment = Config.getData(Id.EQUIPMENT, equipId);
                    innerBuilder.append("\n")
                            .append(equipment.getName());
                }

                map.getEquip().remove(self.getLocation());
            }

            if (!gotEquip) {
                innerBuilder.append("\n획득한 장비가 없습니다");
            }

            if (gotGold || gotItem || gotEquip) {
                if (self.getId().getId().equals(Id.PLAYER)) {
                    ((Player) self).replyPlayer("필드에서 무언가를 주웠습니다", innerBuilder.toString());
                }
            }

            if (self.getId().getId().equals(Id.PLAYER) && (fieldX != 1 && fieldY != 1)) {
                Set<Long> attackMonsters = new HashSet<>();
                Set<Long> attackBosses = new HashSet<>();

                Monster monster;
                for (long monsterId : map.getEntity(Id.MONSTER)) {
                    monster = Config.getData(Id.MONSTER, monsterId);

                    if (self.getFieldDistance(monster.getLocation()) <= Config.RECOGNIZE_DISTANCE) {
                        double attackedPercent = Math.min(Config.ATTACKED_PERCENT + (monster.getLv().get() -
                                self.getLv().get()) * Config.ATTACKED_PERCENT_INCREASE, Config.MAX_ATTACKED_PERCENT);

                        if (Math.random() < attackedPercent) {
                            attackMonsters.add(monsterId);
                        }
                    }
                }

                Boss boss;
                for (long bossId : map.getEntity(Id.BOSS)) {
                    boss = Config.getData(Id.BOSS, bossId);

                    if (self.getFieldDistance(boss.getLocation()) <= Config.RECOGNIZE_DISTANCE) {
                        attackBosses.add(bossId);
                    }
                }

                if (!(attackMonsters.isEmpty() && attackBosses.isEmpty())) {
                    Map<Id, Set<Long>> enemies = new HashMap<>();

                    if (!attackMonsters.isEmpty()) {
                        enemies.put(Id.MONSTER, attackMonsters);
                    }

                    if (!attackBosses.isEmpty()) {
                        enemies.put(Id.BOSS, attackBosses);
                    }

                    FightManager.getInstance().startFight((Player) self, enemies);
                }
            }

            Config.unloadMap(map);
        }

        return isCancelled;
    }

    public boolean setMap(@NonNull Entity self, int x, int y) {
        return this.setMap(self, new Location(x, y),true);
    }

    public boolean setMap(@NonNull Entity self, int x, int y, int fieldX, int fieldY) {
        return this.setMap(self, new Location(x, y, fieldX, fieldY), self.getMapDistance(new Location(x, y)), false);
    }

    public boolean setMap(@NonNull Entity self, Location location) {
        return this.setMap(self, location, false);
    }

    public boolean setMap(@NonNull Entity self, Location location, boolean isToBase) {
        return this.setMap(self, location, self.getMapDistance(location), isToBase);
    }

    public boolean setMap(@NonNull Entity self, Location location, int distance, boolean isToBase) {
        if(isToBase) {
            return this.setMap(self, location.getX().get(), location.getY().get(), 1, 1, distance,true);
        } else {
            return this.setMap(self, location.getX().get(), location.getY().get(),
                    location.getFieldX().get(), location.getFieldY().get(), distance, false);
        }
    }

    public boolean setMap(@NonNull Entity self, int x, int y, int fieldX, int fieldY, int distance, boolean isToBase) {
        if(distance <= 0) {
            throw new NumberRangeException(distance, 1);
        }

        boolean isCancelled = MoveEvent.handleEvent(self, self.getEvents(MoveEvent.class.getName()), distance, false);

        if(!isCancelled) {
            if(self.getId().getId().equals(Id.PLAYER)) {
                ((Player) self).addLog(LogData.MAP_MOVE_DISTANCE, distance);
            }

            GameMap prevMap = Config.loadMap(self.getLocation());
            prevMap.removeEntity(self);
            Config.unloadMap(prevMap);

            GameMap moveMap = Config.loadMap(x, y);

            if(self.getId().getId().equals(Id.PLAYER) && self.getLv().get() < moveMap.getRequireLv().get()) {
                throw new NumberRangeException(self.getLv().get(), moveMap.getRequireLv().get(), Config.MAX_LV);
            }

            self.getLocation().setMap(x, y);
            if(isToBase) {
                this.setField(self, moveMap.getLocation().getFieldX().get(), moveMap.getLocation().getFieldY().get());
            } else {
                this.setField(self, fieldX, fieldY);
            }

            moveMap.addEntity(self);

            if(self.getId().getId().equals(Id.PLAYER)) {
                if(!MapType.cityList().contains(moveMap.getMapType())) {
                    moveMap.respawn();
                }
            }

            Config.unloadMap(moveMap);
        }

        return isCancelled;
    }

    public void moveMap(@NonNull Player self, @NonNull String locationStr) {
        Location location;
        int x, y;

        String[] split = locationStr.split("-");
        if(split.length != 2) {
            location = MapList.findByName(locationStr);

            if(location == null) {
                throw new WeirdCommandException("좌표를 또는 맵의 이름을 정확하게 입력해주세요\n(예시 : " +
                        Emoji.focus("0-1") + " 또는 " + Emoji.focus("시작의 마을") + ")");
            }
        } else {
            try {
                x = Integer.parseInt(split[0]);
                y = Integer.parseInt(split[1]);
                location = new Location(x, y);
            } catch (NumberRangeException e) {
                throw new WeirdCommandException("맵은 " + Config.MIN_MAP_X + "-" + Config.MIN_MAP_Y + " 부터 " +
                        Config.MAX_MAP_X + "-" + Config.MAX_MAP_Y + " 의 범위로만 이동할 수 있습니다");
            }
        }

        if(location.equalsMap(self.getLocation())) {
            throw new WeirdCommandException("현재 위치로는 이동할 수 없습니다");
        }

        int dis = self.getMapDistance(location);
        int movableDis = self.getMovableDistance();

        if(dis > movableDis) {
            throw new WeirdCommandException("이동 가능한 거리보다 먼 거리에 있는 좌표입니다\n" +
                    "이동 가능 거리 : " + movableDis + ", 실제 거리: " + dis);
        } else {
            GameMap moveMap = Config.loadMap(location);

            if(moveMap.getName().equals(Config.INCOMPLETE)) {
                throw new WeirdCommandException("미완성 맵으로는 이동할 수 없습니다");
            }

            try {
                this.setMap(self, location, true);
            } catch (NumberRangeException e) {
                if(Objects.requireNonNull(e.getMessage()).endsWith(Integer.toString(Config.MAX_LV))) {
                    self.replyPlayer("해당 지역으로 이동하기 위한 요구 레벨이 부족합니다\n현재 레벨: " +
                            self.getLv().get() + "\n요구 레벨: " + moveMap.getRequireLv().get());
                    return;
                } else {
                    throw e;
                }
            }

            self.replyPlayer("이동을 완료했습니다\n현재 좌표: " + self.getLocation().toString());

            Config.unloadMap(moveMap);
        }
    }

    public void moveField(@NonNull Player self, @NonNull String locationStr) {
        String[] split = locationStr.split("-");
        if(split.length != 2) {
            throw new WeirdCommandException("좌표를 정확하게 입력해주세요\n(예시 : " +
                    Emoji.focus("1-2") + ")");
        }

        int x, y;
        Location location;

        try {
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            location = new Location(0, 0, x, y);
        } catch (NumberRangeException e) {
            throw new WeirdCommandException("필드는 " + Config.MIN_FIELD_X + "-" + Config.MIN_FIELD_Y + " 부터 " +
                    Config.MAX_FIELD_X + "-" + Config.MAX_FIELD_Y + " 의 범위로만 이동할 수 있습니다");
        }

        if (location.equalsField(self.getLocation())) {
            throw new WeirdCommandException("현재 위치로는 이동할 수 없습니다");
        }

        this.setField(self, x, y);
        self.replyPlayer("이동을 완료했습니다\n현재 좌표: " + self.getLocation().toString());
    }

}
