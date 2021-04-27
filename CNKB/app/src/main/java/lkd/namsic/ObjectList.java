package lkd.namsic;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;

public class ObjectList {

    public final static BiMap<String, Long> itemList = HashBiMap.create(new HashMap<String, Long>() {{
        put("돌멩이", 1L);
        put("나뭇가지", 2L);
        put("나뭇잎", 3L);
        put("잡초", 4L);
        put("골드 주머니", 5L);
        put("골드 보따리", 6L);
        put("약초", 7L);
        put("마나 조각", 8L);
        put("유리조각", 9L);
        put("흙", 10L);
        put("모래", 11L);
        put("유리", 12L);
        put("유리병", 13L);

        put("하급 체력포션", 14L);
        put("중급 체력포션", 15L);
        put("상급 체력포션", 16L);
        put("하급 마나포션", 17L);
        put("중급 마나포션", 18L);
        put("상급 마나포션", 19L);

        put("돌", 20L);
        put("석탄", 21L);
        put("석영", 22L);
        put("구리", 23L);
        put("납", 24L);
        put("주석", 25L);
        put("니켈", 26L);
        put("철", 27L);
        put("리튬", 28L);
        put("청금석", 29L);
        put("레드스톤", 30L);
        put("은", 31L);
        put("금", 32L);
        put("발광석", 33L);
        put("화염석영", 34L);
        put("암흑석영", 35L);
        put("명청석", 36L);
        put("명적석", 37L);
        put("백금", 38L);
        put("무연탄", 39L);
        put("티타늄", 40L);
        put("투명석", 41L);
        put("다이아몬드", 42L);
        put("오리하르콘", 43L);
        put("적청석", 44L);
        put("랜디움", 45L);
        put("에이튬", 46L);

        put("가넷", 47L);
        put("자수정", 48L);
        put("아쿠아마린", 49L);
        put("에메랄드", 50L);
        put("진주", 51L);
        put("루비", 52L);
        put("페리도트", 53L);
        put("사파이어", 54L);
        put("오팔", 55L);
        put("토파즈", 56L);
        put("터키석", 57L);
    }});

}
