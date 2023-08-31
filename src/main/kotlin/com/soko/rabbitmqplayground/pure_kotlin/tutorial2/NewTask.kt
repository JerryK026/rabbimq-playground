package com.soko.rabbitmqplayground.pure_kotlin.tutorial2

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties

const val QUEUE_NAME = "hello"

fun main(args: Array<String>) {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    factory.username = "user"
    factory.password = "password"
    factory.newConnection().use { connection ->
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        val message = args.joinToString(" ")
        // publisher에서 메시지를 전송할 때 영구 저장하도록 알린다
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toByteArray())
        println(" [x] Sent '$message'")
    }
}