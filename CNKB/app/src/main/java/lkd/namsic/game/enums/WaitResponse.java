package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public enum WaitResponse {

    NONE(),
    ANYTHING(),
    YES(Arrays.asList("네", "ㅇㅇ", "yes", "y")),
    NO(Arrays.asList("아니오", "아니요", "ㄴㄴ", "no", "n"));

    @Getter
    public List<String> list;

    WaitResponse() {}

    WaitResponse(List<String> list) {
        this.list = list;
    }

}
