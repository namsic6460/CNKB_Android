package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public enum WaitResponse {
    
    NONE(),
    ANYTHING(),
    YES("네", "ㅇㅇ", "yes", "y"),
    NO("아니오", "아니요", "ㄴㄴ", "no", "n");
    
    @Getter
    private final List<String> list = new ArrayList<>();
    
    WaitResponse() {
    }
    
    WaitResponse(String... texts) {
        this.list.addAll(Arrays.asList(texts));
    }
    
    @NonNull
    public static WaitResponse parseResponse(@NonNull String response) {
        if(YES.list.contains(response)) {
            return YES;
        } else if(NO.list.contains(response)) {
            return NO;
        } else {
            return ANYTHING;
        }
    }
    
    @NonNull
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
