package com.bj.springcloud.controller;

import com.bj.springcloud.entities.CommonResult;
import com.bj.springcloud.entities.Payment;
import com.bj.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Autowired
    private PaymentService paymentService;
    @PostMapping("/payment/create")
    public @ResponseBody CommonResult create( Payment payment){
        int result = paymentService.create(payment);
        log.info("===插入操作返回结果:"+result);
        if(result > 0){
            return new CommonResult(200,"插入数据成功,serverPort:"+serverPort,result);
        }else {
            return new CommonResult(404,"插入数据失败",null);
        }
    }
    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("查询的结果:"+payment);
        if(payment != null){
           return new CommonResult(200,"查询成功,serverPort:"+serverPort,payment);
       }else{
           return new CommonResult(404,"查询失败的",payment);
        }
//        return new CommonResult(404,"查询失败的",payment);
    }
    @GetMapping(value = "/payment/lb")
    public String getPaymentLB()
    {
        return serverPort;
    }


    @GetMapping(value = "/payment/discovery")
    public Object discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.discoveryClient;
    }
    @GetMapping("/payment/feign/timeout")
    public String  paymentFeignTimeOut(){
        System.out.println("*****paymentFeignTimeOut from port: "+serverPort);
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); }
            catch (InterruptedException e) {
            e.printStackTrace(); }
        return serverPort;

    }

}
