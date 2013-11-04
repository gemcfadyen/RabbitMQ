RabbitMQ
========

Repository for playing with RabbitMQ - research for Frankies Chat Bot project

To install RabbitMQ follow the instructions at: http://www.rabbitmq.com/install-windows-manual.html
To start the server use the command rabbitmq-server.bat (if you have added RABBIT_MQ_HOME to your path), otherwise the script can be found in the sbin directory of your installation.

To check the status of the running server use the command rabbitmqctl.bat status. If the server is running this will return the process id (pid) and some stats.

To stop the sever running use rabbitmqctl.bat stop.

Note: By default RabbitMQ will install as a Service so it will be set to run all the time. You can change this through your ControlPanel so that you can start it on demand only.

This repository goes through the tutorials detailed at : http://www.rabbitmq.com/getstarted.html to enable me to get the look and feel of this API.


HelloWorld Example
------------------
* The Sender or Receiver can be started in any order.
* Because of this, both must go through the motion of creating a connection factory and a queue. 
* RabbitMQ will only create this actual queue if it doesnt already exist.

* Messages are sent asynchronously.

* The sender will publish a message (byte[]) onto the queue.

* The receiver has a Consumer which is listening to the same queue and obtains the message by calling nextDelivery(). From this it can extract the byte[] which is the message.

WorkQueues Example
------------------
* This program mimics messages being sent that take a bit longer to process. Each '.' in the message causes the running thread to sleep for one second. 

* Messages are sent asynchronously so if you only start NewTask (which publishes some messages), then later on start the Worker (which consumes the messages), the worker will get the messages (as long as the server has not been stopped in between).

* If multiple workers are running, RabbitMQ will route the message received to the next available Consumer, in a Round-Robin way.

* To preserve the messages on the queue even if the RabbitMQ server goes down, the durable parameter on the queueDeclare method must be set to true. Additionally when the messages are published, they must be set as persistant. This is acheived through the MessageProperty.PERSIST_AS_TEXT_MESSAGE property passed into the basicPublish method on the Channel.

PubSub Example
--------------
* So that multiple consumers can get a message, an exchange must be used.
* When using the exchange, each Consumer dynamically creates a queue bound to the exchange where the messages are being sent.
* When that consumer stops listening, the dynamic queue is automatically deleted.
* To see a list of exchange bindings use the command rabbitmqctl.bat list_bindings
* To see the behaviour, run several Subscribers, then run the Publisher. You will observe that each subscriber receives the message that has been sent.


Routing Example
---------------
* A Routing key is used so that multiple consumers can listen to an exchange for messages, but be selective about the messages they receive.
* Similar to the last example, if several Receivers are running, they will all receive the messages that match their routing key.
* One queue can have several routing keys bound to it. For example in ReceiveLowPriority.java - the routing keys 'low' and 'none' are bound. 
