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
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DeathEvent;
import lkd.namsic.game.event.ItemEatEvent;
import lkd.namsic.game.event.ItemUseEvent;
import lkd.namsic.game.event.MoneyChangeEvent;
import lkd.namsic.game.event.MoveEvent;
import lkd.namsic.game.gameObject.Achieve;
import lkd.namsic.game.gameObject.AiEntity;
import lkd.namsic.game.gameObject.Boss;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Equipment;
import lkd.namsic.game.gameObject.GameObject;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.NamedObject;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.gameObject.Quest;
import lkd.namsic.game.gameObject.Research;
import lkd.namsic.game.gameObject.Skill;
import lkd.namsic.game.base.Use;

public class Eval {

    private static final RhinoAndroidHelper helper = new RhinoAndroidHelper(MainActivity.mainActivity);

    @NonNull
    public static String execute(@NonNull String command, @NonNull Player self) {
        try {
            Context context = helper.enterContext();
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();

            ScriptableObject.putProperty(scope, "Config", Config.class);
            ScriptableObject.putProperty(scope, "System", System.class);

            ScriptableObject.putProperty(scope, "int", int.class);
            ScriptableObject.putProperty(scope, "boolean", boolean.class);
            ScriptableObject.putProperty(scope, "double", double.class);
            ScriptableObject.putProperty(scope, "long", long.class);
            ScriptableObject.putProperty(scope, "Integer", Integer.class);
            ScriptableObject.putProperty(scope, "Boolean", Boolean.class);
            ScriptableObject.putProperty(scope, "Double", Double.class);
            ScriptableObject.putProperty(scope, "Long", Long.class);
            ScriptableObject.putProperty(scope, "String", String.class);

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
            ScriptableObject.putProperty(scope, "Variable", Variable.class);
            ScriptableObject.putProperty(scope, "WaitResponse", WaitResponse.class);

            ScriptableObject.putProperty(scope, "DamageEvent", DamageEvent.class);
            ScriptableObject.putProperty(scope, "DeathEvent", DeathEvent.class);
            ScriptableObject.putProperty(scope, "ItemEatEvent", ItemEatEvent.class);
            ScriptableObject.putProperty(scope, "ItemUseEvent", ItemUseEvent.class);
            ScriptableObject.putProperty(scope, "MoneyChangeEvent", MoneyChangeEvent.class);
            ScriptableObject.putProperty(scope, "MoveEvent", MoveEvent.class);

            ScriptableObject.putProperty(scope, "Achieve", Achieve.class);
            ScriptableObject.putProperty(scope, "AiEntity", AiEntity.class);
            ScriptableObject.putProperty(scope, "Boss", Boss.class);
            ScriptableObject.putProperty(scope, "Chat", Chat.class);
            ScriptableObject.putProperty(scope, "Entity", Entity.class);
            ScriptableObject.putProperty(scope, "Equipment", Equipment.class);
            ScriptableObject.putProperty(scope, "GameObject", GameObject.class);
            ScriptableObject.putProperty(scope, "Item", Item.class);
            ScriptableObject.putProperty(scope, "MapClass", GameMap.class);
            ScriptableObject.putProperty(scope, "Monster", Monster.class);
            ScriptableObject.putProperty(scope, "NamedObject", NamedObject.class);
            ScriptableObject.putProperty(scope, "Npc", Npc.class);
            ScriptableObject.putProperty(scope, "Player", Player.class);
            ScriptableObject.putProperty(scope, "Quest", Quest.class);
            ScriptableObject.putProperty(scope, "Research", Research.class);
            ScriptableObject.putProperty(scope, "Skill", Skill.class);
            ScriptableObject.putProperty(scope, "Use", Use.class);

            ScriptableObject.putProperty(scope, "self", self);

            String[] evalCommands = command.split("\n");

            Object result = null;
            for (String evalCommand : evalCommands) {
                evalCommand = evalCommand.replaceAll("toInt\\(",
                        "Double.getMethod(\"intValue\").invoke(Double.getDeclaredMethod(\"valueOf\", String).invoke(null, ");

                result = context.evaluateString(scope, "eval (" + evalCommand + ");", null, 1, null);
            }

            return Context.toString(result);
        } catch (Exception e) {
            Log.e("namsic!", Config.errorString(e));
            return "[Error]\n" + Config.errorString(e);
        }
    }

}
