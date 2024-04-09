package org.iffat.synconizations;

public class Main {

	public static void main(String[] args) {

		BankAccount companyAccount = new BankAccount("Tom", 10_000);

		Thread thread1 = new Thread(() -> companyAccount.withDraw(2500));
		Thread thread2 = new Thread(() -> companyAccount.deposit(5000));
		Thread thread3 = new Thread(() -> companyAccount.setName("Tim"));
		Thread thread4 = new Thread(() -> companyAccount.withDraw(5000));

		thread1.start();
		thread2.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread3.start();
		thread4.start();

		try {
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Final Balance: " + companyAccount.getBalance());
	}
}
