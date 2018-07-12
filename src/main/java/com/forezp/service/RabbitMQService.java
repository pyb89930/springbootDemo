package com.forezp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RabbitMQService {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

	@Resource
	private RabbitTemplate rabbitTemplate;

	public void send(String exchange, String routingKey, String msg) throws Exception {
		try {
			rabbitTemplate.convertAndSend(exchange, routingKey, msg);
			logger.info("推送消息:{}", msg);
		} catch(Exception e){
			logger.error("推送消息异常，msg:" + msg, e);
			throw e;
		}
	}
}
