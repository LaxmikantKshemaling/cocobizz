package com.cocobizz.cocobizz.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public JSONObject createOrder(Double amount) {

        try {

            if (amount == null || amount <= 0) {
                throw new RuntimeException("Invalid amount ❌");
            }

            RazorpayClient client = new RazorpayClient(key.trim(), secret.trim());

            JSONObject options = new JSONObject();

            // 🔥 IMPORTANT: Razorpay expects paise
            options.put("amount", (int) (amount * 100));
            options.put("currency", "INR");
            options.put("receipt", "order_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            return new JSONObject(order.toString());

        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay error: " + e.getMessage(), e);
        }
    }
}