package com.bj.springcloud.service;

import com.bj.springcloud.entities.CommonResult;
import com.bj.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value="CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {
    @GetMapping("/payment/get/{id}")
     CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);
    @GetMapping("/payment/feign/timeout")
     String  paymentFeignTimeOut();
}
