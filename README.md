# rabbimq-playground

# docker image
https://registry.hub.docker.com/_/rabbitmq/

```bash
docker build -t my-rabbitmq .
docker run -d --name my-rabbitmq-instance -p 5672:5672 -p 15672:15672 my-rabbitmq
```