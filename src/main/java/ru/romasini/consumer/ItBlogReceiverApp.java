package ru.romasini.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ItBlogReceiverApp {

    public static final String EXCHANGE_NAME = "it-blog";

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel= connection.createChannel();
        BufferedReader readerConsole = new BufferedReader(new InputStreamReader(System.in));
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
        String queueName = channel.queueDeclare().getQueue();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

        while (true) {
            System.out.println("Введите действие: 1- подписаться, 2- отписаться, exit - выйти");
            String temp = readerConsole.readLine();

            if (temp.equals("exit")) {

                break;
            }else if(temp.equals("1")){
                System.out.println("Введите ЯП");
                String lang = readerConsole.readLine();
                System.out.println(" queueName " + queueName);
                channel.queueBind(queueName, EXCHANGE_NAME, lang);

            }else if(temp.equals("2")){
                System.out.println("Введите ЯП");
                String lang = readerConsole.readLine();
                channel.queueUnbind(queueName, EXCHANGE_NAME, lang);
            }


        }

        connection.close();
        readerConsole.close();
    }
}
