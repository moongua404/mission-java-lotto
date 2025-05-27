package lotto.application.port.inport;

import java.util.List;

public interface GetRandomUseCase {
    List<Integer> getRandomNumbers(int min, int max, int amount);
}
