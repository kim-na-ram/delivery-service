package com.countrym.deliveryservice.domain.store.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GetStoreListResponseDtoTest {

    @Test
    @DisplayName("0시에 오픈이 19시, 마감이 3시인 가게는 isOpen 이 true 여야 함")
    public void storeIsOpenTrueCase01() {
        LocalTime now = LocalTime.of(0, 0);
        LocalTime openAt = LocalTime.of(19, 0);
        LocalTime closedAt = LocalTime.of(3, 0);

        // when
        boolean isOpen = checkIsOpen(now, openAt, closedAt);

        // then
        assertTrue(isOpen);
    }

    @Test
    @DisplayName("18시에 오픈이 10시, 마감이 0시인 가게는 isOpen 이 true 여야 함")
    public void storeIsOpenTrueCase02() {
        LocalTime now = LocalTime.of(18, 0);
        LocalTime openAt = LocalTime.of(10, 0);
        LocalTime closedAt = LocalTime.of(0, 0);

        // when
        boolean isOpen = checkIsOpen(now, openAt, closedAt);

        // then
        assertTrue(isOpen);
    }

    @Test
    @DisplayName("7시에 오픈이 19시, 마감이 3시인 가게는 isOpen 이 false 여야 함")
    public void storeIsOpenFalseCase01() {
        LocalTime now = LocalTime.of(7, 0);
        LocalTime openAt = LocalTime.of(19, 0);
        LocalTime closedAt = LocalTime.of(3, 0);

        // when
        boolean isOpen = checkIsOpen(now, openAt, closedAt);

        // then
        assertFalse(isOpen);
    }

    @Test
    @DisplayName("20시에 오픈이 0시, 마감이 0시인 가게는 isOpen 이 true 여야 함")
    public void storeIsOpenTrueCase03() {
        LocalTime now = LocalTime.of(20, 0);
        LocalTime openAt = LocalTime.of(0, 0);
        LocalTime closedAt = LocalTime.of(0, 0);

        // when
        boolean isOpen = checkIsOpen(now, openAt, closedAt);

        // then
        assertTrue(isOpen);
    }

    @Test
    @DisplayName("6시에 오픈이 5시, 마감이 15시인 가게는 isOpen 이 true 여야 함")
    public void storeIsOpenTrueCase04() {
        LocalTime now = LocalTime.of(6, 0);
        LocalTime openAt = LocalTime.of(5, 0);
        LocalTime closedAt = LocalTime.of(15, 0);

        // when
        boolean isOpen = checkIsOpen(now, openAt, closedAt);

        // then
        assertTrue(isOpen);
    }

    private boolean checkIsOpen(LocalTime now, LocalTime openAt, LocalTime closedAt) {
        boolean isOpen;
        if (closedAt.getHour() <= 6 && now.getHour() <= 6) {
            // 만약 closedAt이 새벽 6시 이전인데, 현재 시각도 새벽 6시 이전이라면 (ex. 2시)
            // closedAt 이전인지 확인
            isOpen = now.isBefore(closedAt);
        } else if (closedAt.getHour() <= 6) {
            // 만약 closedAt이 새벽 6시 이전인데, 현재 시각이 새벽 6시보다 크다면 (ex. 11시)
            // openAt 이후인지 확인
            isOpen = now.isAfter(openAt);
        } else {
            // 만약 closedAt이 새벽 6시 이후라면
            isOpen = !now.isBefore(openAt) && !now.isAfter(closedAt);
        }

        return isOpen;
    }


}