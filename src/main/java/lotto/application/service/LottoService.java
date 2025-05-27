package lotto.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import lotto.application.domain.Lotto;
import lotto.application.domain.enums.LottoPrize;
import lotto.application.domain.exceptions.ProgramTerminationException;
import lotto.utils.Validator;

public class LottoService {
    public void run() {
        final int price = repeat(this::getPrice);
        final Lotto lotto = repeat(this::getLotto);
        final int bonus = repeat(() -> getBonusNumber(lotto));

        List<Lotto> lottoNumbers = IntStream.range(0, price/1000).mapToObj(i -> {
            Lotto purchased = new Lotto(purchase());
            // lottoNumbers 출력
            return purchased;
        }).toList();

        printResult(lotto, bonus, lottoNumbers);
    }

    private int getPrice() {
        try {
            int res = 3000; // 입력 받아야 함
            Validator.isDividedByThousand(res);
            return res;
        } catch (NoSuchElementException e) {
            throw new ProgramTerminationException();
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 가격이 잘못 입력되었습니다.");
        }
    }

    private Lotto getLotto() {
        try {
            String res = "1,2,3,4,5,6";
            return new Lotto(
                    Arrays.stream(res.split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                    .toList()
            );
        } catch (NoSuchElementException e) {
            throw new ProgramTerminationException();
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 로또 번호가 잘못 입력되었습니다.");
        }
    }

    private int getBonusNumber(Lotto lotto) {
        try {
            int res = 7;
            if (lotto.isDuplicate(res)) {
                throw new IllegalArgumentException();
            }
            return res;
        } catch (NoSuchElementException e) {
            throw new ProgramTerminationException();
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 보너스 숫자가 잘못 입력되었습니다.");
        }
    }

    private <T> T repeat(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (ProgramTerminationException e) {
                throw new IllegalStateException(e.getMessage());
            } catch (RuntimeException ignored) {
            }
        }
    }

    private List<Integer> purchase() {
        return List.of(1, 2, 3, 4, 5, 6); // 랜덤으로 숫자를 뽑음
    }

    private void printResult(Lotto lotto, int bonusNumber, List<Lotto> purchased) {
        List<LottoPrize> result = purchased.stream()
                .map(other -> LottoPrize.getLottoPrize(other.countMatch(lotto), other.isMatch(bonusNumber)))
                .toList();
    }
}
