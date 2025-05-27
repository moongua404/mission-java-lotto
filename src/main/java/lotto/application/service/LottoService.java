package lotto.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lotto.application.domain.Lotto;
import lotto.application.domain.enums.LottoPrize;
import lotto.application.domain.enums.MessageConstants;
import lotto.application.domain.exceptions.ProgramTerminationException;
import lotto.application.port.inport.GetLottoPropertyUseCase;
import lotto.application.port.inport.GetRandomUseCase;
import lotto.application.port.outport.LottoPort;
import lotto.utils.Validator;

public class LottoService {
    private final GetLottoPropertyUseCase getLottoPropertyUseCase;
    private final GetRandomUseCase getRandomUseCase;
    private final LottoPort lottoPort;

    public LottoService(GetLottoPropertyUseCase getLottoPropertyUseCase,
                 GetRandomUseCase getRandomUseCase, LottoPort lottoPort) {
        this.getLottoPropertyUseCase = getLottoPropertyUseCase;
        this.getRandomUseCase = getRandomUseCase;
        this.lottoPort = lottoPort;
    }

    public void run() {
        final int amount = repeat(this::getAmount);
        final Lotto lotto = repeat(this::getLotto);
        final int bonus = repeat(() -> getBonusNumber(lotto));

        List<Lotto> lottoNumbers = IntStream.range(0, amount).mapToObj(i -> {
            Lotto purchased = new Lotto(purchase());
            // lottoNumbers 출력

            return purchased;
        }
        ).toList();

        printResult(lotto, bonus, lottoNumbers, amount);
    }

    private int getAmount() {
        try {
            lottoPort.sendMessage(MessageConstants.PURCHASE_GUIDE);
            int res = getLottoPropertyUseCase.getInteger();
            Validator.isDividedByThousand(res);
            res /= 1000;
            lottoPort.sendMessage(MessageConstants.PURCHASE_RESULT, res);
            return res;
        } catch (NoSuchElementException e) {
            throw new ProgramTerminationException();
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 가격이 잘못 입력되었습니다.");
        }
    }

    private Lotto getLotto() {
        try {
            lottoPort.sendMessage(MessageConstants.WINNING_NUMBER_GUIDE);
            String res = getLottoPropertyUseCase.getString();
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
            lottoPort.sendMessage(MessageConstants.BONUS_NUMBER_GUIDE);
            int res = getLottoPropertyUseCase.getInteger();
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
            } catch (RuntimeException exception) {
                lottoPort.sendMessage(exception.getMessage());
            }
        }
    }

    private List<Integer> purchase() {
        List<Integer> numbers = getRandomUseCase.getRandomNumbers(1, 45, 6);
        lottoPort.printLottoNumbers(numbers);
        return numbers;
    }

    private void printResult(Lotto lotto, int bonusNumber, List<Lotto> purchased, int amount) {
        Map<LottoPrize, Long> result = purchased.stream()
                .map(other -> LottoPrize.getLottoPrize(other.countMatch(lotto), other.isMatch(bonusNumber)))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        long sum = Stream.of(LottoPrize.FIFTH_PRICE, LottoPrize.FOURTH_PRICE, LottoPrize.THIRD_PRICE,
                LottoPrize.SECOND_PRICE, LottoPrize.FIRST_PRICE)
                .peek(prize ->
                    lottoPort.sendMessage(MessageConstants.RESULT_LINE,
                            prize.getCondition(),
                            prize.getPrice(),
                            result.getOrDefault(prize, 0L)))
                .reduce(0L, (acc, prize) -> acc + (prize.getPrice() * result.getOrDefault(prize, 0L)), Long::sum);
        lottoPort.sendMessage(MessageConstants.RETURN_RATE, (float) (sum / (1000L * amount)));
    }
}
