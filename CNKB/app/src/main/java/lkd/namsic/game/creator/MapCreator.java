package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.object.MapList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class MapCreator implements Creatable {

    public void start() {
        GameMap map;

        map = new GameMap(MapList.START_VILLAGE);
        Config.unloadMap(map);

        map = new GameMap(MapList.QUITE_SEASHORE);
        map.setMapType(MapType.SEA);
        Config.unloadMap(map);

        map = new GameMap(MapList.DARK_CAVE);
        map.setMapType(MapType.CAVE);
        map.setSpawnMonster(MonsterList.SPIDER.getId(), 1, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.ENTRANCE_OF_SINKHOLE);
        map.setMapType(MapType.SINKHOLE);
        map.setRequireLv(60);
        map.setSpawnMonster(MonsterList.IMP.getId(), 1, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.DIRTY_RIVER);
        map.setMapType(MapType.CORRUPTED_RIVER);
        map.setRequireLv(100);
        map.setSpawnMonster(MonsterList.LOW_DEVIL.getId(), 1, 3);
        Config.unloadMap(map);

        map = new GameMap(MapList.ADVENTURE_FIELD);
        map.setMapType(MapType.FIELD);
        map.setSpawnMonster(MonsterList.SHEEP.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.PEACEFUL_RIVER);
        map.setMapType(MapType.RIVER);
        Config.unloadMap(map);

        map = new GameMap(MapList.SLIME_SWAMP);
        map.setMapType(MapType.SWAMP);
        map.setRequireLv(40);
        map.setSpawnMonster(MonsterList.SLIME.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.STEEP_CLIFF);
        map.setMapType(MapType.CLIFF);
        map.setRequireLv(130);
        map.setSpawnMonster(MonsterList.GARGOYLE.getId(), 1, 12);
        Config.unloadMap(map);

        map = new GameMap(MapList.DIRTY_RIVER_DOWNSTREAM);
        map.setMapType(MapType.RIVER);
        map.setRequireLv(145);
        map.setSpawnMonster(MonsterList.OWLBEAR.getId(), 1, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.FERTILE_FARM);
        map.setMapType(MapType.FIELD);
        map.setRequireLv(5);
        map.setSpawnMonster(MonsterList.PIG.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.BLUE_FIELD);
        map.setMapType(MapType.FIELD);
        map.setRequireLv(15);
        map.setSpawnMonster(MonsterList.COW.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.VILLAGE_CEMETERY);
        map.setMapType(MapType.CEMETERY);
        map.setRequireLv(20);
        map.setSpawnMonster(MonsterList.SKELETON.getId(), 1, 6);
        map.setSpawnMonster(MonsterList.ZOMBIE.getId(), 1, 3);
        Config.unloadMap(map);

        map = new GameMap(MapList.STONE_MOUNTAIN);
        map.setMapType(MapType.MOUNTAIN);
        map.setRequireLv(130);
        map.setSpawnMonster(MonsterList.GOLEM.getId(), 1, 2);
        Config.unloadMap(map);

        map = new GameMap(MapList.GLOOMY_FIELD);
        map.setMapType(MapType.FIELD);
        map.setRequireLv(50);
        map.setSpawnMonster(MonsterList.ENT.getId(), 1, 16);
        Config.unloadMap(map);

        map = new GameMap(MapList.OVERGROWN_FOREST);
        map.setMapType(MapType.FOREST);
        map.setRequireLv(60);
        map.setSpawnMonster(MonsterList.TROLL.getId(), 1, 3);
        Config.unloadMap(map);

        map = new GameMap(MapList.OAK_MOUNTAIN);
        map.setMapType(MapType.MOUNTAIN);
        map.setRequireLv(75);
        map.setSpawnMonster(MonsterList.OAK.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.SKY_HILL);
        map.setMapType(MapType.HILL);
        map.setRequireLv(115);
        map.setSpawnMonster(MonsterList.HARPY.getId(), 1, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.ELF_FOREST);
        map.getLimitQuest().put(QuestList.HEALING_ELF2.getId(), 1);
        Config.unloadMap(map);

        for (int x = 0; x <= 10; x++) {
            for (int y = 0; y <= 10; y++) {
                if (!MapList.findByLocation(x, y).equals(Config.INCOMPLETE)) {
                    continue;
                }

                map = new GameMap(MapList.INCOMPLETE);
                map.getLocation().setMap(x, y);
                map.setMapType(MapType.FIELD);
                map.getLocation().set(x, y, 1, 1);
                Config.unloadMap(map);
            }
        }

        Player player;
        for (String[] playerData : Config.PLAYER_LIST.values()) {
            player = Config.loadPlayer(playerData[0], playerData[1]);
            Config.unloadObject(player);

            map = Config.loadMap(player.getLocation());
            map.addEntity(player);

            if (!map.getSpawnMonster().isEmpty()) {
                map.respawn();
            }

            Config.unloadMap(map);
        }

        Logger.i("ObjectMaker", "Map making is done!");
    }

}
