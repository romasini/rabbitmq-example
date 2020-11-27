package ru.romasini.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class ItBlogReceiverApp {

    public static final String EXCHANGE_NAME = "it-blog";

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel= connection.createChannel();
        BufferedReader readerConsole = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите язык программирования для подписки");
        String lang = readerConsole.readLine();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
        String queueName = channel.queueDeclare().getQueue();
        System.out.println(" queueName " + queueName);
        channel.queueBind(queueName, EXCHANGE_NAME, lang);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
           String message = new String(delivery.getBody(), "UTF-8");
           System.out.println(" [x] Received '" + message + "'" );
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
