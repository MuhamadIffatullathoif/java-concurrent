package org.iffat.thread_problem;

import java.io.File;

public class Main {

	public static void main(String[] args) {

		File resourceA = new File("inputData.csv");
		File resourceB = new File("outputData.json");

		Thread threadA = new Thread(() -> {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " attempting to lock resourceA (csv)");
			synchronized (resourceA) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println(threadName +
						" NEXT attempting to lock resourceB (json), " +
						"still has lock on resourceA (csv)");
				synchronized (resourceB) {
					System.out.println(threadName + " has lock on resourceB (json)");
				}
				System.out.println(threadName + " has released lock on resourceB (json)");
			}
			System.out.println(threadName + " has released lock on resourceA (csv)");
		}, "THREAD-A");

		Thread threadB = new Thread(() -> {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " attempting to lock resourceA (csv)");
			synchronized (resourceA) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println(threadName +
						" NEXT attempting to lock resourceB (json), " +
						"still has lock on resourceA (csv)");
				synchronized (resourceB) {
					System.out.println(threadName + " has lock on resourceB (json)");
				}
				System.out.println(threadName + " has released lock on resourceB (json)");
			}
			System.out.println(threadName + " has released lock on resourceA (csv)");
		}, "THREAD-B");

		threadA.start();
		threadB.start();

		try {
			threadA.join();
			threadB.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
