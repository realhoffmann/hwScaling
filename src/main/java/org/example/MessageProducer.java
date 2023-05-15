package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageProducer {
    private static final String QUEUE_NAME = "TextAnalysisQueue";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                // Messages to be processed
                String[] messages = {
                        "Hello World!",
                        "My name is Jim",
                        "Test"
                };

                for (String message : messages) {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println("Sent message: " + message);
                    Thread.sleep(message.length() * 1000); // Sleep based on string length
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
