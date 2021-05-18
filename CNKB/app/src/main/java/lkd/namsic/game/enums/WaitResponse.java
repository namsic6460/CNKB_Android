package lkd.namsic.game.enums;

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

    public String getDisplay() {
        if(this.equals(ANYTHING)) {
            return "아무 채팅이나 입력해주세요";
        }

        StringBuilder builder = new StringBuilder("(");

        boolean first = true;
        for(String string : this.list) {
            if(first) {
                first = false;
            } else {
                builder.append("/");
            }

            builder.append(string);
        }

        return builder.append(")").toString();
    }

}
