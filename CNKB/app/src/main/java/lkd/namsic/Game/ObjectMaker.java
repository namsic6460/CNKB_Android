package lkd.namsic.Game;

import lkd.namsic.Setting.Logger;

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

    }

    private static void makeEquip() {

    }

}
