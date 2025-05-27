package lotto.adapter.in;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;
import lotto.application.port.inport.GetRandomUseCase;

public class RandomLibrary implements GetRandomUseCase {
    @Override
    public List<Integer> getRandomNumbers(int min, int max, int amount) {
        return Randoms.pickUniqueNumbersInRange(min, max, amount);
    }
}
