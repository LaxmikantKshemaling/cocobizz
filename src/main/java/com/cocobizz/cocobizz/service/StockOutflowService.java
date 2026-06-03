package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.StockOutflowDto;

import java.util.List;

public interface StockOutflowService {

    List<StockOutflowDto> getAllOutflows();

    StockOutflowDto getOutflowById(Long id);

}