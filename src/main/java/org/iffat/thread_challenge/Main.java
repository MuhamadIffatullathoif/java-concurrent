package org.iffat.thread_challenge;

class OddThread extends Thread {

	@Override
	public void run() {
		for (int i = 1; i <= 10; i += 2) {
			System.out.println("OddThread: " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("OddThread Interrupted!");
				break;
			}
		}
	}
}

class EvenRunnable implements Runnable {

	@Override
	public void run() {
		for (int i = 2; i <= 10; i += 2) {
			System.out.println("EvenRunnable: " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("EvenRunnable Interrupted!");
				break;
			}
		}
	}
}

public class Main {

	public static void main(String[] args) {

		OddThread oddThread = new OddThread();

		Runnable runnable = () -> {
			for (int i = 2; i <= 10; i += 2) {
				System.out.println("EvenRunnable: " + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("EvenRunnable Interrupted!");
					break;
				}
			}
		};
		// Thread thread = new Thread(new EvenRunnable());
		Thread thread = new Thread(runnable);
		oddThread.start();
		thread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		oddThread.interrupt();
	}
}
