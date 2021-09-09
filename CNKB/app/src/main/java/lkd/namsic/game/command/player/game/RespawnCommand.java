package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class RespawnCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        KakaoTalk.checkDoing(player);

        GameMap map = Config.loadMap(player.getLocation());

        try {
            if (MapType.cityList().contains(map.getMapType()) || map.getSpawnMonster().isEmpty()) {
                throw new WeirdCommandException("몬스터 리스폰이 불가능한 맵입니다");
            }

            Set<Long> monsterSet = new HashSet<>(map.getEntity(Id.MONSTER));

            Monster monster;
            for (long monsterId : monsterSet) {
                monster = Config.getData(Id.MONSTER, monsterId);

                if (Doing.fightList().contains(monster.getDoing())) {
                    throw new WeirdCommandException("전투가 진행중인 맵에서는 리스폰이 불가능합니다");
                }
            }
            
            player.replyPlayer("몬스터 리스폰을 시도합니다\n10초만 기다려주세요");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Logger.e("RespawnCommand", e);
                throw new RuntimeException(e.getMessage());
            }

            for (long monsterId : monsterSet) {
                monster = Config.getData(Id.MONSTER, monsterId);

                if (Doing.fightList().contains(monster.getDoing())) {
                    throw new WeirdCommandException("전투가 진행중이어서 몬스터 리스폰에 실패했습니다");
                }
            }

            IdClass idClass;
            for (long monsterId : monsterSet) {
                idClass = new IdClass(Id.MONSTER, monsterId);

                map.removeEntity(idClass);
                Config.deleteGameObject(idClass);
            }

            map.respawn();
            player.replyPlayer("몬스터가 리스폰 되었습니다");
        } finally {
            Config.unloadMap(map);
        }
    }

}
