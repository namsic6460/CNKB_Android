package lkd.namsic.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import lkd.namsic.MainActivity;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class ObjectMaker {

    public static void start() {
        Logger.i("ObjectMaker", "Making objects...");

        Thread thread = new Thread(() -> {
            try {
                makeItem();
                makeEquip();
                makeMap();
                makeChat();
                makeNpc();

                Logger.i("ObjectMaker", "Object making is done!");
            } catch (Exception e) {
                Logger.e("ObjectMaker", e);
            }
        });
        MainActivity.startThread(thread);
    }

    private static void makeItem() {
        Item item = new Item("돌멩이", "평범한 돌멩이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("나뭇가지", "평범한 나뭇가지다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("나뭇잎", "평범한 나뭇잎이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("잡초", "평범한 잡초다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("골드 주머니", "골드가 소량 들어간 주머니다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.setUse((user, other) -> {
            int money = new Random().nextInt(10 * user.getLv().get()) + 50;
            
            user.addMoney(money);
            if(user instanceof Player) {
                ((Player) user).replyPlayer("골드 주머니를 사용하여 " + money + "G 를 획득했습니다");
            }
        });
        Config.unloadObject(item);

        item = new Item("골드 보따리", "골드가 들어간 주머니다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        item.setUse((user, other) -> {
            int money = new Random().nextInt(200 * user.getLv().get()) + 1000;

            user.addMoney(money);
            if(user instanceof Player) {
                ((Player) user).replyPlayer("골드 보따리를 사용하여 " + money + "G 를 획득했습니다");
            }
        });
        Config.unloadObject(item);

        item = new Item("약초", "약의 기운이 있는 풀이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("마나 조각", "마나의 기운이 있는 고체 마나 파편이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("유리조각", "유리 파편이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("흙", "땅에서 흔히 볼 수 있는 평번한 흙이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("모래", "땅에서 흔히 볼 수 있는 평번한 모래다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("유리", "투명한 고체 물질이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(11L, 3);
            put(21L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("유리병", "유리를 가공하여 만든 병이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(12L, 2);
            put(21L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("하급 체력포션", "최대 체력의 15%를 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(3L, 3);
            put(7L, 3);
            put(13L, 1);
            put(21L, 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 3);

            put(15L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("중급 체력포션", "최대 체력의 50%를 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(7L, 5);
            put(14L, 1);
            put(30L, 3);
            put(33L, 1);
            put(34L, 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 2);

            put(16L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("상급 체력포션", "체력을 모두 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(7L, 30);
            put(15L, 1);
            put(30L, 10);
            put(33L, 10);
            put(39L, 1);
            put(41L, 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(19L, 1);
            put(44L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("하급 마나포션", "최대 마나의 15%를 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(3L, 3);
            put(8L, 3);
            put(13L, 1);
            put(21L, 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 3);

            put(18L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("중급 마나포션", "최대 마나의 50%를 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(8L, 5);
            put(17L, 1);
            put(29L, 3);
            put(33L, 1);
            put(34L, 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 2);

            put(19L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("상급 마나포션", "마나를 모두 회복시켜주는 포션이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(8L, 30);
            put(18L, 1);
            put(29L, 10);
            put(33L, 10);
            put(39L, 1);
            put(41L, 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(16L, 1);
            put(44L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("돌", "가장 기본적인 광석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        Config.unloadObject(item);

        item = new Item("석탄", "불이 잘 붙는 흔한 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(2);
        Config.unloadObject(item);

        item = new Item("석영", "약간 투명한 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        Config.unloadObject(item);

        item = new Item("구리", "붉으스름한 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        Config.unloadObject(item);

        item = new Item("납", "다양한 곳에서 사용되는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        Config.unloadObject(item);

        item = new Item("주석", "다양한 곳에서 사용되는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        Config.unloadObject(item);

        item = new Item("니켈", "다양한 곳에서 사용되는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(3);
        Config.unloadObject(item);

        item = new Item("철", "매우 많이 사용되는 일반적인 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(4);
        Config.unloadObject(item);

        item = new Item("리튬", "다양한 곳에서 사용되는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(4);
        Config.unloadObject(item);

        item = new Item("청금석", "푸른색을 띄는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(5);
        Config.unloadObject(item);

        item = new Item("레드스톤", "붉은색을 띄는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(5);
        Config.unloadObject(item);

        item = new Item("은", "회백색을 띄며 신비한 기운을 내뿜는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(6);
        Config.unloadObject(item);

        item = new Item("금", "밝은 노란색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(7);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 3);

            put(35L, 3);
            put(38L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("발광석", "스스로 빛을 내는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(7);
        Config.unloadObject(item);

        item = new Item("화염석영", "주변의 열기를 흡수하는 석영이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(8);
        Config.unloadObject(item);

        item = new Item("암흑석영", "주변의 빛을 흡수하는 석영이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(8);
        Config.unloadObject(item);

        item = new Item("명청석", "스스로 빛을 내는 마나의 기운이 담긴 푸른 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(9);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(29L, 3);
            put(33L, 5);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 10);

            put(33L, 10);
            put(44L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("명적석", "스스로 빛을 내는 힘의 기운이 담긴 붉은 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(9);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(30L, 3);
            put(33L, 5);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 10);

            put(33L, 10);
            put(44L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("백금", "주변의 어둠을 흡수하는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(32L, 5);
            put(41L, 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(0L, 5);

            put(46L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("무연탄", "일반 석탄보다 훨씬 질이 좋은 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(10);
        Config.unloadObject(item);

        item = new Item("티타늄", "꽤나 단단한 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(10);
        Config.unloadObject(item);

        item = new Item("투명석", "거의 보이지 않을 정도로 투명한 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(22L, 10);
            put(33L, 1);
            put(34L, 10);
            put(35L, 1);
            put(38L, 5);
        }}, true);
        Config.unloadObject(item);

        item = new Item("다이아몬드", "매우 단단하고 아름다운 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(40L, 10);
            put(41L, 3);
        }}, true);
        Config.unloadObject(item);

        item = new Item("오리하르콘", "일반적으로 가장 단단하다고 여겨지는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(12);
        Config.unloadObject(item);

        item = new Item("적청석", "힘과 마나, 두개의 상반된 기운을 한번에 가진 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(36L, 50);
            put(37L, 50);
            put(38L, 20);
            put(42L, 10);
        }}, true);
        Config.unloadObject(item);
        
        item = new Item("랜디움", "부서질수록 단단해자고, 스스로 복구되는 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(38L, 10);
            put(46L, 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("에이튬", "공기처럼 가벼우나 오리하르콘만큼 단단한 광물이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(38L, 10);
            put(45L, 1);
        }}, true);
        Config.unloadObject(item);
        
        item = new Item("가넷", "검붉은색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("자수정", "보라색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("아쿠아마린", "파란색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("에메랄드", "녹색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("진주", "흰색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("루비", "붉은색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("페리도트", "연두색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("사파이어", "남색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("오팔", "다양한 색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("토파즈", "주황색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        item = new Item("터키석", "청록색을 띄는 보석이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(11);
        Config.unloadObject(item);

        Config.ID_COUNT.put(Id.ITEM, Math.max(Config.ID_COUNT.get(Id.ITEM), 58));
        Logger.i("ObjectMaker", "Item making is done!");
    }

    private static void makeEquip() {

    }

    private static void makeMap() {
        MapClass map = new MapClass(ObjectList.mapList.get("0-0"));
        map.setMapType(MapType.COUNTRY);
        map.getLocation().set(0, 0, 1, 1);
        Config.unloadMap(map);

        map = new MapClass(ObjectList.mapList.get("0-1"));
        map.setMapType(MapType.SEA);
        map.getLocation().set(0, 1, 1, 1);
        Config.unloadMap(map);

        for(int y = 2; y <= 10; y++) {
            map = new MapClass(ObjectList.mapList.get("0-" + y));
            map.getLocation().setMap(0, y);
            map.setMapType(MapType.FIELD);
            map.getLocation().set(0, y, 1, 1);
            Config.unloadMap(map);
        }

        map = new MapClass(ObjectList.mapList.get("1-0"));
        map.setMapType(MapType.FIELD);
        map.getLocation().set(1, 0, 1, 1);
        Config.unloadMap(map);

        map = new MapClass(ObjectList.mapList.get("1-1"));
        map.setMapType(MapType.RIVER);
        map.getLocation().set(1, 1, 1, 1);
        Config.unloadMap(map);

        for(int x = 1; x <= 10; x++) {
            for(int y = 0; y <= 10; y++) {
                if(x == 1 && y <= 1) {
                    continue;
                }

                map = new MapClass(ObjectList.mapList.get(x + "-" + y));
                map.getLocation().setMap(x, y);
                map.setMapType(MapType.FIELD);
                map.getLocation().set(x, y, 1, 1);
                Config.unloadMap(map);
            }
        }

        Logger.i("ObjectMaker", "Map making is done!");
    }

    private static void makeChat() {
        Chat chat = new Chat();
        chat.getId().setObjectId(1L);
        chat.getText().addAll(Arrays.asList(
                "드디어 일어났네 __nickname",
                "아 이 소리가 어디서 들려오는지는 아직은 몰라도 될거야. 결국엔 알게될테니까",
                "어찌됬든 넌 여기서 성장해야만 해. 그리고 니가 나한테 했던 약속을 지켜야겠지",
                "음 뭐가됬든 일단 기본적인거부터 가르쳐줄게. " + Emoji.focus("n 도움말")
                        + " 을 입력해서 명령어를 살펴봐"
        ));
        chat.setAnyResponseChat("__도움말", 2);
        chat.setAnyResponseChat("__명령어", 2);
        chat.setAnyResponseChat("__?", 2);
        chat.setAnyResponseChat("__h", 2);
        chat.setAnyResponseChat("__help", 2);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(2L);
        chat.getText().addAll(Arrays.asList(
                "좋아, 잘 따라오고 있네",
                "일단 네 정보부터 살펴볼 필요가 있겠지",
                "명령어 목록을 보고 네 정보를 표시하는 명령어를 사용해봐",
                "아 물론 거기 적혀있기도 하지만 네가 명령어 창을 연 것 처럼," +
                        "모든 명령어에는 " + Emoji.focus("n") + "이나 " +
                        Emoji.focus("ㅜ") + "라는 접두사가 포함되니까 기억해"
        ));
        chat.setAnyResponseChat("__정보", 3);
        chat.setAnyResponseChat("__info", 3);
        chat.setAnyResponseChat("__i", 3);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(3L);
        chat.getText().addAll(Arrays.asList(
                "거기 표시된 정보는 위에서부터 [돈, 체력, 마나, 현재 위치(+좌표), 레벨," +
                        " 잔여 스텟 포인트, 모험 포인트, 거점(+좌표)] 야",
                "일단 우리는 0.0.1.1, 그러니까 가장 외곽에 있는거고, 거점도 여기로 잡혀있어",
                "거점? 아 거점은 죽으면 태어나는 장소야. 어짜피 넌 내 권능때문에 죽을 수 없거든...",
                "마지막으로 간단한거 하나만 소개하고 가봐야겠네. 일단 마을에서는 광질을 할 수 있으니까" +
                        "광질 명령어를 입력해봐"
        ));
        chat.setAnyResponseChat("__광질", 4);
        chat.setAnyResponseChat("__mine", 4);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(4L);
        chat.getText().addAll(Arrays.asList(
                "그럼 난 이만 가볼게. 어짜피 할 일도 많고 너도 이젠 혼자서 다 할 수 있을거같으니까",
                "뭐라도 주고 가라고? 골드를 조금 넣어놨으니까 그거라도 써",
                "아 그리고... ... ... (더 이상 들리지 않는다)"
        ));
        chat.getRewardMoney().set(1000L);
        Config.unloadObject(chat);
   }

    private static void makeNpc() {
        Npc npc = new Npc("???");
        npc.getId().setObjectId(1L);
        npc.getLocation().set(0, 0, 1, 1);
        npc.addChat(0, Integer.MAX_VALUE, 1L);
        npc.addChat(0, Integer.MAX_VALUE, 2L);
        npc.addChat(0, Integer.MAX_VALUE, 3L);
        npc.addChat(0, Integer.MAX_VALUE, 4L);
        Config.unloadObject(npc);

        npc = new Npc("최후의 용");
        npc.getId().setObjectId(2L);
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);
    }

}
