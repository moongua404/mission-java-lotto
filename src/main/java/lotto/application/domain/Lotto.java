package lotto.application.domain;

import java.util.List;
import lotto.utils.Validator;

public class Lotto {
    private final List<Integer> numbers;

    private final static int LOTTO_SIZE = 6;
    private final static int LOWER_BOUND = 1;
    private final static int UPPER_BOUND = 45;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        try {
            Validator.fitSize(numbers, LOTTO_SIZE);
            Validator.hasNoDuplication(numbers);
            Validator.isInRange(numbers, LOWER_BOUND, UPPER_BOUND);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("[ERROR] 부적절한 로또 번호 입니다.", e);
        }
    }

    // TODO: 추가 기능 구현
    public boolean isDuplicate(int numbers) {
        return this.numbers.contains(numbers);
    }

    public int countMatch(Lotto other) {
        return (int) numbers.stream()
                .filter(other.numbers::contains)
                .count();
    }

    public boolean isMatch(int bonus) {
        return numbers.contains(bonus);
    }
}