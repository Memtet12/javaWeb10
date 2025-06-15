package com.example.messagingrabbitmq;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	Scanner scanner = new Scanner(System.in);
	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Для выхода напишите exit");
		System.out.println("Введите ваше имя");
		String name = scanner.nextLine();
		while (true)
		{
			String message = scanner.nextLine();
			if (message.equals("exit"))
			{
				rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.fanoutExchangeName,"", name + " покинул(а) чат" );
				break;
			}
			rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.fanoutExchangeName,"", name + ": " + message );
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		}
	}

}
