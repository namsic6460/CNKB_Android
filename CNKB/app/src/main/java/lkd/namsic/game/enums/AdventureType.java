package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import lombok.Getter;

public enum AdventureType {
    
    NONE("", ""),
    RIVER("도랑에 빠져버렸습니다", "도랑을 안전하게 건넜습니다"),
    SPIKE("가시 덤불에 독이 있었나 봅니다", "가시 덤불을 무사히 빠져나왔습니다"),
    ROBBER("도적들을 잘못 건드렸나 봅니다", "도적 무리를 성공적으로 지나왔습니다"),
    ANCIENT("알 수 없는 무언가에 찔려 그대로 기절했습니다", "유적을 성공적으로 지나왔습니다"),
    RAIN("벼락에 정통으로 맞았습니다", "비가 그쳤습니다"),
    CAT("알 수 없는 검은 힘에 그대로 기절했습니다", "고양이는 귀여웠습니다"),
    CLIFF("절벽에 떨어져 기절했습니다", "절벽을 안전하게 건넜습니다"),
    SUN("태양의 열기에 그대로 기절했습니다", "이정도 열기는 가뿐하네요"),
    GOD("고래 싸움에 끼인 새우처럼 기절해버렸습니다", "신도 별 거 아닌가보네요"),
    CRACK("공간이 깨지며 정신도 아득해졌습니다", "다른 차원을 탐사하는데에 성공했습니다");
    
    @Getter
    private final String failMsg;
    
    @Getter
    private final String successMsg;
    
    AdventureType(@NonNull String failMsg, @NonNull String successMsg) {
        this.failMsg = failMsg;
        this.successMsg = successMsg;
    }
    
}
