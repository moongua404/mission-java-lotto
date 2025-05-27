package lotto.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validator {
    public static void fitSize(Collection<?> collection, int size) {
        if (collection.size() != size) {
            throw new IllegalStateException("[ERROR] size mismatch");
        }
    }

    public static <T> void hasNoDuplication(List<T> itemList) {
        if (new HashSet<>(itemList).size() != itemList.size()) {
            throw new IllegalStateException("[ERROR] Duplicated item exist.");
        };
    }

    public static void isInRange(int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalStateException("[ERROR] Invalid range.");
        }
    }

    public static void isInRange(List<Integer> values, int min, int max) {
        if (values.stream().allMatch(v -> v >= min && v <= max)) {
            throw new IllegalStateException("[ERROR] Invalid range.");
        }
    }

    public static void isDividedByThousand(int value) {
        if (value % 1000 != 0) {
            throw new IllegalStateException("[ERROR] Invalid divided thousand.");
        }
    }
}
