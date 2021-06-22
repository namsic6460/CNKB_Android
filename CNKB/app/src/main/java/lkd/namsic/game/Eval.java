package lkd.namsic.game;

import android.util.Log;

import androidx.annotation.NonNull;

import com.faendir.rhino_android.RhinoAndroidHelper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import lkd.namsic.MainActivity;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.FishWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.gameObject.GameObject;
import lkd.namsic.game.gameObject.Player;

public class Eval {

    private static final RhinoAndroidHelper helper = new RhinoAndroidHelper(MainActivity.mainActivity);

    @NonNull
    public static String execute(@NonNull String command, @NonNull Player self) {
        try {
            Context context = helper.enterContext();
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();

            ScriptableObject.putProperty(scope, "Config", Config.class);
            ScriptableObject.putProperty(scope, "GameObject", GameObject.class);

            ScriptableObject.putProperty(scope, "Doing", Doing.class);
            ScriptableObject.putProperty(scope, "EquipType", EquipType.class);
            ScriptableObject.putProperty(scope, "FightWaitType", FightWaitType.class);
            ScriptableObject.putProperty(scope, "FishWaitType", FishWaitType.class);
            ScriptableObject.putProperty(scope, "Id", Id.class);
            ScriptableObject.putProperty(scope, "LogData", LogData.class);
            ScriptableObject.putProperty(scope, "MagicType", MagicType.class);
            ScriptableObject.putProperty(scope, "MapType", MapType.class);
            ScriptableObject.putProperty(scope, "MonsterType", MonsterType.class);
            ScriptableObject.putProperty(scope, "StatType", StatType.class);
            ScriptableObject.putProperty(scope, "WaitResponse", WaitResponse.class);

            ScriptableObject.putProperty(scope, "self", self);

            String[] evalCommands = command.split("\n");

            Object result = null;
            for (String evalCommand : evalCommands) {
                result = context.evaluateString(scope, "eval (" + evalCommand + ");", null, 1, null);
            }

            return Context.toString(result);
        } catch (Exception e) {
            Log.e("namsic!", Config.errorString(e));
            return "[Error]\n" + Config.errorString(e);
        }
    }

}
