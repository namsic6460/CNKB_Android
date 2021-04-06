package lkd.namsic.Game.Enum;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public enum WaitResponse {

    NONE(),
    ANYTHING(),
    NUMBER(),
    YES(Arrays.asList("네", "ㅇㅇ", "yes", "y")),
    NO(Arrays.asList("아니오", "아니요", "ㄴㄴ", "no", "n"));

    private List<String> list;

    WaitResponse() {}

    WaitResponse(List<String> list) {
        this.list = list;
    }

    public static WaitResponse fromString(@NonNull String response) {
        response = response.toLowerCase();

        try {
            Integer.valueOf(response);
            return NUMBER;
        } catch (NumberFormatException ignore) {}

        if(YES.list.contains(response)) {
            return YES;
        } else if(NO.list.contains(response)) {
            return NO;
        }

        return ANYTHING;
    }

}
