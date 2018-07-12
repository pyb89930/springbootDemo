package com.forezp.handler;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "q_obd_wifi_mac")
public class RabbitReveiveTestHandler {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("msg  : " + msg);
    }

}
