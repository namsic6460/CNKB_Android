package lkd.namsic.game.initializer;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.object.MapList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class MapCreator implements ObjectCreator {

    public void start() {
        GameMap map;

        map = new GameMap(MapList.findByLocation(0, 0));
        map.getLocation().set(0, 0, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(0, 1));
        map.setMapType(MapType.SEA);
        map.getLocation().set(0, 1, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(0, 2));
        map.setMapType(MapType.CAVE);
        map.getLocation().set(0, 2, 1, 1);
        map.setSpawnMonster(MonsterList.SPIDER.getId(), 1D, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 0));
        map.setMapType(MapType.FIELD);
        map.getLocation().set(1, 0, 1, 1);
        map.setSpawnMonster(MonsterList.SHEEP.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 1));
        map.setMapType(MapType.RIVER);
        map.getLocation().set(1, 1, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 2));
        map.setMapType(MapType.SWAMP);
        map.getRequireLv().set(40);
        map.getLocation().set(1, 2, 1, 1);
        map.setSpawnMonster(MonsterList.SLIME.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 0));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(5);
        map.getLocation().set(2, 0, 1, 1);
        map.setSpawnMonster(MonsterList.PIG.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 1));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(15);
        map.getLocation().set(2, 1, 1, 1);
        map.setSpawnMonster(MonsterList.COW.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 2));
        map.setMapType(MapType.CEMETERY);
        map.getRequireLv().set(20);
        map.getLocation().set(2, 2, 1, 1);
        map.setSpawnMonster(MonsterList.ZOMBIE.getId(), 1D, 3);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(3, 0));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(50);
        map.getLocation().set(3, 0, 1, 1);
        map.setSpawnMonster(MonsterList.ENT.getId(), 1D, 16);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(3, 1));
        map.setMapType(MapType.FOREST);
        map.getRequireLv().set(60);
        map.getLocation().set(3, 1, 1, 1);
        map.setSpawnMonster(MonsterList.TROLL.getId(), 1D, 3);
        Config.unloadMap(map);

        for(int x = 0; x <= 10; x++) {
            for(int y = 0; y <= 10; y++) {
                if(!MapList.findByLocation(x, y).equals(Config.INCOMPLETE)) {
                    continue;
                }

                map = new GameMap(Config.INCOMPLETE);
                map.getLocation().setMap(x, y);
                map.setMapType(MapType.FIELD);
                map.getLocation().set(x, y, 1, 1);
                Config.unloadMap(map);
            }
        }

        Player player;
        for(String[] playerData : Config.PLAYER_LIST.values()) {
            player = Config.loadPlayer(playerData[0], playerData[1]);
            Config.unloadObject(player);
            map = Config.loadMap(player.getLocation());
            map.addEntity(player);
            Config.unloadMap(map);
        }

        Logger.i("ObjectMaker", "Map making is done!");
    }

}
