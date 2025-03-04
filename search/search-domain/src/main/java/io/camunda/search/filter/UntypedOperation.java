/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Camunda License 1.0. You may not use this file
 * except in compliance with the Camunda License 1.0.
 */
package io.camunda.search.filter;

import static io.camunda.util.ValueTypeUtil.mapValueType;

import io.camunda.search.entities.ValueTypeEnum;
import io.camunda.util.ValueTypeUtil;
import java.util.List;

/**
 * For variables, the value type is always string, although a numeric operation should be done. This
 * leads to problems for the rdbms implementation, this class is used to convert the values to the
 * correct type.
 *
 * @param operator
 * @param values
 * @param type
 */
public record UntypedOperation(Operator operator, List<Object> values, ValueTypeEnum type) {

  public static UntypedOperation of(final Operation<?> operation) {
    final List<Object> typedValues;
    if (operation.values() != null) {
      typedValues =
          operation.values().stream()
              .map(it -> mapValueType(it, ValueTypeUtil.getValueType(it)))
              .toList();
    } else {
      typedValues = null;
    }

    final ValueTypeEnum valueType = ValueTypeUtil.getValueType(operation.value());
    validateOrThrow(operation.operator(), valueType);
    return new UntypedOperation(operation.operator(), typedValues, valueType);
  }

  public Object value() {
    return values.getFirst();
  }

  @Override
  public List<Object> values() {
    return values;
  }

  private static void validateOrThrow(final Operator operator, final ValueTypeEnum type) {
    switch (operator) {
      case GREATER_THAN:
      case GREATER_THAN_EQUALS:
      case LOWER_THAN:
      case LOWER_THAN_EQUALS:
        if (type != ValueTypeEnum.LONG && type != ValueTypeEnum.DOUBLE) {
          throw new IllegalArgumentException(
              "Unsupported value type for number operator (<,<=,>,>=): " + type);
        }
        break;
      case LIKE:
        if (type != ValueTypeEnum.STRING) {
          throw new IllegalArgumentException("Unsupported value type for like operator: " + type);
        }
        break;
      default:
        break;
    }
  }
}
