package pubsub;

import static pubsub.Publisher.FANOUT_EXCHANGE_TYPE;
import static pubsub.Publisher.LOGS_EXCHANGE_NAME;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Subscriber {

	public static void main(String[] args) throws IOException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(LOGS_EXCHANGE_NAME, FANOUT_EXCHANGE_TYPE);
		// declare a default queue - the server will generate a unique name,
		// it will not be durable and it will be deleted if there is no consumer
		// using it
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("The dynamically created queue is: " + queueName);
		channel.queueBind(queueName, LOGS_EXCHANGE_NAME, "");
		
		System.out.println("[x] waiting for messages...");
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}

	}
}
