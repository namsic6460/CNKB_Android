package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class ReinforceManager {

    private static final ReinforceManager instance = new ReinforceManager();

    public static ReinforceManager getInstance() {
        return instance;
    }

    public void displayReinforceExplanation(@NonNull Player self) {
        self.replyPlayer(
                "1. 장비는 최대 " + Config.MAX_REINFORCE_COUNT + "강 까지 강화할 수 있습니다\n" +
                        "2. 강화에 성공할 시 모든 스텟이 균등하게 증가하지만 제한 레벨 또한 증가될 수 있습니다\n" +
                        "3. 일정 횟수 이상 강화에 실패하면 무조건 강화에 성공하는 천장 시스템이 있습니다\n" +
                        "4. 장비의 입수 난이도 및 다양한 조건에 따라 강화의 난이도 및 성장치가 변화합니다"
        );
    }

    public void reinforce(@NonNull Player self, int index) {
        long equipId = self.getEquipIdByIndex(index);
        Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);

        try {
            self.setDoing(Doing.REINFORCE);

            if (self.isEquipped(equipment.getEquipType(), equipId)) {
                throw new WeirdCommandException("장착중인 장비는 강화가 불가능합니다");
            }

            int reinforceCount = equipment.getReinforceCount();
            if(reinforceCount == Config.MAX_REINFORCE_COUNT) {
                throw new WeirdCommandException(Config.MAX_REINFORCE_COUNT + "강 장비는 더이상 강화가 불가능합니다");
            }

            long needItem = equipment.getReinforceItem();
            if(self.getItem(needItem) < 1) {
                throw new WeirdCommandException("해당 장비를 강화하기 위해서는 " + Emoji.focus(ItemList.findById(needItem)) + "이 필요합니다");
            }

            long money = self.getMoney();
            long needMoney = equipment.getReinforceCost();
            if(money < needMoney) {
                throw new WeirdCommandException("해당 장비를 강화하기 위한 비용이 " + (needMoney - money) + "G 부족합니다\n" +
                        "강화 비용: " + needMoney + "G");
            }

            double reinforcePercent = equipment.getReinforcePercent(self.getReinforceMultiplier());
            self.replyPlayer("강화를 시작합니다!\n성공 확률: " + Config.getDisplayPercent(reinforcePercent) + "\n" +
                    "깡... 깡... 깡...");

            try {
                Thread.sleep(Config.REINFORCE_DELAY_TIME);
            } catch (InterruptedException e) {
                Logger.e("ReinforceManager", e);
            }

            self.addItem(needItem, -1);
            self.addMoney(needMoney * -1);

            if(Math.random() < reinforcePercent) {
                String originalName = equipment.getName();

                equipment.successReinforce();
                self.replyPlayer(Emoji.STAR + "강화에 성공했습니다!" + Emoji.STAR + "\n" +
                        originalName + " -> " + equipment.getName());
            } else {
                equipment.failReinforce(reinforcePercent);
                self.replyPlayer("강화에 실패했습니다...\n다음 강화 성공 확률: " +
                        Config.getDisplayPercent(equipment.getReinforcePercent(1)));
            }

            self.setReinforceMultiplier(1);
        } finally {
            self.setDoing(Doing.NONE);
            Config.unloadObject(equipment);
        }
    }

}
