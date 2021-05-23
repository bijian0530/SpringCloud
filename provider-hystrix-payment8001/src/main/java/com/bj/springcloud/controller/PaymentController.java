package com.bj.springcloud.controller;

import com.bj.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")

public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String  paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentService.paymentOnfo_OK(id);
        log.info("===result:"+result );
        return result;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
//    @HystrixCommand
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id)
    throws InterruptedException{
        String result = paymentService.paymentInfo_TimeOut(id);
        log.info("***result:"+result);
        return result;
    }

//    public String payment_Global_FallbackMethod(){
//        return "Global 异常处理信息，请稍后在进行尝试";
//    }

    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id")Integer id){
        String result = paymentService.paymentCircuitBreaker(id);
        log.info("===result: "+result);
        return result;
    }
}
