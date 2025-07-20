package com.countrym.deliveryservice.common.entity.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SnowflakeIdGeneratorTest {
    private final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator();

    // Snowflake Id 를 통해 생성되는 ID 들이 겹치지 않는지 테스트
    @Test
    void idGenerationTest() throws InterruptedException {
        Set<Long> idList = Collections.synchronizedSet(new HashSet<>());

        int executeCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        for (int i = 0; i < executeCount; i++) {
            executorService.submit(() -> {
                idList.add(snowflakeIdGenerator.nextId());
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        int count = 0;
        Iterator<Long> iterator = idList.iterator();
        StringBuilder sb = new StringBuilder();
        while (count < 10 && iterator.hasNext()) {
            sb.append(iterator.next()).append("\n");
            count++;
        }

        System.out.println(sb);

        assertEquals(executeCount, idList.size());
    }
}