package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.StockInflowDto;
import java.util.List;

public interface StockInflowService {

    StockInflowDto createRequest(StockInflowDto dto, Long currentUserId);

    List<StockInflowDto> getAllRequests(Long currentUserId);

    List<StockInflowDto> getRequestsByFarmer(Long farmerId, Long currentUserId);

    StockInflowDto approveStock(Long id, Long currentUserId);

    void rejectStock(Long id, Long currentUserId);
}