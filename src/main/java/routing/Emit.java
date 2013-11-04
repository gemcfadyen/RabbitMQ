package routing;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Emit {
	public static final String DIRECT_EXCHANGE = "DIRECT_EXCHANGE";
	public static final String DIRECT_EXCHANGE_TYPE = "direct";

	public static void main(String[] args) throws IOException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(DIRECT_EXCHANGE, DIRECT_EXCHANGE_TYPE);

		emit("Hello Routing World", "high", channel);
		emit("Hello low", "low", channel);
		emit("Hello no priority", "none", channel);	
		emit("Hello high", "high", channel);

		channel.close();
		connection.close();
	}

	private static void emit(String message, String severity, Channel channel) throws IOException {
		channel.basicPublish(DIRECT_EXCHANGE, severity, null, message.getBytes());
		System.out.println("[x] sent message '" + message + "  " + severity + "'");
	}
}
