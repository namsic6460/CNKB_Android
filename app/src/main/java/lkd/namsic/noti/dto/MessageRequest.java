package lkd.namsic.noti.dto;

import androidx.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private String message;
    private String innerMessage;
    private String sender;
    @Nullable private String room;
}
