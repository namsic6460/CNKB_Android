package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.object.Player;

public class PercentManager {

    private static final PercentManager instance = new PercentManager();

    public static PercentManager getInstance() {
        return instance;
    }

    public void displayMinePercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===광질 확률표===");

        for(int mineLv = 0; mineLv <= Config.MAX_MINE_LV; mineLv++) {
            innerBuilder.append("\n\n---")
                    .append(mineLv)
                    .append(" 레벨 확률표---");

            List<Double> minePercent = RandomList.MINE_PERCENT.get(mineLv);

            double percent;
            StringBuilder tempBuilder;
            for(int index = 0; index < minePercent.size(); index++) {
                percent = minePercent.get(index);

                if(percent == 0) {
                    continue;
                }

                innerBuilder.append("\n[");

                tempBuilder = new StringBuilder();
                for(long itemId : RandomList.MINE_OUTPUT.get(index)) {
                    tempBuilder.append(ItemList.findById(itemId))
                            .append(", ");
                }

                innerBuilder.append(tempBuilder.substring(0, tempBuilder.length() - 2))
                        .append("]: ")
                        .append(Config.getDisplayPercent(percent / 100, 4));
            }
        }

        self.replyPlayer("광질 레벨: " + self.getDisplayMineLv() +
                "\n\n광질 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayFishPercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===낚시 확률표===");

        for(int fishLv = 0; fishLv <= Config.MAX_FISH_LV; fishLv++) {
            innerBuilder.append("\n\n---")
                    .append(fishLv)
                    .append(" 레벨 확률---");

            List<Integer> fishPercent = RandomList.FISH_PERCENT.get(fishLv);
            List<String> outputStr = Arrays.asList(
                    "돌/나뭇가지/쓰레기/물풀",
                    "일반 물고기",
                    "희귀 물고기",
                    "특별 물고기",
                    "유일 물고기",
                    "전설 물고기",
                    "신화 물고기"
            );

            int percent;
            for(int index = 0; index < fishPercent.size(); index++) {
                percent = fishPercent.get(index);

                if(percent == 0) {
                    continue;
                }

                innerBuilder.append("\n")
                        .append(outputStr.get(index))
                        .append(": ")
                        .append(percent)
                        .append(".0%");
            }
        }

        self.replyPlayer("낚시 레벨: " + self.getDisplayFishLv() +
                "\n\n낚시 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayAdvPercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===모험 보상 확률표===");

        for(Map.Entry<Integer, Map<Long, Integer>> entry : RandomList.ADVENTURE_LIST.entrySet()) {
            innerBuilder.append("\n\n---")
                    .append(entry.getKey())
                    .append(" 등급 보상---");

            for(Map.Entry<Long, Integer> percentEntry : entry.getValue().entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(percentEntry.getKey()))
                        .append(": ")
                        .append(Config.getDisplayPercent(percentEntry.getValue() / 1_000_000D, 4));
            }
        }

        self.replyPlayer("모험 스텟: " + self.getAdv() +
                "\n\n모험 보상 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
    }

}
