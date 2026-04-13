package com.lastcalleats.order.factory;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.order.entity.OrderDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for pickup code generators.
 */
@Component
public class PickupCodeFactory {

    private final Map<String, PickupCodeGenerator> generators;

    // The list has objects like NumericCodeGenerator and QRCodeGenerator.
    // It is changed into a map, where the key is the type and the value is the generator.
    public PickupCodeFactory(List<PickupCodeGenerator> generators) {
        this.generators = generators.stream()
                .collect(Collectors.toMap(PickupCodeGenerator::type, Function.identity()));
    }


    /**
     * Generates a pickup code with the given type.
     *
     * @param order target order
     * @param type generator type
     * @return generated pickup code
     */
    public String generate(OrderDO order, String type) {
        // Pick the generator by type.
        PickupCodeGenerator generator = generators.get(type);
        if (generator == null) {
            throw new BusinessException(ErrorCode.PICKUP_CODE_INVALID, "Unsupported pickup code type: " + type);
        }
        return generator.generate(order);
    }
}
