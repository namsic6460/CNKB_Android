package lkd.namsic.game.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Farm;
import lkd.namsic.game.object.Player;
import lombok.NonNull;

public class FarmManager {

    private final static FarmManager instance = new FarmManager();

    public static FarmManager getInstance() {
        return instance;
    }

    public void checkFarm(@NonNull Player self) {
        try {
            Config.getData(Id.FARM, self.getId().getObjectId());
        } catch (ObjectNotFoundException e) {
            throw new WeirdCommandException("농장을 먼저 구매해주세요");
        }
    }

    public void buyFarm(@NonNull Player self) {
        boolean isError = false;

        try {
            this.checkFarm(self);
            isError = true;
        } catch (WeirdCommandException ignore) {}

        if(isError) {
            throw new WeirdCommandException("이미 농장을 보유하고 있습니다");
        }

        if(self.getMoney() < Config.FARM_PRICE) {
            throw new WeirdCommandException("농장을 구매하기에 돈이 부족합니다\n부족한 금액: " + (Config.FARM_PRICE - self.getMoney()) + "G");
        }
        
        self.addMoney(-1 * Config.FARM_PRICE);
        Config.unloadObject(new Farm(self.getId().getObjectId()));
        
        self.replyPlayer("농장 구매가 완료되었습니다");
    }
    
    public void plant(@NonNull Player self, @NonNull String seedName, int count) {
        this.checkFarm(self);

        Long itemId = ItemList.findByName(seedName);
        if(itemId == null) {
            throw new WeirdCommandException("존재하지 않는 씨앗입니다");
        }
        
        if(!seedName.endsWith("씨앗")) {
            throw new WeirdCommandException("씨앗만 심을 수 있습니다");
        }

        if(self.getItem(itemId) < count) {
            throw new WeirdCommandException("씨앗 개수가 부족합니다\n현재 보유개수: " + self.getItem(itemId));
        }

        Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());

        try {
            int remainSpace = farm.getMaxPlantCount() - farm.getPlanted().size();
            if (remainSpace < count) {
                throw new WeirdCommandException(count + "개를 심기에 농장의 공간이 " + (count - remainSpace) + "칸 부족합니다");
            }

            Farm.Plant plant = Config.getData(Id.PLANT, itemId);
            if(plant.getLv() > farm.getLv()) {
                throw new WeirdCommandException("해당 씨앗을 심기에 농장의 레벨이 부족합니다");
            }

            for(int i = 0; i < count; i++) {
                farm.getPlanted().add(new Farm.Plant(Objects.requireNonNull(ItemList.idMap.get(itemId))));
            }
        } finally {
            Config.unloadObject(farm);
        }

        self.addItem(itemId, -1 * count);
        self.replyPlayer(seedName + " " + count + "개를 심었습니다");
    }

    public void removePlant(@NonNull Player self, int plantIndex) {
        this.checkFarm(self);

        Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());

        try {
            List<Farm.Plant> plantList = farm.getPlanted();

            if(plantIndex < 1 || plantList.size() < plantIndex) {
                throw new WeirdCommandException("알 수 없는 작물입니다");
            }

            Farm.Plant plant = plantList.get(plantIndex - 1);
            plantList.remove(plant);
            
            self.replyPlayer("작물을 제거했습니다");
        } finally {
            Config.unloadObject(farm);
        }
    }
    
    public void harvest(@NonNull Player self) {
        this.checkFarm(self);

        StringBuilder innerBuilder = new StringBuilder("---수확 현황---");

        Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());

        try {
            long currentTime = System.currentTimeMillis();
            long gap, itemId;
            int harvestCount, itemCount;

            Map<Long, Integer> harvested = new HashMap<>();

            long growTime;
            for(Farm.Plant plant : farm.getPlanted()) {
                growTime = plant.getGrowTime();
                gap = Math.min(currentTime - plant.getLastHarvestTime(), Config.MAX_HARVEST_DAY * 86_400_000);
                harvestCount = (int) (gap / growTime);

                if(harvestCount == 0) {
                    continue;
                }

                for(Map.Entry<Long, Integer> entry : plant.getRewardItem().entrySet()) {
                    itemId = entry.getKey();
                    itemCount = entry.getValue() * harvestCount;

                    self.addItem(itemId, itemCount, false);
                    harvested.put(itemId, harvested.getOrDefault(itemId, 0) + itemCount);
                }

                plant.setLastHarvestTime(plant.getLastHarvestTime() + (growTime * harvestCount));
            }

            if(harvested.isEmpty()) {
                self.replyPlayer("수확 가능한 작물이 없습니다");
                return;
            }

            for(Map.Entry<Long, Integer> entry : harvested.entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(entry.getKey()))
                        .append(" ")
                        .append(Config.getIncrease(entry.getValue()));
            }
        } finally {
            Config.unloadObject(farm);
        }
        
        self.replyPlayer("작물을 수확했습니다", innerBuilder.toString());
    }
    
    public void upgrade(@NonNull Player self) {
        this.checkFarm(self);
        
        Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());
        int farmLv = farm.getLv();

        try {
            if (farmLv == Config.MAX_FARM_LV) {
                throw new WeirdCommandException("이미 농장이 최대 레벨입니다");
            }

            long needMoney = Objects.requireNonNull(RandomList.FARM_UPGRADE_PRICE.get(farmLv));
            if (self.getMoney() < needMoney) {
                throw new WeirdCommandException("농장을 업그레이드 하기에 돈이 부족합니다\n부족한 금액: " + (needMoney - self.getMoney()) + "G");
            }

            self.addMoney(-1 * needMoney);
            
            farmLv++;
            farm.setLv(farmLv);
            
            self.replyPlayer("농장이 " + farmLv + " 레벨로 업그레이드 되었습니다");
        } finally {
            Config.unloadObject(farm);
        }
    }

}
