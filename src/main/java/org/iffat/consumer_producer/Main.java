package org.iffat.consumer_producer;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MessageRepository {
	private String message;
	private boolean hasMessage = false;
	private final Lock lock = new ReentrantLock();

	public String read() {

		lock.lock();
		try {
			while (!hasMessage) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		} finally {
			lock.unlock();
		}
		hasMessage = false;
		return message;
	}

	public synchronized void write(String message) {

		while (hasMessage) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		hasMessage = true;
		notifyAll();
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

		writer.setUncaughtExceptionHandler((thread, exc) -> {
			System.out.println("Writer had exception: " + exc);
			if (reader.isAlive()) {
				System.out.println("Going to interrupt the reader");
				reader.interrupt();
			}
		});

		reader.setUncaughtExceptionHandler((thread, exc) -> {
			System.out.println("Reader had exception: " + exc);
			if (writer.isAlive()) {
				System.out.println("Going to interrupt the writer");
				writer.interrupt();
			}
		});
		reader.start();
		writer.start();
	}
}
