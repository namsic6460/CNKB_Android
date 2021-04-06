package lkd.namsic.Game.GameObject;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

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
import lkd.namsic.Game.Enum.WaitResponse;
import lkd.namsic.Game.Event.ItemUseEvent;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.ObjectNotFoundException;
import lkd.namsic.Game.Exception.WeirdDataException;
import lkd.namsic.Service.KakaoTalk;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Player extends Entity {

    @NonNull
    String sender;

    @Setter
    @NonNull
    String nickName;

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

    @Setter
    boolean isGroup = true;

    private long lastTime;
    LimitLong runTime = new LimitLong(0L, 0L, Long.MAX_VALUE);

    Location baseLocation = new Location();

    LimitInteger sp = new LimitInteger(0, Config.MIN_SP, Config.MAX_SP);
    LimitInteger adv = new LimitInteger(0, 0, Integer.MAX_VALUE);
    LimitLong exp = new LimitLong(0, 0L, Long.MAX_VALUE);

    @Setter
    int lastDay = LocalDateTime.now().getDayOfMonth();

    Set<Long> achieve = new ConcurrentHashSet<>();
    Set<Long> research = new ConcurrentHashSet<>();

    Map<Long, Long> quest = new ConcurrentHashMap<>();
    Map<Long, ConcurrentHashSet<Long>> questNpc = new ConcurrentHashMap<>();
    Map<Long, Integer> clearedQuest = new ConcurrentHashMap<>();

    Map<WaitResponse, Long> responseChat = new ConcurrentHashMap<>();
    Map<String, Long> anyResponseChat = new ConcurrentHashMap<>();

    Map<MagicType, Integer> magic = new ConcurrentHashMap<>();
    Map<MagicType, Integer> resist = new ConcurrentHashMap<>();

    Map<Long, Integer> closeRate = new ConcurrentHashMap<>();

    Map<LogData, Long> log = new ConcurrentHashMap<LogData, Long>() {{
        put(LogData.PLAYED_DAY, 1L);
    }};

    public Player(@NonNull String sender, @NonNull String name, @NonNull String nickName,
                  @NonNull String image, @NonNull String recentRoom, long lastTime) {
        super(name);
        this.id.setId(Id.PLAYER);

        this.sender = sender;
        this.nickName = nickName;
        this.image = image;
        this.recentRoom = recentRoom;
        this.lastTime = lastTime;
    }

    //[테스트 칭호] 남식(Lv.123)
    @NonNull
    @Override
    public String getName() {
        return "[" + this.getCurrentTitle() + "] " + this.getNickName() + " (Lv." + this.getLv() + ")";
    }
