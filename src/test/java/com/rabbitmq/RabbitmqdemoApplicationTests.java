package com.rabbitmq;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.dao.EmployeesMapper;
import com.rabbitmq.entity.Employees;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqdemoApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private EmployeesMapper empDao;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Test
	public void contextLoads() {
		HashMap<String, Object> stringObjectHashMap = new HashMap<>();
		stringObjectHashMap.put("user","admin");
		stringObjectHashMap.put("pass","123456");
		rabbitTemplate.convertAndSend("amq.direct",null,stringObjectHashMap);
	}
	@Test
	public void getMessage(){
		Object message = rabbitTemplate.receiveAndConvert("qipingzhiji");
		System.out.println(message);
	}

	@Test
	public void getEmps(){
		List<Employees> employees = empDao.selectAll();
		for (Employees employee : employees) {
			rabbitTemplate.convertAndSend("amq.direct",null,employees);
		}
	}


	@Test
	public void createExchange(){
		amqpAdmin.declareQueue(new Queue("emps.emp"));
		TopicExchange topicExchange = new TopicExchange("emp.topic",false,false);
		amqpAdmin.declareExchange(topicExchange);
		Binding binding = new Binding("emps.emp", Binding.DestinationType.QUEUE,"emp.topic","emps.*",null);
		amqpAdmin.declareBinding(binding);
	}

	@Test
	public void receiveEmpsMessage(){

		Object o = rabbitTemplate.receiveAndConvert("emps.emp");

		System.out.println(o.toString());
	}

	@Test
	public void pullEmpTopicExchange(){
		List<Employees> employees = empDao.selectAll();
		for (Employees employee : employees) {
			rabbitTemplate.convertAndSend("emp.topic","emps.emp",employee);
		}
	}


}
