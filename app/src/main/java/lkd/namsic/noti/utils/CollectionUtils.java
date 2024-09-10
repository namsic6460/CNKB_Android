package lkd.namsic.noti.utils;

import java.util.Collection;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] list) {
        return list == null || list.length == 0;
    }
}
