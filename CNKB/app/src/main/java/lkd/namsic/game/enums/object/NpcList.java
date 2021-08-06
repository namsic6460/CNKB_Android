package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lombok.Getter;

@SuppressWarnings("SpellCheckingInspection")
public enum NpcList {

    NONE("NONE", 0L),

    SECRET("???", 1L),
    ABEL("아벨", 2L),
    NOAH("노아", 3L),
    HYEONG_SEOK("형석", 4L),
    BOAM_E("봄이", 5L),
    EL("엘", 6L),
    JOON_SIK("준식", 7L),
    KANG_TAE_GONG("강태공", 8L),
    PEDRO("페드로", 9L),
    MOO_MYEONG("무명", 10L),
    SELINA("셀리나", 11L),
    SYLVIA("실비아", 12L),
    HANA("하나", 13L);

    public static final Map<String, NpcList> nameMap = new HashMap<>();
    public static final Map<Long, NpcList> idMap = new HashMap<>();

    static {
        for(NpcList value : NpcList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    private final long id;

    NpcList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        NpcList npc = nameMap.get(name);
        return npc == null ? null : npc.id;
    }

    @Nullable
    public static String findById(long id) {
        NpcList npc = idMap.get(id);
        return npc == null ? null : npc.displayName;
    }

    public static long checkByName(@NonNull Player player, @NonNull String npcName) {
        Long npcId = NpcList.findByName(npcName);
        GameMap map = Config.getMapData(player.getLocation());

        if(npcId == null || !map.getEntity(Id.NPC).contains(npcId) ||
                npcId == NpcList.SECRET.getId() || npcId == NpcList.ABEL.getId()) {
            throw new WeirdCommandException("해당 NPC 를 찾을 수 없습니다\n" +
                    "존재하지 않거나 현재 맵에 없는 NPC 일 수 있습니다\n" +
                    "현재 위치 정보를 확인해보세요");
        }

        return npcId;
    }

}
