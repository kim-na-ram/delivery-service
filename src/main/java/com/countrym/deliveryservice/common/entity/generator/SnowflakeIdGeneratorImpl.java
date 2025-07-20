package com.countrym.deliveryservice.common.entity.generator;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnowflakeIdGeneratorImpl implements IdentifierGenerator {
    private final SnowflakeIdGenerator Snowflake;

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return Snowflake.nextId();
    }
}