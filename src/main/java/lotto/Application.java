package lotto;

import lotto.adapter.in.InputTerminal;
import lotto.adapter.in.RandomLibrary;
import lotto.adapter.out.OutputTerminal;
import lotto.application.service.LottoService;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        LottoService lottoService = new LottoService(
                new InputTerminal(),
                new RandomLibrary(),
                new OutputTerminal()
        );

        lottoService.run();
    }
}
