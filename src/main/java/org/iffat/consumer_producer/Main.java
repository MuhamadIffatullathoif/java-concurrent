package org.iffat.consumer_producer;

import java.util.Random;

class MessageRepository {
	private String message;
	private boolean hasMessage = false;

	public synchronized String read() {

		while (!hasMessage) {

		}
		hasMessage = false;
		return message;
	}

	public synchronized void write(String message) {

		while (hasMessage) {

		}
		hasMessage = true;
		this.message = message;
	}
}

class MessageWriter implements Runnable {

	private MessageRepository messageRepository;
	private final String text = """
			Humpty Dumpty sat on wall,
			Humpty Dumpty had a great fall,
			All the king's horses and all the king's men,
			Couldn't put Humpty together again.""";

	public MessageWriter(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public void run() {

		Random random = new Random();
		String[] lines = text.split("\n");

		for (int i = 0; i < lines.length; i++) {
			messageRepository.write(lines[i]);
			try {
				Thread.sleep(random.nextInt(500, 2000));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		messageRepository.write("Finished");
	}
}

class MessageReader implements Runnable {

	private MessageRepository messageRepository;

	public MessageReader(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public void run() {

		Random random = new Random();
		String lastMessage = "";

		do {
			try {
				Thread.sleep(random.nextInt(500, 2000));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			lastMessage = messageRepository.read();
			System.out.println(lastMessage);
		} while (!lastMessage.equals("Finished"));
	}
}

public class Main {

	public static void main(String[] args) {

		MessageRepository messageRepository = new MessageRepository();

		Thread reader = new Thread(new MessageReader(messageRepository));
		Thread writer = new Thread(new MessageWriter(messageRepository));

		reader.start();
		writer.start();
	}
}
