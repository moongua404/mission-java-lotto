package lotto.application.domain.enums;

import java.util.List;

public enum LottoPrize {
    NOTHING("꽝", 0),
    FIFTH_PRICE("3개 일치", 5_000),
    FOURTH_PRICE("4개 일치", 50_000),
    THIRD_PRICE("5개 일치", 1_500_000),
    SECOND_PRICE("5개 일치, 보너스 볼 일치", 30_000_000),
    FIRST_PRICE("6개 일치", 2_000_000_000);

    private final String condition;
    private final int price;

    LottoPrize(String condition, int price) {
        this.condition = condition;
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public int getPrice() {
        return price;
    }

    public static List<LottoPrize> getPrizeTypes() {
        return List.of(FIFTH_PRICE, FOURTH_PRICE, THIRD_PRICE, SECOND_PRICE, FIRST_PRICE);
    }

    public static LottoPrize getLottoPrize(int matchCount, boolean matchBonus) {
        if (matchCount == 6) {
            return getFirstOrSecondPrize(matchBonus);
        }
        if (matchCount == 5) {
            return THIRD_PRICE;
        }
        if (matchCount == 4) {
            return FOURTH_PRICE;
        }
        if (matchCount == 3) {
            return FIFTH_PRICE;
        }
        return NOTHING;
    }

    private static LottoPrize getFirstOrSecondPrize(boolean matchBonus) {
        if (matchBonus) {
            return SECOND_PRICE;
        }
        return FIRST_PRICE;
    }
}