package lkd.namsic.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Config;
import lkd.namsic.Game.Exception.WeirdCommandException;
import lkd.namsic.Game.GameObject.Player;
import lkd.namsic.MainActivity;
import lkd.namsic.Setting.Logger;

public class KakaoTalk {

    private final static Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    private final static Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

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
            Player player = null;

            try {
                Logger.i("chatLog", "sender: " + sender + "\nimage: " + image +
                        "\nmsg: " + msg + "\nroom: " + room + "\nisGroupChat: " + isGroupChat +
                        "\ncommand: " + command);
                if (msg.equals("namsicTest")) {
                    reply(session, "reply");
                }

                if(isCommand) {
                    boolean isEnd = false;
                    player = Config.loadPlayer(sender, image);
                    if (player == null) {
                        isEnd = playerCommand(sender, image, command, room, session);
                    } else {
                        isEnd = nonPlayerCommand(sender, image, command, room, session);
                    }

                    if (!isEnd) {
                        otherCommand(command, session);
                    }
                }
            } catch (Exception e) {
                Logger.e("onChat", e);
            } finally {
                if(player != null) {
                    Config.unloadObject(player);
                }
            }
        });

        MainActivity.startThread(gameThread);
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
            builder.append("\n(자세한 내용은 전체보기 확인)\n");

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

    private void replyAll(@NonNull String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

    private static boolean playerCommand(@NonNull String sender, @NonNull String image,
                                         @NonNull String command, @NonNull String room,
                                         @NonNull Notification.Action session) {


        return false;
    }

    private static boolean nonPlayerCommand(@NonNull String sender, @NonNull String image,
                                         @NonNull String command, @NonNull String room,
                                         @NonNull Notification.Action session) {
        try {
            String[] commands = command.split(" ");

            if (Arrays.asList("회원가입", "가입", "register").contains(commands[0])) {
                if (commands.length != 2) {
                    throw new WeirdCommandException();
                }

                String nickName = commands[1];
                //닉네임 검사

                return true;
            }
        } catch (WeirdCommandException e) {
            reply(session, Objects.requireNonNull(e.getMessage()));
        }

        return false;
    }

    private static void otherCommand(@NonNull final String command, @NonNull final Notification.Action session) {
        if(Arrays.asList("도움말", "명령어", "?", "h", "help").contains(command)) {
            reply(session, "=====명령어 목록=====\n" +
                            "모든 명령어는 접두어 'n' 또는 'ㅜ'가 포함됩니다\n" +
                            "영어는 대소문자에 영향받지 않습니다\n" +
                            "'*' 이 붙은 명령어는 언제나 사용이 가능한 명령어입니다\n" +
                            "() : 필수 명령어, [] : 필수x 명령어",
                    "---전체 사용 가능 명령어---\n" +
                            "*(도움말/명령어/?/h/help) : 도움말을 표시합니다\n" +
                            "[회원가입/가입/register] (닉네임) : 회원가입을 합니다\n" +
                            "*[개발자/dev] : 개발자 정보를 표시합니다\n" +
                            "\n---유저 명령어---\n" +
                            "(정보/info/i) : 내 정보를 표시합니다\n" +
                            "(맵/map) : 현재 위치 정보를 표시합니다\n");
        } else if(Arrays.asList("개발자", "dev").contains(command)) {
            reply(session, "---개발자 정보---\n닉네임 : 남식(namsic)",
                    "메일 : namsic6460@gmail.com\n" +
                            "문의 : 개인 카카오톡 또는 이메일");
        }
    }

}
