package lotto;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ApplicationTest extends NsTest {
    private static final String ERROR_MESSAGE = "[ERROR]";

    @Test
    void 기능_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("8000", "1,2,3,4,5,6", "7");
                    assertThat(output()).contains(
                            "8개를 구매했습니다.",
                            "[8, 21, 23, 41, 42, 43]",
                            "[3, 5, 11, 16, 32, 38]",
                            "[7, 11, 16, 35, 36, 44]",
                            "[1, 8, 11, 31, 41, 42]",
                            "[13, 14, 16, 38, 42, 45]",
                            "[7, 11, 30, 40, 42, 43]",
                            "[2, 13, 22, 32, 38, 45]",
                            "[1, 3, 5, 14, 22, 45]",
                            "3개 일치 (5,000원) - 1개",
                            "4개 일치 (50,000원) - 0개",
                            "5개 일치 (1,500,000원) - 0개",
                            "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                            "6개 일치 (2,000,000,000원) - 0개",
                            "총 수익률은 62.5%입니다."
                    );
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45),
                List.of(1, 3, 5, 14, 22, 45)
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("1000j");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Test
    void 로또번호_중복_Error1() {
        assertSimpleTest(() -> {
            run("5000", "1,2,3,3,5,6", "7");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Test
    void 로또번호_중복_Error2() {
        assertSimpleTest(() -> {
            run("5000", "1,2,3,4,5,6", "4");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Test
    void 로또번호_범위_Error() {
        assertSimpleTest(() -> {
            run("5000", "0,2,3,4,5,6", "7");
            assertThat(output()).contains(ERROR_MESSAGE);

            run("5000", "1,2,3,4,5,46", "7");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Test
    void 로또번호_갯수_Error() {
        assertSimpleTest(() -> {
            runException("5000", "1,2,3,4,5", "7");
            assertThat(output()).contains(ERROR_MESSAGE);

            runException("5000", "1,2,3,4,5,6,7", "7");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @ParameterizedTest
    @MethodSource("provideNumbersForTest")
    @DisplayName("로또 당첨 결과 테스트")
    void 로또_당첨_등수_확인(String purchaseAmount, String winningNumbers, String bonusNumber, String expectedOutput) {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run(purchaseAmount, winningNumbers, bonusNumber);
                    assertThat(output()).contains(expectedOutput);
                },
                List.of(1, 2, 3, 4, 5, 6)
        );
    }

    static Stream<Arguments> provideNumbersForTest() {
        return Stream.of(
                Arguments.of("1000", "1,2,3,43,44,45", "7", "3개 일치 (5,000원) - 1개"),
                Arguments.of("1000", "1,2,3,4,44,45", "7", "4개 일치 (50,000원) - 1개"),
                Arguments.of("1000", "1,2,3,4,5,45", "7", "5개 일치 (1,500,000원) - 1개"),
                Arguments.of("1000", "1,2,3,4,5,45", "6", "5개 일치, 보너스 볼 일치 (30,000,000원) - 1개"),
                Arguments.of("1000", "1,2,3,4,5,6", "7", "6개 일치 (2,000,000,000원) - 1개")

        );
    }

    @Test
    void 수익률_반올림_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("3000", "1,2,3,4,5,6", "7");
                    try {
                        assertThat(output()).contains(ERROR_MESSAGE);
                    } catch (AssertionError e) {
                        assertThat(output()).contains("총 수익률은 166.7%입니다.");
                    }
                },
                List.of(1, 2, 3, 43, 44, 45),
                List.of(40, 41, 42, 43, 44, 45),
                List.of(40, 41, 42, 43, 44, 45)
        );
    }

    @Test
    @DisplayName("죽지도 않고 돌아온 정수 오버플로우")
    void OverflowTest() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("2000", "1,2,3,4,5,6", "7");
                    try {
                        assertThat(output()).contains(ERROR_MESSAGE);
                    } catch (AssertionError e) {
                        assertThat(output()).contains("6개 일치 (2,000,000,000원) - 2개");
                        assertThat(output()).contains("총 수익률은 200000000.0%입니다.");
                    }
                },
                List.of(1, 2, 3, 4, 5, 6),
                List.of(1, 2, 3, 4, 5, 6),
                List.of(1, 2, 3, 4, 5, 6)
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}