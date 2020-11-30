package ru.romasini.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ItBlogProducerApp {

    public static final String EXCHANGE_NAME = "it-blog";

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        BufferedReader readerConsole = new BufferedReader(new InputStreamReader(System.in));
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);

        while (true){
               System.out.println("Введите сообщение:");
                String message = readerConsole.readLine();
                if(message.equals("/end")) break;
                System.out.println("Введите язык программирования:");
                String lang = readerConsole.readLine();
                channel.basicPublish(EXCHANGE_NAME, lang, null, message.getBytes("UTF-8"));
                System.out.println("СООБЩЕНИЕ ОТПРАВЛЕНО");
        }

        connection.close();
        readerConsole.close();
    }

}
