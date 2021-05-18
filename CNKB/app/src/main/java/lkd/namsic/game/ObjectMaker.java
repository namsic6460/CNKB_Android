package lkd.namsic.game;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import lkd.namsic.MainActivity;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;
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
                makeQuest();
                makeNpc();

                Logger.i("ObjectMaker", "Object making is done!");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("namsic!", Config.errorString(e));
                Logger.e("ObjectMaker", e);
                System.exit(-1);
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
        
        item = new Item("쓰레기", "누가 이런걸 물에 버렸어?");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("물풀", "물에서 자라는 다양한 풀이다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Common) 금강모치", "평범 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Common) 미꾸라지", "평범 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Common) 붕어", "평범 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Common) 송사리", "평범 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Common) 피라미", "평범 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 망둑어", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 미꾸라지", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 배스", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 살치", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 쏘가리", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(UnCommon) 은어", "특별 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 강준치", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 망둑어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 메기", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 뱀장어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 산천어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 숭어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 쏘가리", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 연어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 은어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Rare) 잉어", "희귀 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 강준치", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 메기", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 뱀장어", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 산천어", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 숭어", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 연어", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Unique) 잉어", "유일 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Legendary) 다금바리", "전설 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Legendary) 돗돔", "전설 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Legendary) 자치", "전설 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Legendary) 쿠니마스", "전설 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Myth) 실러캔스", "신화 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        item = new Item("(Myth) 폐어", "신화 등급의 물고기다");
        item.getId().setObjectId(ObjectList.itemList.get(item.getName()));
        item.getHandleLv().set(1);
        Config.unloadObject(item);

        Config.ID_COUNT.put(Id.ITEM, Math.max(Config.ID_COUNT.get(Id.ITEM), 94L));
        Logger.i("ObjectMaker", "Item making is done!");
    }

    private static void makeEquip() {
        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 1L));
        Logger.i("ObjectMaker", "Equipment making is done!");
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
                "아 이 소리가 어디서 들려오는지는 아직은 몰라도 될거야. 결국엔 알게 될테니까",
                "어찌됬든 넌 여기서 성장해야만 해. 그리고 니가 나한테 했던 약속을 지켜야겠지",
                "음 뭐가됬든 일단 기본적인거부터 가르쳐줄게. " + Emoji.focus("n 도움말")
                        + " 을 입력해서 명령어를 살펴봐"
        ));
        chat.setAnyResponseChat("__도움말", 2, true);
        chat.setAnyResponseChat("__명령어", 2, true);
        chat.setAnyResponseChat("__?", 2, true);
        chat.setAnyResponseChat("__h", 2, true);
        chat.setAnyResponseChat("__help", 2, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(2L);
        chat.getText().addAll(Arrays.asList(
                "좋아, 잘 따라오고 있네",
                "일단 네 정보부터 살펴볼 필요가 있겠지",
                "명령어 목록을 보고 네 정보를 표시하는 명령어를 사용해봐",
                "아 물론 거기 적혀있기도 하지만 네가 명령어 창을 연 것 처럼, " +
                        "모든 명령어에는 " + Emoji.focus("n") + "이나 " +
                        Emoji.focus("ㅜ") + "라는 글자가 붙으니까 기억해"
        ));
        chat.setAnyResponseChat("__정보", 3, true);
        chat.setAnyResponseChat("__info", 3, true);
        chat.setAnyResponseChat("__i", 3, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(3L);
        chat.getText().addAll(Arrays.asList(
                "거기 표시된 정보는 위에서부터 [돈, 체력, 마나, 현재 위치(좌표), 레벨," +
                        " 잔여 스텟 포인트, 모험 포인트, 거점(좌표)] 야",
                "일단 우리는 0-0-1-1, 그러니까 가장 외곽에 있는거고, 거점도 여기로 잡혀있어",
                "거점? 아 거점은 죽으면 태어나는 장소야. 어짜피 넌 내 권능때문에 죽을 수 없거든...",
                "마지막으로 간단한거 하나만 소개하고 가봐야겠네. 일단 마을에서는 광질을 할 수 있으니까" +
                        " 광질 명령어를 입력해봐"
        ));
        chat.setAnyResponseChat("__광질", 4, true);
        chat.setAnyResponseChat("__mine", 4, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(4L);
        chat.getText().addAll(Arrays.asList(
                "그럼 난 이만 가볼게. 어짜피 할 일도 많고 너도 이젠 혼자서 다 할 수 있을거 같으니까",
                "뭐라도 주고 가라고? 골드를 조금 넣어놨으니까 그거라도 써",
                "아 그리고... ... ... (더 이상 들리지 않는다)"
        ));
        chat.getDelayTime().set(1500L);
        chat.getMoney().set(1000L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(5L);
        chat.getText().addAll(Arrays.asList(
                "음? 못보던 얼굴이군",
                "그래 이름이 __nickname 이라고?",
                "난 이 '시작의 마을' 의 이장이라네. 잘 지네봄세"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(6L);
        chat.getText().add("그래 무슨 일인가 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("아무일도 아닙니다");
        chat.getId().setObjectId(7L);
        chat.getText().add("그래그래, 무슨 일 있으면 언제나 말 걸게나");
        Config.unloadObject(chat);

        chat = new Chat("광질을 하면서 할만한 퀘스트가 있을까요?");
        chat.getId().setObjectId(8L);
        chat.getText().addAll(Arrays.asList(
                "음, 아무래도 돌은 항상 부족해서 말이지",
                "돌 50개만 구해다 줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 10, true);
        chat.setResponseChat(WaitResponse.NO, 11, true);
        Config.unloadObject(chat);

        chat = new Chat("낚시를 하면서 할만한 퀘스트가 있을까요?");
        chat.getId().setObjectId(9L);
        chat.getText().addAll(Arrays.asList(
                "요즘 바다나 강이 너무 더러워저셔 말일세",
                "쓰레기가 낚이는게 있으면 10개만 구해다 줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 12, true);
        chat.setResponseChat(WaitResponse.NO, 11, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(10L);
        chat.getText().add("고맙네! 돌을 다 구하고 다시 말을 걸어주게나");
        chat.getQuestId().set(1L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(11L);
        chat.getText().add("그럼 어쩔 수 없지. 나중에 마음 바뀌면 다시 받아가게나");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(12L);
        chat.getText().add("고맙네! 쓰레기를 다 수거하고 다시 말을 걸어주게나");
        chat.getQuestId().set(2L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(13L);
        chat.getText().add("빨리 구해와줬군! 여기 보상이네");
        Config.unloadObject(chat);

        Config.ID_COUNT.put(Id.CHAT, Math.max(Config.ID_COUNT.get(Id.CHAT), 14L));
        Logger.i("ObjectMaker", "Chat making is done!");
   }

   private static void makeQuest() {
       Quest quest = new Quest("광부의 일", 3L, 13L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(20L, 50);
       quest.setRewardCloseRate(3L, 1, true);
       quest.getRewardExp().set(20000L);
       quest.getRewardMoney().set(250L);
       Config.unloadObject(quest);

       quest = new Quest("쓰레기 수거", 3L, 13L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(58L, 10);
       quest.setRewardCloseRate(3L, 1, true);
       quest.getRewardExp().set(50000L);
       Config.unloadObject(quest);

       Config.ID_COUNT.put(Id.QUEST, Math.max(Config.ID_COUNT.get(Id.CHAT), 3L));
       Logger.i("ObjectMaker", "Quest making is done!");
   }

    private static void makeNpc() {
        Npc npc = new Npc("???");
        npc.getId().setObjectId(1L);
        npc.getLv().set(Config.MAX_LV);
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);

        npc = new Npc("아벨");
        npc.getId().setObjectId(2L);
        npc.getLv().set(Config.MAX_LV);
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);
        
        npc = new Npc("노아");
        npc.getId().setObjectId(3L);
        npc.getLocation().set(0, 0, 16, 16);
        npc.setFirstChat(5L);
        npc.setCommonChat(new ChatLimit(), 6L);
        npc.setChat(new ChatLimit(), 7L);

        ChatLimit chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(1L);
        npc.setChat(chatLimit, 8L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(2L);
        npc.setChat(chatLimit, 9L);

        Config.unloadObject(npc);

        Config.ID_COUNT.put(Id.NPC, Math.max(Config.ID_COUNT.get(Id.NPC), 4L));
        Logger.i("ObjectMaker", "Npc making is done!");
    }

}
