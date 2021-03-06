package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.MineEvent;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class MineManager {

    private static final MineManager instance = new MineManager();

    public static MineManager getInstance() {
        return instance;
    }

    public boolean canMine(@NonNull Player self) {
        return Config.getMapData(self.getLocation()).getMapType().equals(MapType.COUNTRY);
    }

    public void mine(@NonNull Player self, int mineLv) {
        self.setDoing(Doing.MINE);
        self.addLog(LogData.MINED, 1);

        double randomPercent = Math.random() * 100;

        List<Double> percents = new ArrayList<>();
        for(List<Double> minePercent : RandomList.MINE_PERCENT) {
            percents.add(minePercent.get(mineLv));
        }

        List<Long> output = Arrays.asList(ItemList.STONE.getId(), ItemList.COAL.getId(),
                ItemList.NONE.getId(), ItemList.NONE.getId(), ItemList.NONE.getId(), ItemList.SILVER.getId(),
                ItemList.GLOW_STONE.getId(), ItemList.NONE.getId(), ItemList.NONE.getId(), ItemList.NONE.getId(),
                ItemList.ORICHALCON.getId(), ItemList.NONE.getId());

        double percent;
        long itemId = ItemList.NONE.getId();

        //0 ~ 11
        int itemTier = 0;
        for(; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                itemId = output.get(itemTier);

                if(itemId == ItemList.NONE.getId()) {
                    long[] itemList;

                    switch(itemTier) {
                        case 2:
                            itemList = new long[]{ItemList.COPPER.getId(), ItemList.LEAD.getId(),
                                    ItemList.TIN.getId(), ItemList.NICKEL.getId()};
                            break;
                        case 3:
                            itemList = new long[]{ItemList.IRON.getId(), ItemList.LITHIUM.getId()};
                            break;
                        case 4:
                            itemList = new long[]{ItemList.LAPIS.getId(), ItemList.RED_STONE.getId()};
                            break;
                        case 7:
                            itemList = new long[]{ItemList.FIRE_QUARTZ.getId(), ItemList.DARK_QUARTZ.getId()};
                            break;
                        case 8:
                            itemList = new long[]{ItemList.GLOW_LAPIS.getId(), ItemList.GLOW_RED_STONE.getId()};
                            break;
                        case 9:
                            itemList = new long[]{ItemList.HARD_COAL.getId(), ItemList.TITANIUM.getId()};
                            break;
                        case 11:
                            itemList = new long[]{ItemList.LAPIS_RED_STONE.getId(), ItemList.LANDIUM.getId(),
                                    ItemList.AITUME.getId()};
                            break;
                        default:
                            throw new InvalidNumberException(itemTier);
                    }

                    itemId = itemList[new Random().nextInt(itemList.length)];
                }

                break;
            } else {
                randomPercent -= percent;
            }
        }

        if(itemId == ItemList.NONE.getId()) {
            itemId = ItemList.STONE.getId();
        }

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Logger.e("MineManager", e);
            throw new RuntimeException(e.getMessage());
        }

        Int mineCount = new Int(1);

        String eventName = MineEvent.getName();
        MineEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName), itemId, mineCount);

        int count = self.getItem(itemId);
        self.addItem(itemId, mineCount.get(), false);
        self.addExp(15 * (itemTier + 1));

        StringBuilder builder = new StringBuilder();

        Item item = Config.getData(Id.ITEM, itemId);
        builder.append(item.getName())
                .append("??? ????????????!\n????????? ?????? : ")
                .append(count).append(" -> ")
                .append(count + mineCount.get())
                .append("\n");

        int token = Config.randomToken(ItemList.LOW_MINER_TOKEN.getId(), RandomList.MINE_TOKEN, itemTier, self);
        if(token >= 0) {
            builder.append(Config.TIERS[token])
                    .append("??? ????????? ?????? 1?????? ?????????????????????\n");
        }

        if(this.checkMineLevel(self)) {
            self.addVariable(Variable.MINE, 1);

            mineLv = self.getVariable(Variable.MINE);
            self.replyPlayer("?????? ????????? ???????????????!\n?????? ??????: " + (mineLv - 1) + " -> " + mineLv);
        }

        self.replyPlayer(builder.toString().trim());

        self.setDoing(Doing.NONE);
    }

    public boolean checkMineLevel(@NonNull Player self) {
        int mineLv = self.getVariable(Variable.MINE);
        long mined = self.getLog(LogData.MINED);
        int requireCount;

        if(mineLv == 8) {
            return false;
        }

        switch (mineLv) {
            case 0:
                requireCount = 30;
                break;
            case 1:
                requireCount = 100;
                break;
            case 2:
                requireCount = 300;
                break;
            case 3:
                requireCount = 800;
                break;
            case 4:
                requireCount = 2_000;
                break;
            case 5:
                requireCount = 5_000;
                break;
            case 6:
                requireCount = 15_000;
                break;
            case 7:
                requireCount = 50_000;
                break;
            default:
                throw new NumberRangeException(mineLv, 0, 7);
        }

        return mined >= requireCount;
    }

}
