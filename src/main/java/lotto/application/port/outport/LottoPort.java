package lotto.application.port.outport;

import java.util.List;
import lotto.application.domain.enums.MessageConstants;

public interface LottoPort {
    void sendMessage(MessageConstants message);
    void sendMessage(MessageConstants message, Object... params);
    void sendMessage(String message);
    void clear();
    void printLottoNumbers(List<Integer> numbers);
}
