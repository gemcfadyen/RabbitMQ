package workqueues;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

	public static final String WORK_QUEUE = "WORK_QUEUE";

	public static void main(String[] args) throws IOException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();

		Channel channel = connection.createChannel();
		channel.queueDeclare(WORK_QUEUE, isADurable(), false, false, null);
		
		String[] messagesThatTakeSomeTimeToProcess = new String[] { "hi.",
				"Georgina..", "bye...", "another..", "lovely......", "message..." };

		/*
		 * Send a messages in one at a time - when there are multiple Workers running, a round-robin approach is taken.
		 * RabbitMQ will route the next message to the next free Consumer.
		 */
		for (String individualMessage : messagesThatTakeSomeTimeToProcess) {
			channel.basicPublish(defaultExchange(), WORK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, individualMessage.getBytes());
			System.out.println("[x] sent '" + individualMessage + "'");
		}
		
		channel.close();
		connection.close();
	}

	private static String defaultExchange() {
		return "";
	}

	private static boolean isADurable() {
		return true;
	}

}
