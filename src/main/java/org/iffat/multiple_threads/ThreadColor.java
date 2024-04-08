package org.iffat.multiple_threads;

public enum ThreadColor {
	ANSI_RESET("\u001B[0m"),
	ANSI_BLACK("\u001B[30m"),
	ANSI_WHITE("\u001b[37m"),
	ANSI_BLUE("\u001b[34m"),
	ANSI_CYAN("\u001b[36m"),
	ANSI_GREEN("\u001b[32m"),
	ANSI_PURPLE("\u001b[35m"),
	ANSI_RED("\u001b[31m"),
	ANSI_YELLOW("\u001b[33m");

	private final String color;

	ThreadColor(String color) {
		this.color = color;
	}

	public String color() {
		return color;
	}
}
