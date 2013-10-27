package workqueues;


import static workqueues.NewTask.WORK_QUEUE;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * This mimics messages being sent to and from a receiver, where the messages
 * are actually tasks that will take some time to complete. The
 * 'mimicATimeConsumingTaskBeingExecuted' method spoofs this by sleeping for a
 * few seconds thus faking work being completed.
 * 
 * @author Georgina
 * 
 */
public class Worker {

	public static void main(String[] args) throws IOException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();

		Channel channel = connection.createChannel();
		channel.queueDeclare(WORK_QUEUE, isADurable(), false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(WORK_QUEUE, isNotConfiguredToRedistributeMessagesIfWorkerGoesDown(), consumer); 
		//This Qos property allocates the next message to the next free Worker
		channel.basicQos(1);
		
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("[y] received the message '" + message + "'");
			mimicATimeConsumingTaskBeingExecuted(message);
			System.out.println("[y] has completed its work");
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

	}

	private static boolean isNotConfiguredToRedistributeMessagesIfWorkerGoesDown() {
		return false;
	}

	private static boolean isADurable() {
		return true;
	}

	/*
	 * For each '.' in the input messages, the thread sleeps for one second,
	 * mimicking different amounts of work being done.
	 */
	private static void mimicATimeConsumingTaskBeingExecuted(String task)
			throws InterruptedException {

		for (char charactersInMessage : task.toCharArray()) {
			if (charactersInMessage == '.') {
				System.out.println("Sleeping for a second");
				Thread.sleep(1000);
				System.out.println("Alive again");
			}
		}
	}
}
