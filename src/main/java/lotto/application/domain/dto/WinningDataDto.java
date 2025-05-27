package lotto.application.domain.dto;

import lotto.application.domain.enums.LottoPrize;

public record WinningDataDto(LottoPrize lottoPrize, int count) {
}