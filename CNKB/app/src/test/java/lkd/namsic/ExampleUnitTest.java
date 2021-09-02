package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Random;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.command.player.debug.SetStatCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.FileManager;

@SuppressWarnings("SpellCheckingInspection")
public class ExampleUnitTest {

    @Test
    public void evalTest() {
        try {
            System.out.println(FightManager.class.getDeclaredMethod("getInstance").invoke(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        System.out.println(SetStatCommand.class.getPackage().getName());
    }

    @Test
    public void loadTest() throws Exception {
        File[] files = Objects.requireNonNull(new File("C:\\Users\\user\\Downloads\\players").listFiles());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Equipment.class, new TestEquipAdapter())
                .registerTypeAdapter(Player.class, new TestPlayerAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();

        for(File file : files) {
//        File file = new File("C:\\Users\\user\\Downloads\\CNKB\\pl\\0w0-QGiQrMIAkQJDLQQOf4-oV7b9XZP-59-jUYi0CDlOfw8Nvr1DXV+-1955777633+18829.json");
            String jsonStr = FileManager.read(file);

            System.out.println(file.getAbsolutePath());
            Player player = gson.fromJson(jsonStr, Player.class);
//            System.out.println(gson.toJson(player));
            FileManager.save(file.getAbsolutePath(), gson.toJson(player));
        }

        System.out.println("DONE");
    }

    static class TestEquipAdapter implements JsonDeserializer<Equipment> {

        @Override
        public Equipment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonObject tempObject = jsonObject.remove("handleLv").getAsJsonObject();
            jsonObject.addProperty("handleLv", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("limitLv").getAsJsonObject();
            jsonObject.addProperty("limitLv", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("reinforceCount").getAsJsonObject();
            jsonObject.addProperty("reinforceCount", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("reinforceFloor1").getAsJsonObject();
            jsonObject.addProperty("reinforceFloor1", tempObject.get("value").getAsDouble());

            tempObject = jsonObject.remove("reinforceFloor2").getAsJsonObject();
            jsonObject.addProperty("reinforceFloor2", tempObject.get("value").getAsInt());

            return new Gson().fromJson(jsonObject, Equipment.class);
        }

    }

    static class TestPlayerAdapter implements JsonDeserializer<Player> {

        @Override
        public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonObject tempObject = jsonObject.remove("adv").getAsJsonObject();
            jsonObject.addProperty("adv", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("exp").getAsJsonObject();
            jsonObject.addProperty("exp", tempObject.get("value").getAsLong());

            tempObject = jsonObject.remove("sp").getAsJsonObject();
            jsonObject.addProperty("sp", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("lv").getAsJsonObject();
            jsonObject.addProperty("lv", tempObject.get("value").getAsInt());

            tempObject = jsonObject.remove("money").getAsJsonObject();
            jsonObject.addProperty("money", tempObject.get("value").getAsLong());

            return new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .create()
                    .fromJson(jsonObject, Player.class);
        }

    }

    @Test
    public void reinforceTest() {
        int total = 0;

        Equipment equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1, null, null);
        equipment.setHandleLv(6);

        Random random = new Random();
        double basePercent;
        double percent;
        for (int i = 0; i < Config.MAX_REINFORCE_COUNT; i++) {
            basePercent = equipment.getReinforcePercent(1);

            for (int j = 1; ; j++) {
                total++;
                percent = equipment.getReinforcePercent(1);

                if (random.nextDouble() < percent) {
                    equipment.successReinforce();
                    System.out.println(i + "(" + Config.getDisplayPercent(basePercent) + "): " + j);

                    break;
                } else {
                    equipment.failReinforce(percent);
                }
            }
        }

        System.out.println(total);
    }

}