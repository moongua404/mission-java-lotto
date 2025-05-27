package lotto.application.domain.exceptions;

public class ProgramTerminationException extends IllegalArgumentException {
    public ProgramTerminationException() {
        super("[ERROR] 예기치 않은 문제로 프로그램을 종료합니다.");
    }
}
