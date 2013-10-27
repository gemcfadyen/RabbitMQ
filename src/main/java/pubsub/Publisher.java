package pubsub;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {

	public static final String FANOUT_EXCHANGE_TYPE = "fanout";
	public static final String LOGS_EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException{
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(LOGS_EXCHANGE_NAME, FANOUT_EXCHANGE_TYPE);
		
		String messageToPublish = "Hello Georgina";
		channel.basicPublish(LOGS_EXCHANGE_NAME, "", null, messageToPublish.getBytes());
		
		System.out.println("[x] sent message '" + messageToPublish + "'");
		channel.close();
		connection.close();
	}
}
