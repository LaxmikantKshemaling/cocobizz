package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.InventoryDto;
import java.util.List;

public interface InventoryService {

    List<InventoryDto> getInventory();
}