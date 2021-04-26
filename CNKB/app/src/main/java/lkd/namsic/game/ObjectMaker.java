package lkd.namsic.game;

import java.util.HashMap;
import java.util.Random;

import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class ObjectMaker {

    public static void start() {
        Logger.i("ObjectMaker", "Start!");

        try {
            makeItem();
            makeEquip();

            Logger.i("ObjectMaker", "Done!");
        } catch (Exception e) {
            Logger.e("ObjectMaker", e);
        }
    }

    private static void makeItem() {
        Item item = new Item("돌멩이", "평범한 돌멩이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("나뭇가지", "평범한 나뭇가지다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("나뭇잎", "평범한 나뭇잎이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("잡초", "평범한 잡초다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("골드 주머니", "골드가 소량 들어간 주머니다");
        item.setUse((user, other) -> {
            int money = new Random().nextInt(10 * user.getLv().get()) + 50;
            
            user.addMoney(money);
            if(user instanceof Player) {
                ((Player) user).replyPlayer("골드 주머니를 사용하여 " + money + "G 를 획득했습니다");
            }
        });
        Config.unloadObject(Config.newObject(item));

        item = new Item("골드 보따리", "골드가 들어간 주머니다");
        item.setUse((user, other) -> {
            int money = new Random().nextInt(200 * user.getLv().get()) + 1000;

            user.addMoney(money);
            if(user instanceof Player) {
                ((Player) user).replyPlayer("골드 보따리를 사용하여 " + money + "G 를 획득했습니다");
            }
        });
        Config.unloadObject(Config.newObject(item));

        item = new Item("약초", "약의 기운이 있는 풀이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("마나 조각", "마나의 기운이 있는 고체 마나 파편이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("유리조각", "유리 파편이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("흙", "땅에서 흔히 볼 수 있는 평번한 흙이다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("모래", "땅에서 흔히 볼 수 있는 평번한 모래다");
        Config.unloadObject(Config.newObject(item));

        item = new Item("유리", "투명한 고체 물질이다");
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(11L, 3);
            put(21L, 1);
        }}, true);
        Config.unloadObject(Config.newObject(item));

        item = new Item("유리병", "유리를 가공하여 만든 병이다");
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(12L, 2);
            put(21L, 1);
        }}, true);
        Config.unloadObject(Config.newObject(item));

        
    }

    private static void makeEquip() {

    }

}
