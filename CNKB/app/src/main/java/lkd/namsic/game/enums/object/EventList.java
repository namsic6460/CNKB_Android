package lkd.namsic.game.enums.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

public enum EventList {
    
    NONE(0L),
    ENT_DAMAGED(1L),
    OAK_START(2L),
    SKILL_SMITE_PRE_DAMAGE(3L),
    IMP_DAMAGE(4L),
    SCAR_TURN(5L),
    SCAR_END(6L),
    CHARM_SELF_TURN(7L),
    CHARM_END(8L),
    CHARM_DAMAGE(9L),
    CHARM_TURN(10L),
    GOLEM_DAMAGED(11L),
    SKILL_STRINGS_OF_LIFE_START(12L),
    SKILL_STRINGS_OF_LIFE_INJECT(13L),
    STRINGS_OF_LIFE_DEATH(14L),
    STRINGS_OF_LIFE_END(15L),
    RESIST_PRE_DAMAGED(16L),
    RESIST_TURN(17L),
    RESIST_END(18L),
    SILVER_SWORD_DAMAGE(19L),
    SILVER_SWORD_END(20L),
    SILVER_SET_DAMAGE(21L),
    SILVER_SET_END(22L),
    SILVER_CHESTPLATE_PRE_DAMAGED(23L),
    SILVER_CHESTPLATE_END(24L),
    ELEMENT_HEART_GEM_ATS_TURN(25L),
    ELEMENT_HEART_GEM_ATS_END(26L),
    ELEMENT_HEART_GEM_FIRE_TURN(27L),
    ELEMENT_HEART_GEM_FIRE_END(28L),
    RUBY_EARRING_DAMAGE(29L),
    RUBY_EARRING_END(30L),
    LYCANTHROPE_PAGE_2(31L),
    WOLF_OF_MOON_PAGE_2(32L),
    WOLF_OF_MOON_PAGE_3(33L),
    WOLF_OF_MOON_END(34L);
    
    public static final Map<Long, EventList> idMap = new HashMap<>();
    
    static {
        for(EventList value : EventList.values()) {
            idMap.put(value.id, value);
        }
        
        idMap.remove(NONE.id);
    }
    
    @Getter
    private final long id;
    
    EventList(long id) {
        this.id = id;
    }
    
    @NonNull
    public static EventList findById(long id) {
        return Objects.requireNonNull(idMap.get(id));
    }
    
}
