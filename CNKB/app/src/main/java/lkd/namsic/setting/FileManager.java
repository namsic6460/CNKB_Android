package lkd.namsic.setting;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;

public class FileManager extends Application {

    private final static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CNKB";
    public final static String CONFIG_PATH = PATH + "/config.json";
    public final static String LOG_PATH = PATH + "/logs";
    public final static String DATA_PATH = PATH + "/data";
    public final static String MAP_PATH = DATA_PATH + "/map";
    public final static HashMap<Id, String> DATA_PATH_MAP = new HashMap<>();

    public static void initDir() {
        try {
            File file = new File(PATH);
            file.mkdir();

            new File(LOG_PATH).mkdir();
            new File(DATA_PATH).mkdir();

            String path;
            for (Id id : Id.values()) {
                path = DATA_PATH + "/" + id.toString();
                DATA_PATH_MAP.put(id, path + "/");
                new File(path).mkdir();
            }

            new File(MAP_PATH).mkdir();

            String config = read(CONFIG_PATH);
            if (config.equals("")) {
                JSONObject configObject = Config.createConfig();
                save(CONFIG_PATH, configObject.toString());
            } else {
                JSONObject jsonObject = new JSONObject(config);
                Config.parseConfig(jsonObject);
            }

            Logger.i("FileManager", "Init complete");
            Logger.logCount = 0;
            Logger.logs = "";
        } catch (Exception e) {
            Logger.e("FileManager", e);
        }
    }

    @NonNull
    public static String read(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return "";
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            char[] c = new char[(int) file.length()];
            bufferedReader.read(c);
            bufferedReader.close();
            Logger.i("FileManger", "read - " + path);

            return new String(c);
        } catch (Exception e) {
            Logger.e("FileManager", e);
            return "";
        }
    }

    public static void append(String path, @NonNull String data) {
        String readData = read(path);
        if (!readData.equals("")) {
            data = readData + "\n" + data;
        }

        save(path, data);
    }

    public static void save(String path, @NonNull String data) {
        try {
            File file = new File(path);
            if(!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(data);
            bufferedWriter.close();
            Logger.i("FileManger", "save - " + path + "\n" + data);
        } catch (Exception e) {
            Logger.e("FileManager(path : " + path + ")", e);
        }
    }

    public static void delete(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    Logger.i("FileManager", "delete - " + path);
                } else {
                    throw new Exception("Failed to delete file - " + path);
                }
            } else {
                Logger.w("FileManager", "file does not exist - " + path);
            }
        } catch (Exception e) {
            Logger.e("FileManager", e);
        }
    }

}