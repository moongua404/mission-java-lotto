package lotto.adapter.in;

import camp.nextstep.edu.missionutils.Console;
import lotto.application.port.inport.GetLottoPropertyUseCase;

public class InputTerminal implements GetLottoPropertyUseCase {
    @Override
    public String getString() {
        return Console.readLine();
    }

    @Override
    public int getInteger() {
        return Integer.parseInt(Console.readLine());
    }
}
