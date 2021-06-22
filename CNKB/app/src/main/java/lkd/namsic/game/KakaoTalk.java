package lkd.namsic.game;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.MainActivity;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.ChatManager;
import lkd.namsic.game.manager.DisplayManager;
import lkd.namsic.game.manager.AdventureManager;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.manager.FishManager;
import lkd.namsic.game.manager.ItemManager;
import lkd.namsic.game.manager.MineManager;
import lkd.namsic.game.manager.MoveManager;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.ObjectList;
import lkd.namsic.service.NotificationListener;
import lkd.namsic.setting.Logger;

public class KakaoTalk {

    private final static Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    private final static Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

    private static String lastSender = "";
    private static String lastMsg = "";

    private final static ChatManager chatManager = ChatManager.getInstance();
    private final static DisplayManager displayManager = DisplayManager.getInstance();
    private final static AdventureManager ADVENTURE_MANAGER = AdventureManager.getInstance();
    private final static FishManager fishManager = FishManager.getInstance();
    private final static FightManager fightManager = FightManager.getInstance();
    private final static ItemManager itemManager = ItemManager.getInstance();
    private final static MineManager mineManager = MineManager.getInstance();
    private final static MoveManager moveManager = MoveManager.getInstance();

    @NonNull
    public static Notification.Action getGroupSession(String room) {
        return Objects.requireNonNull(groupSessions.get(room));
    }

    @NonNull
    public static Notification.Action getSoloSession(String room) {
        return Objects.requireNonNull(soloSessions.get(room));
    }

    public static void onChat(@NonNull final String sender, @NonNull final String image,
                              @NonNull final String msg, @NonNull final String room, final boolean isGroupChat,
                              @NonNull final Notification.Action session) {
        if(isGroupChat) {
            groupSessions.put(room, session);
        } else {
            soloSessions.put(room, session);
        }

        final boolean isCommand = msg.startsWith("n ") || msg.startsWith("ㅜ ");
        final String command = isCommand ? msg.substring(2) : null;

        Thread gameThread = new Thread(() -> {
            Player player = null;

            try {
                if(lastSender.equals(sender) && lastMsg.equals(msg)) {
                    Logger.i("Chat", "ReChat");
                } else {
                    Logger.i("Chat", "\nsender: " + sender + "\nmsg: " + msg +
                            "\nroom: " + room + "\nisGroupChat: " + isGroupChat);
                }

                lastSender = sender;
                lastMsg = msg;

                player = Config.loadPlayer(sender, image);

                if(isCommand) {
                    otherCommand(command.toLowerCase(), session);

                    if (player != null) {
                        playerCommand(player, command, room, isGroupChat, session);
                    } else {
                        nonPlayerCommand(sender, image, command, room, isGroupChat, session);
                    }
                }

                if(player != null) {
                    if(player.getDoing().equals(Doing.WAIT_RESPONSE)) {
                        chatManager.checkResponseChat(player, msg);
                    }

                    Config.unloadObject(player);
                }
            } catch (Exception e) {
                Logger.e("onChat", e);

                //TODO: 추후 봇 전용 휴대폰으로 돌릴 시, 에러 발생을 reply 가 아닌 replyAll 으로 알려야한다.
                reply(session, "[ERROR]\n" + Config.errorString(e));
//                replyAll("[ERROR]\n" + Config.errorString(e));

                if(player != null) {
                    Config.discardPlayer(player);
                }
            }
        });

        MainActivity.startThread(gameThread);
    }

    private static void playerCommand(@NonNull Player player, @NonNull String command, @NonNull String room,
                                      boolean isGroupChat, @NonNull Notification.Action session) {
        player.setRecentRoom(room);
        player.setGroup(isGroupChat);

        player.checkNewDay();

        try {
            List<String> commands = Arrays.asList(command.toLowerCase().split(" "));

            String first = commands.get(0);
            String second = null;
            String third = null;
            String fourth = null;

            try {
                second = commands.get(1);
                third = commands.get(2);
                fourth = commands.get(3);
            } catch (IndexOutOfBoundsException ignore) {}

            if(player.getId().getObjectId() == 1) {
                switch (first) {
                    case "give":
                        player.addItem(Long.parseLong(second), Integer.parseInt(third));
                        reply(session, "Success");
                        return;
                    case "save":
                        Logger.saveLog();
                        reply(session, "Success");
                        return;
                    case "doing":
                        player.setDoing(Doing.valueOf(Objects.requireNonNull(second).toUpperCase()));
                        reply(session, "Success");
                        return;
                    case "eval":
                        String result = Eval.execute(command.replace("eval ", ""), player);
                        reply(session, result);
                }
            }

            if (Arrays.asList("회원가입", "가입", "register").contains(first)) {
                reply(session, "이미 회원가입이 되어 있습니다.\n" +
                        "회원가입을 진행한 적이 없는 경우, 프로필 이미지 또는 카카오톡 이름을 변경한 후 다시 시도해주세요");
            } else {
                Doing doing = player.getDoing();

                if (Arrays.asList("정보", "info", "i").contains(first)) {
                    displayManager.displayInfo(player);
                } else if (first.equals("맵") || first.equals("map")) {
                    if (second == null) {
                        MapClass map = Config.getMapData(player.getLocation());
                        player.replyPlayer(map.getInfo(), map.getInnerInfo());
                    } else if (second.equals("목록") || second.equals("list")) {
                        displayManager.displayMapList(player);
                    } else {
                        throw new WeirdCommandException();
                    }
                } else if(first.equals("대화") || first.equals("chat")) {
                    checkDoing(doing);

                    if(second == null) {
                        throw new WeirdCommandException();
                    }

                    chatManager.startChat(player, second);
                } else if(Arrays.asList("가방", "인벤토리", "inventory", "inven").contains(first)) {
                    int page = second == null ? 1 : Integer.parseInt(second);
                    displayManager.displayInventory(player, page);
                } else if(Arrays.asList("설정", "setting", "set").contains(first)) {
                    if(second == null) {
                        throw new WeirdCommandException();
                    }

                    if(Arrays.asList("가방", "인벤토리", "inventory", "inven").contains(second)) {
                        if(fourth == null) {
                            throw new WeirdCommandException();
                        }

                        Long itemId = ObjectList.itemList.get(fourth);
                        if(itemId == null) {
                            throw new ObjectNotFoundException("해당 이름을 가진 아이템을 찾을 수 없습니다");
                        }

                        List<Long> highPriorityItems = player.getListVariable(Variable.HIGH_PRIORITY_ITEM);

                        if(third.equals("추가") || third.equals("add")) {
                            if (!highPriorityItems.isEmpty()) {
                                if (highPriorityItems.contains(itemId)) {
                                    throw new WeirdCommandException("해당 아이템은 이미 우선순위에 등록되어 있습니다");
                                }
                            }

                            highPriorityItems.add(itemId);
                            player.replyPlayer("아이템이 우선순위에 성공적으로 등록되었습니다");
                        } else if(third.equals("제거") || third.equals("remove")) {
                            if(highPriorityItems.isEmpty()) {
                                throw new ObjectNotFoundException("우선순위 목록이 비어있습니다");
                            } else if(!highPriorityItems.contains(itemId)) {
                                throw new ObjectNotFoundException("우선순위 목록에 해당 아이템이 등록되어있지 않습니다");
                            }

                            if(highPriorityItems.size() == 1) {
                                player.getVariable().remove(Variable.HIGH_PRIORITY_ITEM);
                            } else {
                                highPriorityItems.remove(itemId);
                            }

                            player.replyPlayer("아이템이 우선순위에서 성공적으로 제거되었습니다");
                        } else {
                            throw new WeirdCommandException();
                        }
                    } else if(second.equals("pvp")) {
                        checkDoing(doing);

                        if(third == null) {
                            player.replyPlayer("현재 PVP가 " + (player.isPvp() ? "활성화" : "비활성화") + "되어있습니다");
                        } else {
                            if (third.equals("켜기") || third.equals("on")) {
                                player.setPvp(true, null);
                            } else if (third.equals("끄기") || third.equals("off")) {
                                if(fourth == null) {
                                    throw new WeirdCommandException();
                                }

                                int day = Integer.parseInt(fourth);
                                if(day != 1 && day != 7) {
                                    throw new WeirdCommandException("PvP 비활성화는 1일 또는 7일만 가능합니다");
                                }

                                player.setPvp(false, day);
                            }
                        }
                    }
                } else if(first.equals("이동") || first.equals("move")) {
                    checkDoing(doing);

                    if(second == null) {
                        throw new WeirdCommandException();
                    }

                    if(second.equals("맵") || second.equals("map")) {
                        String locationStr = command.replaceFirst(first, "").replaceFirst(second, "").trim();
                        moveManager.moveMap(player, locationStr);
                    } else if(second.equals("필드") || second.equals("field")) {
                        if(third == null) {
                            throw new WeirdCommandException();
                        }

                        moveManager.moveField(player, third);
                    } else {
                        String locationStr = command.replace(first, "").trim();
                        moveManager.moveMap(player, locationStr.trim());
                    }
                } else if (first.equals("광질") || first.equals("mine")) {
                    boolean isTutorial = player.getObjectVariable(Variable.IS_TUTORIAL, false);
                    if(!isTutorial) {
                        checkDoing(doing);
                    } else {
                        player.setVariable(Variable.IS_TUTORIAL, false);
                    }

                    if (mineManager.canMine(player)) {
                        mineManager.mine(player);

                        if(isTutorial) {
                            player.setDoing(Doing.WAIT_RESPONSE);
                        }
                    } else {
                        player.replyPlayer("광질이 불가능한 상태입니다");
                    }
                } else if (first.equals("낚시") || first.equals("fish")) {
                    if(doing.equals(Doing.NONE)) {
                        checkDoing(doing);

                        if(second != null) {
                            throw new WeirdCommandException();
                        }

                        fishManager.tryFish(player);
                    } else if(doing.equals(Doing.FISH)) {
                        if(second == null) {
                            throw new WeirdCommandException();
                        }

                        fishManager.fishCommand(player, second);
                    } else {
                        throw new DoingFilterException();
                    }
                } else if (Arrays.asList("전투", "fight", "f").contains(first)) {
                    if(doing.equals(Doing.NONE)) {
                        if(second == null) {
                            throw new WeirdCommandException("전투 대상을 선택해주세요");
                        }
                        
                        if(!fightManager.tryFight(player, command.replace(first, "").trim())) {
                            player.replyPlayer("해당 상대와의 전투가 불가능한 상태입니다",
                                    "대상을 정확히 지정했는지 다시 확인해주세요\n" +
                                            "대상이 플레이어인 경우, 본인 또는 대상의 pvp가 꺼져있을 수 있습니다\n" +
                                            "또는 대상이 현재 다른 행동을 하고 있을 수 있습니다\n" +
                                            "또는 일부 전투의 경우 난입이 불가능할 수도 있습니다");
                        }
                    } else if(Doing.fightList().contains(doing)) {
                        if(second == null) {
                            throw new WeirdCommandException();
                        }

                        fightManager.fightCommand(player, second, third);
                    } else {
                        throw new DoingFilterException();
                    }
                } else if (first.equals("제작") || first.equals("craft")) {
                    if(second == null) {
                        throw new WeirdCommandException();
                    }

                    if(Arrays.asList("아이템", "item", "장비", "equip").contains(second)) {
                        displayManager.displayRecipes(player, second.equals("아이템") || second.equals("item"));
                    } else if(second.equals("확인") || second.equals("check")) {
                        displayManager.displayRecipe(player, command.replace(first, "").replace(second, "").trim());
                    } else {
                        checkDoing(doing);
                        itemManager.craft(player, command.replace(first, "").trim());
                    }
                } else if (Arrays.asList("모험", "adventure", "adv").contains(first)) {
                    if(doing.equals(Doing.NONE)) {
                        checkDoing(doing);

                        if(second != null) {
                            throw new WeirdCommandException();
                        }

                        ADVENTURE_MANAGER.tryAdventure(player);
                    } else if(doing.equals(Doing.ADVENTURE)) {
                        if(second == null) {
                            throw new WeirdCommandException();
                        }

                        ADVENTURE_MANAGER.exploreCommand(player, second);
                    } else {
                        throw new DoingFilterException();
                    }
                }
            }
        } catch (WeirdCommandException | DoingFilterException | ObjectNotFoundException e) {
            player.replyPlayer("[오류]\n" + e.getMessage());
            Logger.w("Common Error", e);
        } catch (NumberFormatException e) {
            player.replyPlayer("숫자를 입력해야합니다", Objects.requireNonNull(e.getMessage()));
            Logger.w("Number Error", e);
        }
    }

    private static void nonPlayerCommand(@NonNull String sender, @NonNull String image,
                                            @NonNull String command, @NonNull String room,
                                            boolean isGroupChat, @NonNull Notification.Action session) {
        try {
            List<String> commands = Arrays.asList(command.split(" "));

            String first = commands.get(0).toLowerCase();

            if (Arrays.asList("회원가입", "가입", "register").contains(first)) {
                String nickName = command.replace(first, "").trim();

                if (nickName == null) {
                    throw new WeirdCommandException("닉네임을 입력해주세요\n" +
                            "(예시: " + Emoji.focus("n 회원가입 남식") + ")");
                }

                String nickNameCheck = Config.getRegex(nickName, "");
                if(!nickName.equals(nickNameCheck)) {
                    reply(session, "닉네임에 허용되지 않는 문자가 포함되어 있습니다\n" +
                                    "상세 내용은 전체보기에서 확인해주세요",
                            "입력받은 닉네임 : " + nickName + "\n" +
                                    "변경되어야 하는 닉네임: " + nickNameCheck + "\n\n" +
                                    "[닉네임 규칙]\n" +
                                    Emoji.LIST + " 영어 대소문자\n" +
                                    Emoji.LIST + " 한글\n" +
                                    Emoji.LIST + " 숫자\n" +
                                    Emoji.LIST + " 하이픈(-)\n" +
                                    Emoji.LIST + " 언더바(_)\n" +
                                    Emoji.LIST + " 연속 공백은 최대 1칸" +
                                    Emoji.LIST + " 길이는 2~16자");
                    return;
                }

                int length = nickName.length();
                if(length < 2 || length > 16) {
                    throw new WeirdCommandException("닉네임의 길이가 2~16자가 되도록 변경해주세요\n" +
                            "현재 " + length + "자 입니다");
                }

                if(Config.PLAYER_ID.get(nickName) != null) {
                    throw new WeirdCommandException("[" + nickName + "]\n동일한 닉네임을 지닌 플레이어가 존재합니다.\n" +
                            "닉네임을 변경한 후 다시 시도해주세요");
                }

                if(ObjectList.npcList.containsKey(nickName)) {
                    throw new WeirdCommandException("[" + nickName + "]\n동일한 닉네임을 지닌 NPC가 존재합니다.\n" +
                            "닉네임을 변경한 후 다시 시도해주세요");
                }

                if(ObjectList.monsterList.containsKey(nickName) || ObjectList.bossList.containsKey(nickName)) {
                    throw new WeirdCommandException("[" + nickName + "]\n동일한 닉네임을 지닌 몬스터가 존재합니다.\n" +
                            "닉네임을 변경한 후 다시 시도해주세요");
                }

                Player player = new Player(sender, nickName, image, room);
                player.setGroup(isGroupChat);
                player.setVariable(Variable.IS_TUTORIAL, true);

                long objectId = Config.ID_COUNT.get(Id.PLAYER) + 1;
                player.getId().setObjectId(objectId);
                Config.PLAYER_ID.put(nickName, objectId);
                Config.PLAYER_LIST.put(objectId, new String[] {sender, image});
                Config.ID_COUNT.put(Id.PLAYER, objectId);

                for(long itemId = 12L; itemId <= 19L; itemId++) {
                    player.getItemRecipe().add(itemId);
                }

                MapClass map = null;
                try {
                    map = Config.loadMap(0, 0);
                    map.addEntity(player);
                } finally {
                    if(map != null) {
                        Config.unloadMap(map);
                    }
                }

                player.replyPlayer("회원가입에 성공하였습니다!");
                Config.unloadObject(player);

                Config.loadPlayer(sender, image);
                chatManager.startChat(player, 1L, 1L);
                Config.unloadObject(player);
            } else {
                //TODO: 공기계로 진행할 때 주석 해제
//                throw new WeirdCommandException("현재 회원가입이 되어있지 않습니다.\n회원가입을 진행해주세요");
            }
        } catch (WeirdCommandException e) {
            reply(session, Objects.requireNonNull(e.getMessage()));
        }
    }

    private static void otherCommand(@NonNull final String command, @NonNull final Notification.Action session) {
        if(Arrays.asList("도움말", "명령어", "?", "h", "help").contains(command)) {
            reply(session, "=====명령어 목록=====\n" +
                            "모든 명령어는 앞쪽에 " + Emoji.focus("n") + " 또는 " + Emoji.focus("ㅜ") + " 를 붙여야합니다\n" +
                            "(예시 : " + Emoji.focus("n 가방 1") + ")\n" +
                            "영어는 대소문자에 영향받지 않습니다\n" +
                            Emoji.focus("*") + " 이 붙은 명령어는 언제나 사용이 가능한 명령어입니다\n" +
                            "() : 필수 명령어, [] : 필수x 명령어, {} : 직접 입력",
                    "---전체 사용 가능 명령어---\n" +
                            Emoji.LIST + " *(도움말/명령어/?/h/help) : 도움말을 표시합니다\n" +
                            Emoji.LIST + " (회원가입/가입/register) ({닉네임}) : 회원가입을 합니다\n" +
                            Emoji.LIST + " *(개발자/dev) : 개발자 정보를 표시합니다\n" +
                            "\n---유저 명령어---\n" +
                            Emoji.LIST + " *(정보/info/i) : 내 정보를 표시합니다\n" +
                            Emoji.LIST + " *(가방/인벤토리/inventory/inven) [{페이지}] : 현재 인벤토리를 확인합니다\n" +
                            Emoji.LIST + " (대화/chat) ({NPC 이름}) : 해당 NPC 와 대화를 합니다\n" +
                            Emoji.LIST + " *(맵/map) : 현재 위치 정보를 표시합니다\n" +
                            Emoji.LIST + " *(맵/map) (목록/list) : 이동 가능한 주변 맵의 정보를 표시합니다\n" +
                            Emoji.LIST + " (이동/move) [맵/map] ({x좌표}-{y좌표}) : 해당 좌표의 지역으로 이동합니다(예시: " +
                            Emoji.focus("n move 2-3") + ")\n" +
                            Emoji.LIST + " (이동/move) [맵/map] ({맵 이름}) : 해당 맵으로 이동합니다(예시 : " +
                            Emoji.focus("n move map 시작의 마을") + ")\n" +
                            Emoji.LIST + " (이동/move) (필드/field) ({x좌표}-{y좌표}) : 해당 좌표의 필드 위치로 이동합니다(예시: " +
                            Emoji.focus("n move field 16-10") + ")\n" +
                            Emoji.LIST + " (광질/mine) : 광질을 합니다(약 1초 소요)(마을에서만 가능)\n" +
                            Emoji.LIST + " (낚시/fish) : 낚시를 합니다(추가 명령 필요)(강 또는 바다에서만 가능)\n" +
                            Emoji.LIST + " (전투/fight/f) ({닉네임}/{좌표}) : 대상과의 전투를 시작합니다" +
                            "(좌표로 지정할 경우 몬스터 또는 보스만 대상이 됩니다)\n" +
                            Emoji.LIST + " (모험/adventure/adv) : 현재 지역을 탐색합니다(마을 이외 지역에서만 가능)(모험 중 공격받을 수 있음)\n" +
                            Emoji.LIST + " *(제작/craft) (아이템/item/장비/equip) : 제작 가능한 아이템 또는 장비 목록을 확인합니다\n" +
                            Emoji.LIST + " *(제작/craft) (확인/check) ({아이템 또는 장비 이름}) : 해당 아이템 또는 장비의 제작법을 확인합니다\n" +
                            Emoji.LIST + " (제작/craft) ({아이템 또는 장비 이름}) : 해당 아이템 또는 장비를 제작합니다\n" +
                            Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (추가/add) ({아이템 이름}) : " +
                            "해당 아이템을 인벤토리 표시 우선순위 목록에 추가합니다\n" +
                            Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (제거/remove) ({아이템 이름}) : " +
                            "해당 아이템을 인벤토리 표시 우선순위 목록에서 제거합니다\n" +
                            Emoji.LIST + " *(설정/setting/set) (pvp) : PvP 설정을 확인합니다\n" +
                            Emoji.LIST + " *(설정/setting/set) (pvp) (켜기/on) : PvP 설정을 활성화합니다\n" +
                            Emoji.LIST + " *(설정/setting/set) (pvp) (끄기/off) (1/7) : PvP 설정을 1일 또는 7일간 비활성화합니다" +
                            "(아이템 필요)(시간은 반올림 적용)");
        } else if(command.equals("개발자") || command.equals("dev")) {
            reply(session, "---개발자 정보---\n" +
                    "닉네임 : 남식(namsic)\n" +
                    "메일 : namsic6460@gmail.com\n" +
                    "문의 : 개인 카카오톡 또는 이메일");
        }
    }

    private static void checkDoing(@NonNull Doing doing) {
        if(!doing.equals(Doing.NONE)) {
            throw new DoingFilterException();
        }
    }

    public static void reply(@NonNull Notification.Action session, @Nullable String msg){
        reply(session, msg == null ? "" : msg, null);
    }

    public static void reply(@NonNull Notification.Action session, @Nullable String msg, @Nullable String innerMsg) {
        msg = msg == null ? "" : msg;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        if(innerMsg != null) {
            StringBuilder builder = new StringBuilder(msg);

            for(int i = 0; i < 500; i++) {
                builder.append('\u200b');
            }

            builder.append("\n\n")
                    .append(innerMsg);

            msg = builder.toString();
        }

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), msg);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);

        try {
            session.actionIntent.send(NotificationListener.context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Logger.e("KakaoTalkListener", e);
        }
    }

    private static void replyGroup(@NonNull String msg) {
        for(Notification.Action session : groupSessions.values()) {
            reply(session, msg);
        }
    }

    @Deprecated
    private static void replyAll(@NonNull String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

}
