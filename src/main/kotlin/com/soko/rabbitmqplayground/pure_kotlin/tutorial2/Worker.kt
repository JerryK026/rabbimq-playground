package com.soko.rabbitmqplayground.pure_kotlin.tutorial2

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery

fun main() {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    factory.username = "user"
    factory.password = "password"
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    // 서버가 재시작되면 queue가 되살아나도록 한다
    val durability = true
    channel.queueDeclare(QUEUE_NAME, durability, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    // 한 번에 하나의 unack 메시지를 처리할 수 있도록 한다
    channel.basicQos(1)
    val deliverCallback = {consumerTag: String, delivery: Delivery ->
        val message = String(delivery.body, charset("UTF-8"))
        println(" [x] Received '$message'")

        try {
            doWork()
        } finally {
            println(" [x] Done")
            // 메시지 처리 이후 ack를 보내 queue에서 메시지를 삭제할 수 있도록 한다
            channel.basicAck(delivery.envelope.deliveryTag, false)
        }
    }
    val autoAck = false
    // autoAck=true : consumer에 메시지가 전달되기만 하면 바로 ack를 보내, queue에서 삭제한다
    // consumer가 메시지를 처리하지 못하고 죽으면(채널이 닫히거나, 연결이 끊어지거나, TCP 연결이 끊어지면) 메시지가 유실된다
    // 메시지 유실이 걱정된다면 autoAck=false로 두고 basicAck를 추가하는 것이 낫다
    channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback) { _ -> }
}

// CPU intensive한 작업을 시뮬레이션하기 위해 fake task를 생성한다.
fun doWork() {
    Thread.sleep(10000)
}

