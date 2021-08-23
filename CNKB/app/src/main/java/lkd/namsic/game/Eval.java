package lkd.namsic.game;

import androidx.annotation.NonNull;

import com.faendir.rhino_android.RhinoAndroidHelper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import lkd.namsic.MainActivity;
import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
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
import lkd.namsic.game.object.Achieve;
import lkd.namsic.game.object.AiEntity;
import lkd.namsic.game.object.Boss;
import lkd.namsic.game.object.Chat;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Farm;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.GameObject;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.NamedObject;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Quest;
import lkd.namsic.game.object.Research;
import lkd.namsic.game.object.Shop;
import lkd.namsic.game.object.Skill;

public class Eval {

    private static final RhinoAndroidHelper helper = new RhinoAndroidHelper(MainActivity.mainActivity);

    @NonNull
    public static String execute(@NonNull String command, @NonNull Player self) {
        try {
            Context context = helper.enterContext();
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();

            ScriptableObject.putProperty(scope, "Config", Config.class);
            ScriptableObject.putProperty(scope, "RandomList", RandomList.class);
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

            ScriptableObject.putProperty(scope, "Achieve", Achieve.class);
            ScriptableObject.putProperty(scope, "AiEntity", AiEntity.class);
            ScriptableObject.putProperty(scope, "Boss", Boss.class);
            ScriptableObject.putProperty(scope, "Chat", Chat.class);
            ScriptableObject.putProperty(scope, "Entity", Entity.class);
            ScriptableObject.putProperty(scope, "Equipment", Equipment.class);
            ScriptableObject.putProperty(scope, "Farm", Farm.class);
            ScriptableObject.putProperty(scope, "GameObject", GameObject.class);
            ScriptableObject.putProperty(scope, "Item", Item.class);
            ScriptableObject.putProperty(scope, "GameMap", GameMap.class);
            ScriptableObject.putProperty(scope, "Monster", Monster.class);
            ScriptableObject.putProperty(scope, "NamedObject", NamedObject.class);
            ScriptableObject.putProperty(scope, "Npc", Npc.class);
            ScriptableObject.putProperty(scope, "Player", Player.class);
            ScriptableObject.putProperty(scope, "Quest", Quest.class);
            ScriptableObject.putProperty(scope, "Research", Research.class);
            ScriptableObject.putProperty(scope, "Shop", Shop.class);
            ScriptableObject.putProperty(scope, "Skill", Skill.class);
            ScriptableObject.putProperty(scope, "Use", Use.class);

            ScriptableObject.putProperty(scope, "KakaoTalk", KakaoTalk.class);

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
            return "[Error]\n" + Config.errorString(e);
        }
    }

}
