package routing;

import static routing.Emit.DIRECT_EXCHANGE;
import static routing.Emit.DIRECT_EXCHANGE_TYPE;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveHighSeverity {

	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(DIRECT_EXCHANGE, DIRECT_EXCHANGE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("The dynamically created queue is: " + queueName);

		channel.queueBind(queueName, DIRECT_EXCHANGE, "high");

		System.out.println("Waiting for high priority messages...");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();
			System.out.println("message: " + message + " and severity: " + routingKey);
		}
	}
}
