/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Camunda License 1.0. You may not use this file
 * except in compliance with the Camunda License 1.0.
 */
package io.camunda.optimize.service.db.os.report.filter;

import static io.camunda.optimize.service.db.schema.index.ProcessInstanceIndex.END_DATE;

import io.camunda.optimize.dto.optimize.query.report.single.filter.data.date.DateFilterDataDto;
import io.camunda.optimize.service.db.filter.FilterContext;
import io.camunda.optimize.service.db.os.report.filter.util.DateFilterQueryUtilOS;
import io.camunda.optimize.service.util.configuration.condition.OpenSearchCondition;
import java.util.List;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(OpenSearchCondition.class)
public class InstanceEndDateQueryFilterOS implements QueryFilterOS<DateFilterDataDto<?>> {

  public InstanceEndDateQueryFilterOS() {}

  @Override
  public List<Query> filterQueries(
      final List<DateFilterDataDto<?>> filters, final FilterContext filterContext) {
    return DateFilterQueryUtilOS.filterQueries(filters, END_DATE, filterContext.getTimezone());
  }
}
