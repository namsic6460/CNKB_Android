package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.Config;
import lkd.namsic.game.Emoji;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.event.MoveEvent;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.MapClass;
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

        boolean isCancelled = MoveEvent.handleEvent(self.getEvents().get(MoveEvent.getName()), new Object[]{distance, true});

        if(!isCancelled) {
            if(self.getId().getId().equals(Id.PLAYER)) {
                ((Player) self).addLog(LogData.FIELD_MOVE_DISTANCE, distance);
            }

            self.getLocation().setField(fieldX, fieldY);

            MapClass map = null;

            try {
                map = Config.loadMap(self.getLocation());

                long money = map.getMoney(self.getLocation());
                if(money != 0) {
                    self.addMoney(money);
                    map.getMoney().remove(self.getLocation());
                }

                Map<Long, Integer> item = map.getItem().get(self.getLocation());
                if(item != null) {
                    for(Map.Entry<Long, Integer> entry : item.entrySet()) {
                        self.addItem(entry.getKey(), entry.getValue());
                    }

                    map.getItem().remove(self.getLocation());
                }

                Set<Long> equip = map.getEquip().get(self.getLocation());
                if(equip != null) {
                    for (long equipId : equip) {
                        self.addEquip(equipId);
                    }

                    map.getEquip().remove(self.getLocation());
                }
            } finally {
                if(map != null) {
                    Config.unloadMap(map);
                }
            }
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

        boolean isCancelled = MoveEvent.handleEvent(self.getEvents().get(MoveEvent.getName()), new Object[]{distance, false});

        if(!isCancelled) {
            if(self.getId().getId().equals(Id.PLAYER)) {
                ((Player) self).addLog(LogData.MAP_MOVE_DISTANCE, distance);
            }

            MapClass prevMap = null;

            try {
                prevMap = Config.loadMap(self.getLocation());
                prevMap.removeEntity(self);
            } finally {
                if(prevMap != null) {
                    Config.unloadMap(prevMap);
                }
            }

            MapClass moveMap = null;

            try {
                moveMap = Config.loadMap(x, y);

                if(self.getLv().get() < moveMap.getRequireLv().get()) {
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
                        moveMap.spawn();
                    }
                }
            } finally {
                if(moveMap != null) {
                    Config.unloadMap(moveMap);
                }
            }
        }

        return isCancelled;
    }

    public void moveMap(@NonNull Player self, @NonNull String locationStr) {
        Location location;
        int x, y;

        String[] split = locationStr.split("-");
        if(split.length != 2) {
            String loc = ObjectList.mapList.inverse().get(locationStr);

            if(loc == null) {
                throw new WeirdCommandException("좌표를 또는 맵의 이름을 정확하게 입력해주세요\n(예시 : " +
                        Emoji.focus("0-1") + " 또는 " + Emoji.focus("시작의 마을") + ")");
            }

            split = loc.split("-");
        }

        try {
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            location = new Location(x, y);
        } catch (NumberRangeException e) {
            throw new WeirdCommandException("맵은 " + Config.MIN_MAP_X + "-" + Config.MIN_MAP_Y + " 부터 " +
                    Config.MAX_MAP_X + "-" + Config.MAX_MAP_Y + " 의 범위로만 이동할 수 있습니다");
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
            MapClass moveMap = null;

            try {
                moveMap = Config.loadMap(x, y);

                if(moveMap.getName().equals(Config.INCOMPLETE)) {
                    throw new WeirdCommandException("미완성 맵으로는 이동할 수 없습니다");
                }

                try {
                    this.setMap(self, location, true);
                } catch (NumberRangeException e) {
                    if(Objects.requireNonNull(e.getMessage()).endsWith(Integer.toString(Config.MAX_LV))) {
                        self.replyPlayer("해당 지역으로 이동하기 위한 요구 레벨이 부족합니다\n현재 레벨: " +
                                self.getLv().get() + ", 요구 레벨: " + moveMap.getRequireLv().get());
                        return;
                    } else {
                        throw e;
                    }
                }

                self.replyPlayer("이동을 완료했습니다\n현재 좌표: " + self.getLocation().toString());
            } finally {
                if(moveMap != null) {
                    Config.unloadMap(moveMap);
                }
            }
        }
    }

    public void moveField(@NonNull Player self, @NonNull String locationStr) {
        String[] split = locationStr.split("-");
        if(split.length != 2) {
            throw new WeirdCommandException("좌표를 정확하게 입력해주세요\n(예시 : " +
                    Emoji.focus("0-1") + ")");
        }

        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);

        try {
            if (new Location(0, 0, x, y).equalsField(self.getLocation())) {
                throw new WeirdCommandException("현재 위치로는 이동할 수 없습니다");
            }
        } catch (NumberRangeException e) {
            throw new WeirdCommandException("좌표를 정확하게 입력해주세요\n(예시 : " +
                    Emoji.focus("0-1") + ")");
        }

        this.setField(self, x, y);
        self.replyPlayer("이동을 완료했습니다\n현재 좌표: " + self.getLocation().toString());
    }

}
