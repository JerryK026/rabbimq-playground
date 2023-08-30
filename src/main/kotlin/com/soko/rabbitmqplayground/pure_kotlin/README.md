# 튜토리얼 1
참조
- tutorial 1 : https://www.rabbitmq.com/tutorials/tutorial-one-java.html
- api guide : https://www.rabbitmq.com/api-guide.html

Send : publisher  
Recv : consumer

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
  - 즉, Connection을 생성하려면 ConnectionFactory을 사용하면 됨.
  - Spec : https://www.amqp.org/
- Channel : 스레드들 간 공유되어선 안되고 스레드당 할당해서 사용해야 한다.
  - 사전적 의미 : 정보를 전달해 주는 통로

프로토콜 작업은 Channel 인터페이스로 이루어진다. Connection은 channel을 열고, connection 라이프사이클 이벤트 핸들러를 등록하고, 더 필요 없으면 연결을 해제한다.
따라서 API 클래스의 핵심 클래스는 Connection과 Channel이다. (AMQP-0-9-1)

RabbitMQ에 메시지가 얼마나 들어있는지 확인할 수 있는 방법
```bash
sudo rabbitmqctl list_queues
```

위 과정에서는 rabbitMQ 클라이언트를 사용했지만, JMS Client for RabbitMQ를 사용할 수도 있다
- https://www.rabbitmq.com/jms-client.html