package com.github.thedaemoness.irciobridge;

import java.util.Scanner;

public class Message {
	private final String prefix;
	private final String command;
	private final String args;
	private final String text;
	Message(String s) {
		Scanner line = new Scanner(s);
		final String cmdbuf = line.next();
		if(cmdbuf.charAt(0) == ':') {
			prefix = cmdbuf.substring(1);
			command = line.next();
		} else {
			prefix = "";
			command = cmdbuf;
		}
		final String[] divided = line.nextLine().split(":",2);
		args = divided[0].trim();
		if(divided.length == 2) text = divided[1];
		else text = "";
	}
	@Override
	public String toString() {
		return String.join("", ":", getPrefix(), " ", getCommand(), " ", getArgs(), " :", getText());
	}

	public String getPrefix() {
		return prefix;
	}

	public String getCommand() {
		return command;
	}

	public String getArgs() {
		return args;
	}

	public String getText() {
		return text;
	}
}