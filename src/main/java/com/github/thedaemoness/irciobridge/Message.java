package com.github.thedaemoness.irciobridge;

import com.github.thedaemoness.irciobridge.messages.MessageType;

import java.util.Scanner;

public class Message {
	private final String prefix;
	private final MessageType type;
	private final String args;
	private final String text;
	public Message(String s) {
		Scanner line = new Scanner(s);
		final String cmdbuf = line.next();
		if(cmdbuf.charAt(0) == ':') {
			prefix = cmdbuf.substring(1);
			type = MessageType.parse(line.next());
		} else {
			prefix = "";
			type = MessageType.parse(cmdbuf);
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

	public MessageType getType() {
		return type;
	}

	public String getCommand() {
		//TODO: Rename getCommand.
		return type.toString();
	}

	public String getArgs() {
		return args;
	}

	public String getText() {
		return text;
	}
}