# 튜토리얼 1
목표 : RabbitMQ를 사용해 메시지를 보내고 받는다

참조
- tutorial 1 : https://www.rabbitmq.com/tutorials/tutorial-one-java.html
- api guide : https://www.rabbitmq.com/api-guide.html

Send : publisher  
Recv : consumer

# 알아야 하는 개념
Producer : 메시지를 보내는 프로그램  
![producer](../../images/producer.png)  

Queue : RabbiMQ에서 메시지를 보관하는 버퍼. 메시지는 큐에 저장되고, 컨슈머가 큐에서 메시지를 가져와서 처리한다.   
- 우체통에 편지를 넣어놓으면 편지 받은 사람이 가져가는 것과 같다  

![queue](../../images/queue.png)  

Consumer : 메시지를 받기 위해 기다리는 프로그램  
![consumer](../../images/consumer.png)  

메시지 전송도  
![producer to consumer](../../images/producer2consumer.png)

# 본문

## Sending And Receiving Messages

java client를 활용해서 RabbitMQ에 메시지를 보내고 받을 수 있다. 이를 구현하려면 다음 개념들에 대해 알아야 한다.

```
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
```

위와 같은 모듈들이 필요하다. 각 클래스들이 하는 역할들은 다음과 같다.
- ConnectionFactory : RabbitMQ 노드에 대한 Connection을 추상화하는 편의성 팩토리 클래스. 연결 및 소켓 설정할 때 사용한다.
  - port, username, password, vhost, host, heartbeat, channel_max, frame_max, ssl_port, timeout 등 설정 가능
- Connection : AMQ 커넥션에 대한 인터페이스. 브로커와 연결할 때는 ConnectionFactory를 사용해서 연결한다
  - 즉, Connection을 생성하려면 ConnectionFactory을 사용하면 된다.
  - Spec : https://www.amqp.org/
- Channel : 스레드들 간 공유되어선 안되고 스레드당 할당해서 사용해야 한다.
  - 사전적 의미 : 정보를 전달해 주는 통로

프로토콜 작업은 Channel 인터페이스로 이루어진다. Connection은 channel을 열고, connection 라이프사이클 이벤트 핸들러를 등록하고, 더 필요 없으면 연결을 해제한다.
따라서 API 클래스의 핵심 클래스는 Connection과 Channel이다. (AMQP-0-9-1)

Recv
```kotlin
package com.soko.rabbitmqplayground.pure_kotlin.tutorial1

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery

fun main() {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    factory.username = "user"
    factory.password = "password"
    // try-with-resources를 사용하지 않는 이유 : consumer는 도착할 메시지를 비동기적으로 기다려야 하므로 연결 해제하지 않는다
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    // consumer에서 queue를 바라본다
    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    // queue에서 받아온 메시지를 처리하는 callback을 정의한다. DeliverCallback 서브클래스는 메시지를 사용할 준비가 될때까지 버퍼링하는 역할도 한다
    val deliverCallback = {consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, charset("UTF-8"))
        println(" [x] Received '$message'")
    }

    // queue에 callback을 등록한다
    channel.basicConsume(QUEUE_NAME, true, deliverCallback) { _ -> }
}
```

Send
```kotlin
package com.soko.rabbitmqplayground.pure_kotlin.tutorial1

import com.rabbitmq.client.ConnectionFactory

const val QUEUE_NAME = "hello"

fun main() {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    factory.username = "user"
    factory.password = "password"
    // connection : 소켓 connection을 추상화해서 protocol version negotiation, 인증 등을 대신 처리한다
    factory.newConnection().use { connection ->
        // channel : 대부분의 API가 존재
        val channel = connection.createChannel()

        // 메시지를 publish하려면 queue가 필요하다
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        val message = "Hello World!"
        // https://www.rabbitmq.com/publishers.html#message-properties
//        val props = AMQP.BasicProperties().builder()
//            .contentType("application/json")
//            .deliveryMode(2)
//            .priority(1)
//            .userId("bob")
//            .build()
        // publisher guide : https://www.rabbitmq.com/publishers.html
        // 메시지를 전송할 때는 byteArray로 만들어 보낸다
        channel.basicPublish("", QUEUE_NAME, null, message.toByteArray())
        println(" [x] Sent '$message'")
    }
}
```

RabbitMQ에 메시지가 얼마나 들어있는지 확인할 수 있는 방법
```bash
sudo rabbitmqctl list_queues
```

위 과정에서는 rabbitMQ 클라이언트를 사용했지만, JMS Client for RabbitMQ를 사용할 수도 있다
- https://www.rabbitmq.com/jms-client.html