package lkd.namsic.service;

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

import lkd.namsic.game.Config;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.Variable;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.MainActivity;
import lkd.namsic.setting.Logger;

public class KakaoTalk {

    private final static Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    private final static Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

    private static String lastSender = "";
    private static String lastMsg = "";

    public static Notification.Action getGroupSession(String room) {
        return Objects.requireNonNull(groupSessions.get(room));
    }

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
        final String command = isCommand ? msg.substring(2).toLowerCase() : null;

        Thread gameThread = new Thread(() -> {
            Player player;

            try {
                if(lastSender.equals(sender) && lastMsg.equals(msg)) {
                    Logger.i("chatLog", "ReChat");
                } else {
                    Logger.i("chatLog", "\nsender: " + sender + "\nmsg: " +
                            msg.substring(0, Math.min(msg.length(), 100)).trim() +
                            "\nroom: " + room + "\nisGroupChat: " + isGroupChat);
                }

                lastSender = sender;
                lastMsg = msg;

                if(isCommand) {
                    boolean isEnd;
                    player = Config.loadPlayer(sender, image);

                    if (player != null) {
                        isEnd = playerCommand(player, msg, command, room, isGroupChat, session);
                    } else {
                        isEnd = nonPlayerCommand(sender, image, command, room, isGroupChat, session);
                    }

                    if (!isEnd) {
                        otherCommand(command, session);
                    }
                }
            } catch (Exception e) {
                Logger.e("onChat", e);

                //TODO: 추후 봇 전용 휴대폰으로 돌릴 시, 에러 발생을 replyAll 으로 알려야한다.
                reply(session, "[ERROR!]\n" + e.getMessage());

                if(player != null) {
                    Config.discardPlayer(player);
                }
            }
        });

        MainActivity.startThread(gameThread);
    }

    private static boolean playerCommand(@NonNull Player player, @NonNull String msg, @NonNull String command,
                                         @NonNull String room, boolean isGroupChat, @NonNull Notification.Action session) {
        if(!player.checkChat()) {
            return true;
        }

        player.setRecentRoom(room);
        player.setGroup(isGroupChat);

        boolean isRightCmd = false;

        try {
            List<String> commands = Arrays.asList(command.split(" "));

            String first = commands.get(0);
            String second = null;
            String third = null;
            String fourth = null;

            try {
                second = commands.get(1);
                third = commands.get(2);
                fourth = commands.get(3);
            } catch (IndexOutOfBoundsException ignore) {}

            if (Arrays.asList("회원가입", "가입", "register").contains(first)) {
                reply(session, "이미 회원가입이 되어 있습니다.\n" +
                        "회원가입을 진행한 적이 없는 경우, 프로필 이미지 또는 카카오톡 이름을 변경한 후 다시 시도해주세요");

                isRightCmd = true;
            } else {
                if (Arrays.asList("정보", "info", "i").contains(first)) {
                    player.displayInfo();

                    isRightCmd = true;
                } else if (Arrays.asList("맵", "map").contains(first)) {
                    MapClass map = Config.getMapData(player.getLocation());
                    player.replyPlayer(map.getInfo(), map.getInnerInfo());

                    isRightCmd = true;
                } else if(Arrays.asList("가방", "인벤토리", "inventory", "inven").contains(first)) {
                    int page = second == null ? 0 : Integer.parseInt(second);
                    player.displayInventory(page);

                    isRightCmd = true;
                } else if(Arrays.asList("설정", "setting", "set").contains(first)) {
                    if(second == null) {
                        throw new WeirdCommandException();
                    }

                    if(Arrays.asList("가방", "인벤토리", "inventory", "inven").contains(second)) {
                        if(fourth == null) {
                            throw new WeirdCommandException();
                        }

                        long itemId = ObjectList.getItem(fourth);
                        if(itemId == 0L) {
                            throw new ObjectNotFoundException("해당 이름을 가진 아이템을 찾을 수 없습니다");
                        }

                        List<Long> highPriorityItems = player.getObjectVariable(Variable.HIGH_PRIORITY_ITEM);

                        if(Arrays.asList("추가", "add").contains(third)) {
                            if(highPriorityItems == null) {
                                highPriorityItems = new ConcurrentArrayList<>();
                                highPriorityItems.add(itemId);
                                player.setVariable(Variable.HIGH_PRIORITY_ITEM, highPriorityItems);
                            } else {
                                if(highPriorityItems.contains(itemId)) {
                                    throw new WeirdCommandException("해당 아이템은 이미 우선순위에 등록되어 있습니다");
                                }

                                highPriorityItems.add(itemId);
                            }
                        } else if(Arrays.asList("제거", "remove").contains(third)) {
                            if(highPriorityItems == null) {
                                throw new ObjectNotFoundException("우선순위 목록이 비어있습니다");
                            } else if(!highPriorityItems.contains(itemId)) {
                                throw new ObjectNotFoundException("우선순위 목록에 해당 아이템이 등록되어있지 않습니다");
                            }

                            if(highPriorityItems.size() == 1) {
                                player.getVariable().remove(Variable.HIGH_PRIORITY_ITEM);
                            } else {
                                highPriorityItems.remove(itemId);
                            }
                        }
                    }
                } else if (Arrays.asList("광질", "mine").contains(first)) {
                    checkDoing(player);

                    if (player.canMine()) {
                        player.mine();
                    } else {
                        player.replyPlayer("광질이 불가능한 상태입니다");
                    }

                    isRightCmd = true;
                }
            }
        } catch (WeirdCommandException | DoingFilterException | ObjectNotFoundException e) {
            player.replyPlayer(Objects.requireNonNull(e.getMessage()));
        } catch (NumberFormatException e) {
            player.replyPlayer("숫자를 입력해야합니다");
        }

        checkResponseChat(player, msg);
        Config.unloadObject(player);
        
        return isRightCmd;
    }

    private static boolean nonPlayerCommand(@NonNull String sender, @NonNull String image,
                                            @NonNull String command, @NonNull String room,
                                            boolean isGroupChat, @NonNull Notification.Action session) {
        boolean isRightCmd = false;

        try {
            List<String> commands = Arrays.asList(command.split(" "));

            String first = null;
            String second = null;

            try {
                first = commands.get(0);
                second = commands.get(1);
            } catch (IndexOutOfBoundsException ignore) {}

            if (Arrays.asList("회원가입", "가입", "register").contains(first)) {
                if (second == null) {
                    throw new WeirdCommandException();
                }

                if(Config.NICKNAME_LIST.contains(second)) {
                    reply(session, "[" + second + "]\n동일한 닉네임을 지닌 플레이어가 존재합니다.\n" +
                            "닉네임을 변경한 후 다시 시도해주세요");
                }

                Player player = new Player(sender, second, image, room);
                player.setGroup(isGroupChat);
                player.setCloseRate(1L, 100);
                player.setCloseRate(2L, 100);
                Config.unloadObject(player);

                player.replyPlayer("회원가입에 성공하였습니다!");
                player.startChat(1L, "???");

                isRightCmd = true;
            }
        } catch (WeirdCommandException e) {
            reply(session, Objects.requireNonNull(e.getMessage()));
        }

        return isRightCmd;
    }

    private static void otherCommand(@NonNull final String command, @NonNull final Notification.Action session) {
        if(Arrays.asList("도움말", "명령어", "?", "h", "help").contains(command)) {
            reply(session, "=====명령어 목록=====\n" +
                            "모든 명령어는 접두어 'n' 또는 'ㅜ'가 포함됩니다\n" +
                            "영어는 대소문자에 영향받지 않습니다\n" +
                            "'*' 이 붙은 명령어는 언제나 사용이 가능한 명령어입니다\n" +
                            "() : 필수 명령어, [] : 필수x 명령어, {} : 직접 입력",
                    "---전체 사용 가능 명령어---\n" +
                            "*(도움말/명령어/?/h/help) : 도움말을 표시합니다\n" +
                            "(회원가입/가입/register) ({닉네임}) : 회원가입을 합니다\n" +
                            "*(개발자/dev) : 개발자 정보를 표시합니다\n" +
                            "\n---유저 명령어---\n" +
                            "(정보/info/i) : 내 정보를 표시합니다\n" +
                            "(맵/map) : 현재 위치 정보를 표시합니다\n" +
                            "(광질/mine) : 광질을 합니다(마을에서만 가능)\n" +
                            "(가방/인벤토리/inventory/inven) [{페이지}] : 현재 인벤토리를 확인합니다\n" +
                            "(설정/setting/set) (가방/인벤토리/inventory/inven) (추가/add) ({아이템 이름}) : " +
                            "해당 아이템을 인벤토리 표시 우선순위로 설정합니다\n" +
                            "(설정/setting/set) (가방/인벤토리/inventory/inven) (제거/remove) ({아이템 이름}) : " +
                            "해당 아이템을 인벤토리 표시 우선순위에서 해제합니다");
        } else if(Arrays.asList("개발자", "dev").contains(command)) {
            reply(session, "---개발자 정보---\n닉네임 : 남식(namsic)",
                    "메일 : namsic6460@gmail.com\n" +
                            "문의 : 개인 카카오톡 또는 이메일");
        }
    }

    private static void checkResponseChat(@NonNull Player player, @NonNull String msg) {
        String response = null;

        if(msg.startsWith("__")) {
            return;
        } else if(msg.startsWith("n ") || msg.startsWith("ㅜ ")) {
            response = msg.replaceAll("n ", "__").replaceAll("ㅜ ", "__");
        }
        
        for(Map.Entry<WaitResponse, Long> entry : player.getResponseChat().entrySet()) {
            if(entry.getKey().getList().contains(response)) {
                player.startChat(entry.getValue(), player.getWaitNpcName());
                return;
            }
        }

        for(Map.Entry<String, Long> entry : player.getAnyResponseChat().entrySet()) {
            if(entry.getKey().equals(response)) {
                player.startChat(entry.getValue(), player.getWaitNpcName());
                return;
            }
        }
    }

    private static void checkDoing(@NonNull Player player) {
        Doing doing = player.getDoing();

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
            builder.append("\n");

            for(int i = 0; i < 500; i++) {
                builder.append('\u200b');
            }
            builder.append('\n');
            builder.append(innerMsg);

            msg = builder.toString();
        }

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), msg);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);

        try {
            session.actionIntent.send(KakaoTalkListener.context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Logger.e("KakaoTalkListener", e);
        }
    }

    private void replyGroup(@NonNull String msg) {
        for(Notification.Action session : groupSessions.values()) {
            reply(session, msg);
        }
    }

    @Deprecated
    private void replyAll(@NonNull String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

}
