package lkd.namsic.game.initializer;

import android.widget.Button;

import androidx.annotation.NonNull;

import lkd.namsic.MainActivity;
import lkd.namsic.game.config.Config;
import lkd.namsic.setting.Logger;

public class Initializer {

    //TODO : Use, EquipUse, Event 등등은 직렬화 대신 코드 내에 저장해두고 직접 호출하는 방식으로 변경
    //TODO : 좀비 영혼 드롭 테스트

    public static void start(@NonNull Button button) {
        Logger.i("ObjectMaker", "Making objects...");

        Thread thread = new Thread(() -> {
            MainActivity.mainActivity.runOnUiThread(() -> button.setEnabled(false));

            try {
                Config.IGNORE_FILE_LOG = true;

                new ItemCreator().start();
                new EquipCreator().start();
                new MonsterCreator().start();
                new MapCreator().start();
                new ChatCreator().start();
                new QuestCreator().start();
                new NpcCreator().start();

                Config.IGNORE_FILE_LOG = false;
                Logger.i("ObjectMaker", "Object making is done!");

                Config.save();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("ObjectMaker", e);
            } finally {
                MainActivity.mainActivity.runOnUiThread(() -> button.setEnabled(true));
            }
        });

        MainActivity.startThread(thread);
    }

}
