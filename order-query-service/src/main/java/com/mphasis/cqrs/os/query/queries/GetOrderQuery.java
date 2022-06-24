package com.mphasis.cqrs.os.query.queries;

import com.mphasis.cqrs.os.query.utils.SortOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOrderQuery {
    @Builder.Default
    Integer months = 3;
    @Builder.Default
    Integer page = 1;
    @Builder.Default
    Integer size = 10;
    @Builder.Default
    SortOrder sortOrder = SortOrder.DESC;

}
