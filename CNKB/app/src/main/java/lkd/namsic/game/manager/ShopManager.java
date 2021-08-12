package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.ShopWaitType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Shop;
import lkd.namsic.setting.Logger;

public class ShopManager {

    private static final ShopManager instance = new ShopManager();

    public static ShopManager getInstance() {
        return instance;
    }

    public void displayHelp(@NonNull Player self) {
        self.replyPlayer("---상점 도움말---\n" +
                Emoji.LIST + " (도움말/명령어/?/help/h) - 상점 도움말을 표시합니다\n" +
                Emoji.LIST + " (종료/end/e) - 상점 이용을 종료합니다\n" +
                Emoji.LIST + " (변경/change/c) - 구매/판매 모드를 전환합니다\n" +
                Emoji.LIST + " ({페이지}) - 해당 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (다음/next/n) - 다음 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (이전/prev/p) - 이전 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (구매/buy/b) ({아이템 이름}) [{아이템 개수}] - 아이템을 구매합니다\n" +
                Emoji.LIST + " (판매/sell/s) ({아이템 이름}) [{아이템 개수}] - 아이템을 판매합니다\n\n" +
                "예시: " + Emoji.focus("n 상점 구매 하급 체력 포션 1"));
    }

    public void displayShopList(@NonNull Player self, @NonNull String npcName) {
        long npcId = NpcList.checkByName(self, npcName);
        Npc npc = Config.getData(Id.NPC, npcId);

        StringBuilder builder = new StringBuilder("---상점 목록---");

        if(npc.getShop().isEmpty()) {
            builder.append("\n해당 Npc 에게서 이용 가능한 상점이 없습니다");
        } else {
            List<Long> sortedList = new ArrayList<>(npc.getShop());
            Collections.sort(sortedList);

            Shop shop;
            int index = 1;
            for (long shopId : sortedList) {
                shop = Config.getData(Id.SHOP, shopId);
                builder.append("\n")
                        .append(index++)
                        .append(". ")
                        .append(shop.getName());
            }
        }

        self.replyPlayer(builder.toString());
    }

    public void startShopping(@NonNull Player self, @NonNull String npcName, int shopIndex) {
        long npcId = NpcList.checkByName(self, npcName);
        Npc npc = Config.getData(Id.NPC, npcId);

        if(self.getChatCount(npcId) == 0) {
            throw new WeirdCommandException("해당 Npc 와 최소 1번 이상 대화를 한 후 상점을 이용할 수 있습니다");
        }

        List<Long> sortedList = new ArrayList<>(npc.getShop());
        Collections.sort(sortedList);

        if(shopIndex < 1 || shopIndex > sortedList.size()) {
            throw new WeirdCommandException("알 수 없는 상점 번호입니다");
        }

        long shopId = sortedList.get(shopIndex - 1);
        Shop shop = Config.getData(Id.SHOP, shopId);

        int page = 1;
        boolean isBuy;

        List<Long> sortedSellList = new ArrayList<>(shop.getSellPrice().keySet());
        Collections.sort(sortedSellList);

        List<Long> sortedBuyList = new ArrayList<>(shop.getBuyPrice().keySet());
        Collections.sort(sortedBuyList);

        if(!shop.getSellPrice().isEmpty()) {
            sortedList = sortedSellList;
            isBuy = true;
        } else if(!shop.getBuyPrice().isEmpty()) {
            sortedList = sortedBuyList;
            isBuy = false;
        } else {
            throw new WeirdCommandException("판매 및 구매 항목이 없는 상점입니다");
        }

        self.setDoing(Doing.SHOP);
        self.replyPlayer(shop.getName() + " 상점 이용을 시작합니다\n" +
                "상점 관련 명령어는 " + Emoji.focus("n 상점 ?") + " 를 참고해주세요");

        displayPage(self, sortedList, shop, npcId, page, isBuy);

        while(true) {
            synchronized (self) {
                try {
                    self.wait(Config.SHOPPING_WAIT_TIME);
                } catch (InterruptedException e) {
                    Logger.e("ShopManager", e);
                    throw new RuntimeException(e.getMessage());
                }

                self.notifyAll();
            }

            ShopWaitType response = self.getObjectVariable(Variable.SHOP, ShopWaitType.END);

            if(response.equals(ShopWaitType.END)) {
                self.replyPlayer("상점 이용을 종료합니다");
                self.setDoing(Doing.NONE);
                self.removeVariable(Variable.SHOP);

                return;
            } else if(response.equals(ShopWaitType.CHANGE)) {
                if(isBuy) {
                    if(shop.getBuyPrice().isEmpty()) {
                        self.replyPlayer("판매 항목이 없는 상점입니다");
                        continue;
                    }

                    sortedList = sortedBuyList;
                } else {
                    if (shop.getSellPrice().isEmpty()) {
                        self.replyPlayer("구매 항목이 없는 상점입니다");
                        continue;
                    }

                    sortedList = sortedSellList;
                }
                
                isBuy = !isBuy;
                page = 1;

                displayPage(self, sortedList, shop, npcId, page, isBuy);
            } else if(response.equals(ShopWaitType.NEXT)) {
                if(page == 1) {
                    self.replyPlayer("현재 첫번째 페이지입니다");
                } else {
                    displayPage(self, sortedList, shop, npcId, --page, isBuy);
                }
            } else if(response.equals(ShopWaitType.PREV)) {
                if(page == Config.getMaxPage(self.getInventory().size())) {
                    self.replyPlayer("현재 마지막 페이지입니다");
                } else {
                    displayPage(self, sortedList, shop, npcId, ++page, isBuy);
                }
            } else if(response.equals(ShopWaitType.PAGE)) {
                page = self.getVariable(Variable.SHOP_PAGE);
                displayPage(self, sortedList, shop, npcId, page, isBuy);
            } else {
                long itemId = Objects.requireNonNull(self.getObjectVariable(Variable.SHOP_ITEM_ID));
                int count = self.getVariable(Variable.SHOP_ITEM_COUNT);

                boolean buying = response.equals(ShopWaitType.BUY);
                if(!(buying ? shop.getSellPrice() : shop.getBuyPrice()).containsKey(itemId)) {
                    String msg = "이 상점에서 ";
                    if(buying) {
                        msg += "구매";
                    } else {
                        msg += "판매";
                    }

                    self.replyPlayer(msg + "가 불가능한 아이템입니다");
                } else {
                    String msg = ItemList.findById(itemId) + " (을/를) " + count + "개 ";
                    long price = (buying ? shop.getSellPrice(self, itemId, npcId) : shop.getBuyPrice(self, itemId, npcId)) * count;

                    if(buying) {
                        long money = self.getMoney();
                        if(money < price) {
                            self.replyPlayer("해당 아이템을 " + count + "개 구매하기에 돈이 " +
                                    (price - money) + "G 부족합니다");
                            continue;
                        }

                        self.addMoney(price * -1);
                        self.addItem(itemId, count, false);
                        self.addLog(LogData.BUY, count);

                        msg += "구매했습니다\n" + "현재";
                    } else {
                        int currentCount = self.getItem(itemId);
                        if(currentCount < count) {
                            self.replyPlayer("해당 아이템을 " + count + "개 판매하기에 아이템이 " +
                                    (count - currentCount) + "개 부족합니다");
                            continue;
                        }

                        self.addMoney(price);
                        self.addItem(itemId, count * -1);
                        self.addLog(LogData.SELL, count);

                        msg += "판매했습니다\n" + "남은";
                    }
                    
                    self.replyPlayer(msg + " 개수: " + self.getItem(itemId) + "개");
                }
            }
        }
    }

    public void displayPage(@NonNull Player self, @NonNull List<Long> sortedList,
                            @NonNull Shop shop, long npcId, int page, boolean isBuy) {
        int maxPage = Config.getMaxPage(self.getInventory().size());

        if(page < 1 || page > maxPage) {
            self.replyPlayer("1 ~ " + maxPage + " 페이지 범위 내의 숫자를 입력해주세요");
            return;
        }

        StringBuilder builder;
        StringBuilder innerBuilder;

        String string;
        if(isBuy) {
            string = "구매";
        } else {
            string = "판매";
        }

        builder = new StringBuilder(string).append(" 목록 ").append(page).append(" 페이지는 전체보기로 확인해주세요");
        innerBuilder = new StringBuilder("---").append(string).append(" 가능 목록---");

        long itemId;
        int startIndex = Config.MAX_COUNT_PER_PAGE * (page - 1);
        for(int i = 0; i < Config.MAX_COUNT_PER_PAGE; i++) {
            try {
                itemId = sortedList.get(startIndex + i);
            } catch (IndexOutOfBoundsException e) {
                break;
            }

            innerBuilder.append("\n")
                    .append(ItemList.findById(itemId))
                    .append(": ");

            if(isBuy) {
                innerBuilder.append(shop.getSellPrice(self, itemId, npcId));
            } else {
                innerBuilder.append(shop.getBuyPrice(self, itemId, npcId));
            }

            innerBuilder.append("G");
        }

        self.replyPlayer(builder.toString(), innerBuilder.toString());
    }

    public void shopCommand(@NonNull Player self, @NonNull String command, @NonNull String subCommand, int count) {
        ShopWaitType response = ShopWaitType.parseWaitType(command);

        if(response.equals(ShopWaitType.BUY) || response.equals(ShopWaitType.SELL)) {
            Long itemId = ItemList.findByName(subCommand);
            if(itemId == null) {
                throw new WeirdCommandException("알 수 없는 아이템입니다\n아이템의 이름을 다시 확인해주세요");
            }
            
            if(count < 1 || count > 999) {
                throw new WeirdCommandException("아이템의 개수는 1 ~ 999개 사이로 입력해주세요");
            }

            self.setVariable(Variable.SHOP_ITEM_ID, itemId);
            self.setVariable(Variable.SHOP_ITEM_COUNT, count);
        } else if(response.equals(ShopWaitType.PAGE)) {
            self.setVariable(Variable.SHOP_PAGE, Integer.parseInt(command));
        }

        self.setVariable(Variable.SHOP, response);

        synchronized (self) {
            self.notifyAll();
        }
    }

}
