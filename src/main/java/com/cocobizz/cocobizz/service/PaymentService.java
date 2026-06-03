package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import java.util.List;

public interface PaymentService {

    String payFarmer(FarmerPaymentDto dto);

    List<PaymentDto> getAllFarmerPayments();

    List<PaymentDto> getPaymentsByFarmer(Long farmerId);

    PaymentDto updatePayment(Long id, FarmerPaymentDto dto);

    void deletePayment(Long id);

    String payTransport(TransportPaymentDto dto);

    List<PaymentDto> getPaymentsByAdmin(Long adminId);
}