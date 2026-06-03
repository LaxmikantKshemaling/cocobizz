package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.FinanceDto;
import com.cocobizz.cocobizz.dto.ProfitGraphDto;

import java.util.List;

public interface FinanceService {

    FinanceDto getFinanceData();

    List<ProfitGraphDto> getProfitGraph(String type);
}