package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.object.Player;

public class RankingManager {

    private static final RankingManager instance = new RankingManager();

    public static RankingManager getInstance() {
        return instance;
    }

    public void displayLvRanking(@NonNull Player self) {
        List<String> sortedList = new ArrayList<>(Config.LV_RANK.keySet());
        sortedList.sort((o1, o2) -> Integer.compare(Config.LV_RANK.get(o2), Config.LV_RANK.get(o1)));

        StringBuilder innerBuilder = new StringBuilder(Config.SPLIT_BAR);

        int rank = 1;
        for (String name : sortedList) {
            innerBuilder.append("\n")
                    .append(rank++)
                    .append(". ")
                    .append(name)
                    .append("\n")
                    .append(Config.SPLIT_BAR);
        }

        self.replyPlayer("레벨 랭킹은 전체보기로 확인해주세요", innerBuilder.toString());
    }
    
    public void displayTowerRanking(@NonNull Player self) {
        List<Long> sortedList = new ArrayList<>(Config.TOWER_RANK.keySet());
        sortedList.sort((o1, o2) -> Integer.compare(Config.TOWER_RANK.get(o2), Config.TOWER_RANK.get(o1)));

        StringBuilder innerBuilder = new StringBuilder(Config.SPLIT_BAR);

        int rank = 1;
        Player player;
        for (long playerId : sortedList) {
            player = Config.getData(Id.PLAYER, playerId);

            innerBuilder.append("\n")
                    .append(rank++)
                    .append(". ")
                    .append(player.getName())
                    .append("\n")
                    .append(Config.SPLIT_BAR);
        }

        self.replyPlayer("탑 랭킹은 전체보기로 확인해주세요", innerBuilder.toString());
    }

}
