# rabbimq-playground

# docker image
https://registry.hub.docker.com/_/rabbitmq/

```bash
docker build -t my-rabbitmq .
docker run -d --name my-rabbitmq-instance -p 5672:5672 -p 15672:15672 my-rabbitmq
```

# 튜토리얼
- 튜토리얼1 : https://github.com/JerryK026/rabbitmq-playground/tree/main/src/main/kotlin/com/soko/rabbitmqplayground/pure_kotlin/tutorial1
- 튜토리얼2 : https://github.com/JerryK026/rabbitmq-playground/tree/main/src/main/kotlin/com/soko/rabbitmqplayground/pure_kotlin/tutorial2

# 참조 문서
- rabbitmq 공식 문서 : https://www.rabbitmq.com/documentation.html
- rabbitmq 공식 튜토리얼 : https://www.rabbitmq.com/getstarted.html
- AMQP 0-9-1 모델 : https://www.rabbitmq.com/tutorials/amqp-concepts.html
- RabbiqMQ JMS client : https://www.rabbitmq.com/jms-client.html