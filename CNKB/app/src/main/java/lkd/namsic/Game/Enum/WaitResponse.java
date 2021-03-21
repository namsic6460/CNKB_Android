package lkd.namsic.Game.Enum;

import androidx.annotation.NonNull;

public enum WaitResponse {

    NONE,
    YES,
    NO,
    NUMBER,
    ANYTHING;

    public static WaitResponse fromString(@NonNull String response) {
        response = response.toLowerCase();

        try {
            Integer.valueOf(response);
            return NUMBER;
        } catch (NumberFormatException ignore) {}

        if(response.equals("네") || response.equals("ㅇㅇ") || response.equals("yes") || response.equals("y")) {
            return YES;
        } else if(response.equals("아니오") || response.equals("아니요") || response.equals("ㄴㄴ") ||
                response.equals("no") || response.equals("n")) {
            return NO;
        }

        return ANYTHING;
    }

}
