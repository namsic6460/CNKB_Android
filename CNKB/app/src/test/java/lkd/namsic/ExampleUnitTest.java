package lkd.namsic;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.FileManager;

public class ExampleUnitTest {

    @Test
    public void evalTest() {
        try {
            System.out.println(Config.class.getDeclaredMethod("getBar", int.class, int.class).invoke(null, 0, 150));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        List<Entity> sortedList = new ArrayList<>();
        sortedList.add(new Monster(MonsterList.OWLBEAR));
        sortedList.add(new Player("a", "", "", ""));
        sortedList.add(new Monster(MonsterList.LYCANTHROPE));
        sortedList.add(new Player("b", "", "", ""));
        sortedList.add(new Monster(MonsterList.GOLEM));

        sortedList.sort((o1, o2) -> {
            boolean isPlayer1 = o1.getId().getId().equals(Id.PLAYER);
            boolean isPlayer2 = o2.getId().getId().equals(Id.PLAYER);

            if(isPlayer1 != isPlayer2) {
                return isPlayer1 ? 1 : -1;
            } else {
                return 0;
            }
        });

        for(Entity entity : sortedList) {
            System.out.println(entity.getName());
        }
    }

    public long getNeedExp(int lv) {
        long needExp;
        if (lv <= 100) {
            needExp = 10_000 + 5_000L * lv;
        } else if (lv <= 300) {
            needExp = -200_000 + (long) (Math.pow(lv - 10, 2.5) * 2) + 10_000 * lv;
        } else if (lv <= 500) {
            needExp = -50_000_000 + 200_000 * lv;
        } else if(lv <= 750) {
            needExp = -50_000_000 + (long) (Math.pow(lv, 3.1) / 1.5);
        } else if(lv <= 950) {
            needExp = 45_000_000L * (lv - 750) + 1_000_000_000L;
        } else {
            needExp = -30_000_000_000L + (long) Math.pow(lv - 600, 4.2);
        }

        return needExp;
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

        String json;
        Player player;
        for(File file : files) {
            json = FileManager.read(file);
            player = Config.gson.fromJson(json, Player.class);

            player.addMoney(player.getLv() * -5000L);
            player.addMoney(Math.min(player.getLv() * 5000, 500_000));

            FileManager.save(file.getAbsolutePath(), Config.gson.toJson(player));
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
    
    @Test
    public void scheduleTest() throws InterruptedException {
        Timer timer = new Timer();
        
        long startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("A: " + (System.currentTimeMillis() - startTime));
                timer.cancel();
            }
        }, 1000, 1000);
    
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(
            () -> System.out.println("B: " + (System.currentTimeMillis() - startTime)),
            1000,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(3000);
    }
    
    @Test
    public void regexTest() {
        System.out.println(Config.replaceLast("음양검1 +5", Pattern.quote("+5"), ""));
    }
    
    @Test
    public void killEventTest() {
        for(int i = 1; i <= 500; i++) {
            System.out.println(i + ": " + ((int) (Math.atan(i / 100D) * 2_000_000)));
        }
    }

}