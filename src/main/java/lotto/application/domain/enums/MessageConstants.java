package lotto.application.domain.enums;

public enum MessageConstants {
    PURCHASE_GUIDE("구입금액을 입력해 주세요."),
    PURCHASE_RESULT("%d개를 구매했습니다."),
    WINNING_NUMBER_GUIDE("당첨 번호를 입력해 주세요."),
    BONUS_NUMBER_GUIDE("보너스 번호를 입력해 주세요."),
    WINNING_STATUS("당첨 통계\n---"),
    RESULT_LINE("%s (%,d원) - %d개"),
    RETURN_RATE("총 수익률은 %.1f%%입니다.")


    ;
    private String message;
    MessageConstants(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
