package org.example;
import com.rabbitmq.client.*;

public class MessageReceiver {
    private static final String QUEUE_NAME = "TextAnalysisQueue";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            System.out.println("Waiting for messages...");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body);
                    System.out.println("Received message: " + message);

                    try {
                        // Apply text analysis services
                        String reversed = new StringBuilder(message).reverse().toString();
                        Thread.sleep(reversed.length() * 1000);

                        String deleted = "";
                        for (int i = 0; i < reversed.length(); i++) {
                            if (i % 2 == 0) {
                                deleted += reversed.charAt(i);
                            }
                        }
                        Thread.sleep(deleted.length() * 1000);

                        String finalOutput = deleted.replaceAll("\\s", "");
                        Thread.sleep(finalOutput.length() * 1000);

                        System.out.println("Final output: " + finalOutput);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
