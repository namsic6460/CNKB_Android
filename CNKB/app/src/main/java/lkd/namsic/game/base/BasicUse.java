package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;

public class BasicUse {
    
    public int getMinTargetCount() {
        return 0;
    }
    
    public int getMaxTargetCount() {
        return 0;
    }
    
    public void checkUse(@NonNull Entity self, @Nullable String other) {
        String msg = "대상을 " + this.getMinTargetCount() + " 개체 이상, " + this.getMaxTargetCount() + " 개체 이하로 입력해주세요";
        
        if(other != null) {
            String[] split = other.split(",");
            int length = split.length;
            if(length < this.getMinTargetCount() || length > this.getMaxTargetCount()) {
                throw new WeirdCommandException(msg);
            }
            
            String result = checkOther(self, other.split(","));
            if(result != null) {
                throw new WeirdCommandException(result);
            }
        } else {
            if(getMinTargetCount() != 0) {
                throw new WeirdCommandException(msg);
            }
        }
    }
    
    @Nullable
    public String checkOther(@NonNull Entity self, @NonNull String... other) {
        return null;
    }
    
}
