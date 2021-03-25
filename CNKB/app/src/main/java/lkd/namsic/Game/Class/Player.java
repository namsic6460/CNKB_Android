package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentArrayList;
import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.LogData;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Event.Event;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Player extends Entity {

    @Setter
    String nickName;
    @Setter
    String image;
    @Setter
    String currentTitle;
    @Setter
    String recentRoom;

    @Setter
    boolean pvp;

    long lastTime;

    LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    LimitInteger adv = new LimitInteger(0, 0, Integer.MAX_VALUE);
    LimitLong exp = new LimitLong(0, 0L, Long.MAX_VALUE);

    LocalDateTime lastDateTime;

    ConcurrentHashMap<LogData, Long> log = new ConcurrentHashMap<>();

    public Player(@NonNull String name, int lv, long money, @NonNull Location location,
                  @NonNull Doing doing, @NonNull ConcurrentHashMap<StatType, Integer> basicStat,
                  @NonNull ConcurrentHashSet<Long> equip,
                  @NonNull ConcurrentHashMap<Long, ConcurrentHashMap<StatType, Integer>> buff,
                  @NonNull ConcurrentHashMap<Long, Integer> inventory,
                  @NonNull ConcurrentHashSet<Long> equipInventory,
                  @NonNull ConcurrentHashMap<Id, ConcurrentHashSet<Long>> enemies,
                  @NonNull ConcurrentHashMap<String, ConcurrentArrayList<Event>> events,
                  @NonNull ConcurrentHashMap<LogData, Long> log) {
        super(name, lv, money, location, doing, basicStat, equip, buff, inventory, equipInventory, enemies, events);

        this.id.setId(Id.PLAYER);

        this.log.put(LogData.LOG_COUNT, 0L);
        this.setLog(log);
    }

    public boolean checkChat() {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - this.lastTime;
        this.lastTime = currentTime;

        return diffTime >= 500;
    }

    public void setLog(Map<LogData, Long> log) {
        for(Map.Entry<LogData, Long> entry : log.entrySet()) {
            this.setLog(entry.getKey(), entry.getValue());
        }
    }

    public void setLog(LogData logData, long count) {
        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count == 0) {
            this.log.remove(logData);
        } else {
            this.log.put(logData, count);
        }
    }

    public long getLog(LogData logData) {
        Long value = this.log.get(logData);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addLog(LogData logData, long count) {
        this.setLog(logData, this.getLog(logData) + count);
        this.log.put(LogData.LOG_COUNT, Objects.requireNonNull(this.log.get(LogData.LOG_COUNT)) + 1);
    }

    public boolean canEquip(long equipId) {
        Equipment equipment = Config.getObject(Id.EQUIPMENT, equipId);

        return equipment.getTotalLimitLv().isInRange(this.lv.get()) &&
                Config.compareMap(equipment.getLimitStat(), this.basicStat, false);
    }

    @Override
    public boolean setMoney(long money) {
        long gap = money - this.getMoney();
        boolean isCancelled = super.setMoney(money);

        if(!isCancelled) {
            this.addLog(LogData.TOTAL_MONEY, gap);

            if(this.getLog(LogData.MAX_MONEY) < money) {
                this.setLog(LogData.MAX_MONEY, money);
            }
        } else if(gap < 0) {
            gap *= -1;

            if(this.getLog(LogData.MAX_PAYMENT) < gap) {
                this.setLog(LogData.MAX_PAYMENT, gap);
            }
        }

        return isCancelled;
    }

    @Override
    public boolean setField(int fieldX, int fieldY, int distance) {
        boolean isCancelled = super.setField(fieldX, fieldY, distance);

        if(!isCancelled) {
            this.addLog(LogData.FIELD_MOVE_DISTANCE, distance);
        }

        return isCancelled;
    }

    @Override
    public boolean setMap(int worldX, int worldY, int distance) {
        boolean isCancelled = super.setField(worldX, worldY, distance);

        if(!isCancelled) {
            this.addLog(LogData.MAP_MOVE_DISTANCE, distance);
        }

        return isCancelled;
    }

    @Override
    public void addBuff(long time, StatType statType, int stat) {
        super.addBuff(time, statType, stat);
        this.addLog(LogData.BUFF_RECEIVED, 1);
    }

    @Override
    public void revalidateStat() {
        super.revalidateStat();
        this.addLog(LogData.STAT_UPDATED, 1);
    }

    public String getFileName() {
        return this.getName() + "-" + this.getImage();
    }

    //[테스트 칭호] 남식(Lv.123)
    public String getDisplayName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + "(Lv." + this.getLv() + ")";
    }

}
