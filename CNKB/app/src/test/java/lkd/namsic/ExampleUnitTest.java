package lkd.namsic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.json.ChatLimitAdapter;
import lkd.namsic.game.json.LocationAdapter;
import lkd.namsic.game.json.NpcAdapter;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Shop;
import lkd.namsic.setting.FileManager;

public class ExampleUnitTest {

    @Test
    public void evalTest() {
        try {
            System.out.println(FightManager.class.getDeclaredMethod("getInstance").invoke(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Shop shop = new Shop(NpcList.PEDRO);

        shop.addSellItem(ItemList.PIECE_OF_GEM, 20000L);

        shop.addBuyItem(ItemList.STONE_LUMP, 30L);
        shop.addBuyItem(ItemList.COAL, 5L);

        for(ItemList gem : Config.GEMS) {
            shop.addBuyItem(gem, 1000L);
        }

        shop.addBuyItem(ItemList.QUARTZ, 20L);
        shop.addBuyItem(ItemList.GOLD, 100L);
        shop.addBuyItem(ItemList.WHITE_GOLD, 500L);

        Set<Long> idSet = Arrays.stream(Config.GEMS).map(ItemList::getId).collect(Collectors.toSet());
        shop.addSimpleMap(idSet, "감정", "appraise", "apr");

        idSet = new HashSet<>(idSet);
        idSet.remove(ItemList.QUARTZ.getId());
        idSet.remove(ItemList.GOLD.getId());
        idSet.remove(ItemList.WHITE_GOLD.getId());
        shop.addSimpleMap(idSet, "보석", "gem");

        System.out.println(shop.getSimpleMap().toString());

        String json = Config.gson.toJson(shop);
        System.out.println(json);
    }

    @Test
    public void queueTest() {
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        Thread thread1 = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                queue.offer("a");
                queue.offer("a");
                queue.offer("a");

                synchronized (this) {
                    if(queue.size() != 0) {
                        this.notifyAll();
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1990);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                queue.offer("b");
                queue.offer("b");
                queue.offer("b");
                queue.offer("b");
                queue.offer("b");

                synchronized (this) {
                    if(queue.size() != 0) {
                        this.notifyAll();
                    }
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(4980);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");
                queue.offer("c");

                synchronized (this) {
                    if(queue.size() != 0) {
                        this.notifyAll();
                    }
                }
            }
        });

        new Thread(() -> {
        synchronized (this) {
            while(true) {
                String data = queue.poll();
                if(data != null) {
                    int size = queue.size();

                    System.out.println(data + " " + queue.toString());

                    try {
                        if(size < 5) {
                            Thread.sleep(200);
                        } else if(size < 10) {
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    System.out.println("Waiting - " + queue.toString());

                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }}).start();

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadTest() throws Exception {
        String path = "C:\\Users\\user\\Downloads\\";
        File[] files = Objects.requireNonNull(new File(path + "players").listFiles());

        int day = LocalDateTime.now().getDayOfYear();

        String json;
        Player player;
        for(File file : files) {
            json = FileManager.read(file);
            player = Config.gson.fromJson(json, Player.class);

            if(player.getLastDay() + 30 < day) {
                System.out.println(player.getName() + " - " + file.delete());
            }
        }

        System.out.println("DONE");
    }

    @Test
    public void reinforceTest() {
        int total = 0;

        Equipment equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1, null, null);
        equipment.setHandleLv(6);

        Random random = new Random();
        double basePercent;
        double percent;
        for (int i = 0; i < Config.MAX_REINFORCE_COUNT; i++) {
            basePercent = equipment.getReinforcePercent(1);

            for (int j = 1; ; j++) {
                total++;
                percent = equipment.getReinforcePercent(1);

                if (random.nextDouble() < percent) {
                    equipment.successReinforce();
                    System.out.println(i + "(" + Config.getDisplayPercent(basePercent) + "): " + j);

                    break;
                } else {
                    equipment.failReinforce(percent);
                }
            }
        }

        System.out.println(total);
    }

}