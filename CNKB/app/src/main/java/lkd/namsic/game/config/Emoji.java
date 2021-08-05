package lkd.namsic.game.config;

public class Emoji {

    public final static String OPENER = "「";
    public final static String CLOSER = "」";
    public final static String LIST = "⚫";  //consider '-'
    public final static String GOLD = "\uD83D\uDCB0";
    public final static String HEART = "♥";
    public final static String MANA = "\uD83D\uDCA7";
    public final static String FILLED_BAR = "█";
    public final static String[] HALF_BAR = {"", "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};
    public final static String WORLD = "🌏";
    public final static String LV = "⭐";
    public final static String SP = "\uD83C\uDF59";
    public final static String EXP = "✨";
    public final static String ADV = "\uD83D\uDDFA";
    public final static String HOME = "\uD83C\uDFE0";
    public final static String MONSTER = "\uD83D\uDC3E";
    public final static String BOSS = "❗";
    public final static String STAR = "★";

    public static String focus(String text) {
        return OPENER + text + CLOSER;
    }

}
