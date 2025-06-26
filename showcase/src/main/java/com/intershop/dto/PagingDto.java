package com.intershop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingDto {

    private int pageSize;
    private boolean hasPrevious;
    private boolean hasNext;
    private int pageNumber;

}
