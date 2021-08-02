package lkd.namsic.game;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import lkd.namsic.MainActivity;
import lkd.namsic.game.command.CommandListener;
import lkd.namsic.game.command.CommonCommand;
import lkd.namsic.game.command.NonPlayerCommand;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.command.common.DevCommand;
import lkd.namsic.game.command.common.HelpCommand;
import lkd.namsic.game.command.non_player.RegisterCommand;
import lkd.namsic.game.command.player.debug.CleanCommand;
import lkd.namsic.game.command.player.debug.DoingCommand;
import lkd.namsic.game.command.player.debug.EvalCommand;
import lkd.namsic.game.command.player.debug.GiveCommand;
import lkd.namsic.game.command.player.debug.SaveCommand;
import lkd.namsic.game.command.player.game.AdventureCommand;
import lkd.namsic.game.command.player.game.AppraiseCommand;
import lkd.namsic.game.command.player.game.ChatCommand;
import lkd.namsic.game.command.player.game.CraftCommand;
import lkd.namsic.game.command.player.game.EatCommand;
import lkd.namsic.game.command.player.game.EquipCommand;
import lkd.namsic.game.command.player.game.FightCommand;
import lkd.namsic.game.command.player.game.FishCommand;
import lkd.namsic.game.command.player.game.InfoCommand;
import lkd.namsic.game.command.player.game.InvenCommand;
import lkd.namsic.game.command.player.game.MapCommand;
import lkd.namsic.game.command.player.game.MineCommand;
import lkd.namsic.game.command.player.game.MoveCommand;
import lkd.namsic.game.command.player.game.RankingCommand;
import lkd.namsic.game.command.player.game.SettingCommand;
import lkd.namsic.game.command.player.game.StatCommand;
import lkd.namsic.game.command.player.game.UseCommand;
import lkd.namsic.game.command.player.register.PlayerRegisterCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.EquipUseException;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.ChatManager;
import lkd.namsic.service.NotificationListener;
import lkd.namsic.setting.Logger;

public class KakaoTalk {

    public final static Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    public final static Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

    private static String lastSender = "";
    private static String lastMsg = "";

    private static final Map<String, CommonCommand> commonCommands = new HashMap<>();
    private static final Map<String, PlayerCommand> playerCommands = new HashMap<>();
    private static final Map<String, NonPlayerCommand> nonPlayerCommands = new HashMap<>();

    public static void registerCommands() {
        commonCommands.clear();
        playerCommands.clear();
        nonPlayerCommands.clear();

        Logger.i("CommandRegister", "Commands cleared");

        try {
            registerCommonCommand(new HelpCommand(), "도움말", "명령어", "?", "h", "help");
            registerCommonCommand(new DevCommand(), "개발자", "dev");

            registerNonPlayerCommand(new RegisterCommand(), "회원가입", "가입", "register");

            registerPlayerCommands();
        } catch (WeirdCommandException ignore) {}

        Logger.i("CommandRegister", "Commands register complete");
    }

    private static void registerPlayerCommands() {
        //Debug
        registerPlayerCommand(new GiveCommand(), "give");
        registerPlayerCommand(new DoingCommand(), "doing");
        registerPlayerCommand(new SaveCommand(), "save");
        registerPlayerCommand(new EvalCommand(), "eval");
        registerPlayerCommand(new CleanCommand(), "clean");

        //Register
        registerPlayerCommand(new PlayerRegisterCommand(), "회원가입", "가입", "register");

        //Game
        registerPlayerCommand(new AdventureCommand(),   "모험", "adventure", "adv");
        registerPlayerCommand(new AppraiseCommand(),    "감정", "appraise", "apr");
        registerPlayerCommand(new ChatCommand(),        "대화", "chat");
        registerPlayerCommand(new CraftCommand(),       "제작", "craft");
        registerPlayerCommand(new EatCommand(),         "먹기", "eat");
        registerPlayerCommand(new EquipCommand(),       "장비", "equip");
        registerPlayerCommand(new FightCommand(),       "전투", "fight", "f");
        registerPlayerCommand(new FishCommand(),        "낚시", "fish");
        registerPlayerCommand(new InfoCommand(),        "정보", "info", "i");
        registerPlayerCommand(new InvenCommand(),       "가방", "인벤토리", "인벤", "inventory", "inven");
        registerPlayerCommand(new MapCommand(),         "맵", "map");
        registerPlayerCommand(new MineCommand(),        "광질", "mine");
        registerPlayerCommand(new MoveCommand(),        "이동", "move");
        registerPlayerCommand(new RankingCommand(),     "랭킹", "랭크", "ranking", "rank");
        registerPlayerCommand(new SettingCommand(),     "설정", "setting", "set");
        registerPlayerCommand(new StatCommand(),        "스텟", "stat");
        registerPlayerCommand(new UseCommand(),         "사용", "use");
    }

    private static void registerCommonCommand(@NonNull CommonCommand listener, @NonNull String...commands) {
        registerCommand(commonCommands, listener, commands);
    }

    private static void registerPlayerCommand(@NonNull PlayerCommand listener, @NonNull String...commands) {
        registerCommand(playerCommands, listener, commands);
    }

    private static void registerNonPlayerCommand(@NonNull NonPlayerCommand listener, @NonNull String...commands) {
        registerCommand(nonPlayerCommands, listener, commands);
    }

    private static <T extends CommandListener> void registerCommand(@NonNull Map<String, T> map, @NonNull T listener, @NonNull String...commands) {
        for(String command : commands) {
            T prevListener = map.put(command, listener);

            if(prevListener != null) {
                String errorString = "<" + command + "> already exists\n" + map.toString();
                Logger.e("CommandRegister", errorString);

                throw new WeirdCommandException();
            }
        }
    }

    @Nullable
    public static Notification.Action getGroupSession(String room) {
        return groupSessions.get(room);
    }

    @Nullable
    public static Notification.Action getSoloSession(String room) {
        return soloSessions.get(room);
    }

    public static void onChat(@NonNull final String sender, @NonNull final String image,
                              @NonNull final String msg, @NonNull final String room, final boolean isGroupChat,
                              @NonNull final Notification.Action session) {
        if(isGroupChat) {
            groupSessions.put(room, session);
        } else {
            soloSessions.put(room, session);
        }

        boolean isCommand = msg.startsWith("N ") || msg.startsWith("n ") || msg.startsWith("ㅜ ");
        final String command = isCommand ? msg.substring(2) : null;

        Thread gameThread = new Thread(() -> {
            CommandListener listener;
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

                try {
                    player = Config.loadPlayer(sender, image);
                } catch (ObjectNotFoundException ignore) {}

                if(isCommand) {
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

                    String cmd = command.replaceFirst(Pattern.quote(first), "").trim();

                    if((listener = KakaoTalk.commonCommands.get(first)) != null) {
                        listener.execute(cmd, session);
                    } else {
                        try {
                            if (player != null && (listener = KakaoTalk.playerCommands.get(first)) != null) {
                                player.setRecentRoom(room);
                                player.setGroup(isGroupChat);
                                player.checkNewDay();

                                listener.execute(player, cmd, commands, second, third, fourth, session);
                            } else if (player == null) {
                                listener = KakaoTalk.nonPlayerCommands.get(first);

                                if (listener != null) {
                                    listener.execute(sender, image, cmd, room, isGroupChat, session);
                                } else {
                                    //TODO: 공기계로 진행할 때 주석 해제
//                                throw new WeirdCommandException("현재 회원가입이 되어있지 않습니다.\n회원가입을 진행해주세요");
                                }
                            }
                        } catch (WeirdCommandException | DoingFilterException | EquipUseException e) {
                            KakaoTalk.reply(session,"[오류]\n" + e.getMessage());
                        } catch (NumberFormatException e) {
                            String message = e.getMessage();
                            message = message == null ? "" : "\n" + message;

                            if(player != null) {
                                player.replyPlayer("숫자를 입력해야합니다", message);
                            }
                        }
                    }
                }

                if(player != null) {
                    if(player.getDoing().equals(Doing.WAIT_RESPONSE)) {
                        ChatManager.getInstance().checkResponseChat(player, msg);
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

    public static void checkDoing(@NonNull Player player) throws DoingFilterException {
        if(!player.getDoing().equals(Doing.NONE)) {
            throw new DoingFilterException(player.getDoing());
        }
    }

    public static void reply(@Nullable Notification.Action session, @Nullable String msg){
        reply(session, msg == null ? "" : msg, null);
    }

    public static void reply(@Nullable Notification.Action session, @Nullable String msg, @Nullable String innerMsg) {
        if(session == null) {
            return;
        }

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

    public static void replyGroup(@NonNull String msg) {
        for(Notification.Action session : groupSessions.values()) {
            reply(session, msg);
        }
    }

    @Deprecated
    public static void replyAll(@NonNull String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

}
