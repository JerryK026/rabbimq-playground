package com.soko.rabbitmqplayground.pure_kotlin

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