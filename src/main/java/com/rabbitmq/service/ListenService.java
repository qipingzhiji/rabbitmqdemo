package com.rabbitmq.service;

import com.rabbitmq.entity.Employees;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created by zhang_htao on 2019/8/25.
 */
@Service
public class ListenService {

    @RabbitListener(queues = {"qipingzhiji"})
    public void SysInfo(Employees employees){

        System.out.println(employees);

    }

    @RabbitListener(queues = "emps.emp")
    public void receiveEmps(Employees employees){
        System.out.println(employees.toString());
    }

}
