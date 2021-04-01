package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Base.LimitLong;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.LogData;
import lkd.namsic.Game.Enum.MagicType;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Player extends Entity {

    @Setter
    @NonNull
    String nickName;

    @Setter
    @NonNull
    String image;

    Set<String> title = new ConcurrentHashSet<String>() {{
        add("초심자");
    }};

    @NonNull
    String currentTitle = "초심자";

    @Setter
    @NonNull
    String recentRoom;

    @Setter
    boolean pvp = false;

    private long lastTime;

    Location baseLocation = new Location();

    LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    LimitInteger adv = new LimitInteger(0, 0, Integer.MAX_VALUE);
    LimitLong exp = new LimitLong(0, 0L, Long.MAX_VALUE);

    LocalDateTime lastDateTime = LocalDateTime.now();

    Set<Long> achieve = new ConcurrentHashSet<>();
    Set<Long> research = new ConcurrentHashSet<>();

    Set<Long> quest = new ConcurrentHashSet<>();
    Set<Long> questNpc = new ConcurrentHashSet<>();
    Set<Long> clearedQuest = new ConcurrentHashSet<>();

    Map<MagicType, Integer> magic = new ConcurrentHashMap<>();
    Map<MagicType, Integer> resist = new ConcurrentHashMap<>();

    Map<Long, Integer> closeRate = new ConcurrentHashMap<>();

    Map<LogData, Long> log = new ConcurrentHashMap<LogData, Long>() {{
        put(LogData.LOG_COUNT, 0L);
    }};

    public Player(@NonNull String name, @NonNull String nickName, @NonNull String image,
                  @NonNull String recentRoom, long lastTime) {
        super(name);
        this.id.setId(Id.PLAYER);

        this.nickName = nickName;
        this.image = image;
        this.recentRoom = recentRoom;
        this.lastTime = lastTime;
    }

    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    public String getDisplayName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + "(Lv." + this.getLv() + ")";
    }

    public boolean checkChat() {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - this.lastTime;
        this.lastTime = currentTime;

        return diffTime >= 500;
    }



    public void setLog(@NonNull Map<LogData, Long> log) {
        for(Map.Entry<LogData, Long> entry : log.entrySet()) {
            this.setLog(entry.getKey(), entry.getValue());
        }
    }

    public void setLog(@NonNull LogData logData, long count) {
        if(count < 0) {
            throw new NumberRangeException(count, 0);
        }

        if(count == 0) {
            this.log.remove(logData);
        } else {
            this.log.put(logData, count);
        }
    }

    public long getLog(@NonNull LogData logData) {
        Long value = this.log.get(logData);

        if(value == null) {
            return 0;
        } else {
            return value;
        }
    }

    public void addLog(@NonNull LogData logData, long count) {
        this.setLog(logData, this.getLog(logData) + count);
        this.log.put(LogData.LOG_COUNT, this.getLog(LogData.LOG_COUNT) + 1);
    }

    public boolean canEquip(long equipId) {
        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);

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
    public boolean setMap(int x, int y, int fieldX, int fieldY, int distance) {
        boolean isCancelled = super.setMap(x, y, fieldX, fieldY, distance);

        if(!isCancelled) {
            this.addLog(LogData.MAP_MOVE_DISTANCE, distance);
        }

        return isCancelled;
    }

    @Override
    public void setBuff(long time, @NonNull StatType statType, int stat) {
        super.setBuff(time, statType, stat);
        this.addLog(LogData.BUFF_RECEIVED, 1);
    }

    @Override
    public void death() {
        super.death();

        this.setStat(StatType.HP, 1);
        this.location.set(((Player) this).baseLocation);

        Random random = new Random();
        double loseMoneyPercent = random.nextDouble() * 0.1 + 0.05;
        int dropItem = random.nextInt(4);

        long loseMoney = (long) (this.getMoney() * loseMoneyPercent * 0.5);
        this.addMoney(loseMoney);
        this.dropMoney(loseMoney);

        List<Long> keys;
        long itemId;
        int count;
        for(int i = 0; i < dropItem; i++) {
            keys = new ArrayList<>(this.inventory.keySet());
            itemId = keys.get(random.nextInt(keys.size()));
            count = random.nextInt(this.getItem(itemId));

            this.dropItem(itemId, count);
        }

        this.addLog(LogData.DEATH, 1);
    }

    @Override
    public void revalidateStat() {
        super.revalidateStat();
        this.addLog(LogData.STAT_UPDATED, 1);
    }

    @Override
    public boolean canFight(@NonNull Entity enemy) {
        boolean flag = super.canFight(enemy);

        if(flag) {
            if(this.getDoing().equals(Doing.FIGHT)) {
                return false;
            }

            List<Doing> doingList = Doing.nonFightDoing();

            if(enemy instanceof Player) {
                Player player = (Player) enemy;

                doingList.add(Doing.FIGHT);
                return player.isPvp() && this.isPvp() && !doingList.contains(player.getDoing());
            } else {
                return !doingList.contains(enemy.getDoing());
            }
        } else {
            return false;
        }
    }

    @NonNull
    public String getFileName() {
        return this.getName() + "-" + this.getImage();
    }

}
