package lkd.namsic.game;

public class Emoji {

    public final static String OPENER = "「";
    public final static String CLOSER = "」";
    public final static String GOLD = "\uD83D\uDCB0";
    public final static String HEART = "♥";
    public final static String MANA = "\uD83D\uDCA7";
    public final static String FILLED_HEART = "█";
    public final static String[] HALF_HEART = {"", "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};
    public final static String WORLD = "🌏";
    public final static String LV = "⭐";
    public final static String SP = "\uD83C\uDF59";
    public final static String ADV = "\uD83D\uDDFA";
    public final static String HOME = "\uD83C\uDFE0";
    public final static String MONSTER = "\uD83D\uDC3E";
    public final static String BOSS = "❗";

    public static String focus(String text) {
        return OPENER + text + CLOSER;
    }

}
