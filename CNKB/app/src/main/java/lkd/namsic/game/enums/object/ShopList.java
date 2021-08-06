package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@SuppressWarnings("SpellCheckingInspection")
public enum ShopList {

    EL_FLOWER("엘의 꽃집", 1L),
    HYEONG_SEOK_REINFORCE("형석의 제련소", 2L);

    public static final Map<String, ShopList> nameMap = new HashMap<>();
    public static final Map<Long, ShopList> idMap = new HashMap<>();

    static {
        for(ShopList value : ShopList.values()) {
            nameMap.put(value.displayName, value);
            idMap.put(value.id, value);
        }
    }

    @Getter
    @NonNull
    private final String displayName;

    @Getter
    private final long id;

    ShopList(@NonNull String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    @Nullable
    public static Long findByName(@NonNull String name) {
        ShopList shop = nameMap.get(name);
        return shop == null ? null : shop.id;
    }

    @Nullable
    public static String findById(long id) {
        ShopList shop = idMap.get(id);
        return shop == null ? null : shop.displayName;
    }

}
