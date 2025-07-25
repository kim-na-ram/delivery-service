package com.countrym.deliveryservice.common.util;

import javax.inject.Singleton;
import java.time.LocalDateTime;

@Singleton
public class TimeFormatter {
    public static String convertToString(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.getYear() == now.getYear()) {
            if (localDateTime.getMonth() == now.getMonth()) {
                if (localDateTime.getDayOfMonth() == now.getDayOfMonth()) {
                    return "오늘";
                }
            }
            return String.format("%tB %te일", localDateTime, localDateTime);
        } else return String.format("%tY 년 %tm %te일", localDateTime, localDateTime, localDateTime);
    }
}
