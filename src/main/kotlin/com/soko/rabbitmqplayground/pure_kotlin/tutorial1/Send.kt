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