package com.soko.rabbitmqplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RabbitmqPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<RabbitmqPlaygroundApplication>(*args)
}
