package com.github.thedaemoness.irciobridge.messages;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Message {
	public static final Message EMPTY = new Message(MessageType.EMPTY, "");

	private final String prefix;
	private final MessageType type;
	private final String[] args;
	private final String argsJoined;
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
		argsJoined = divided[0].trim();
		args = argsJoined.split(" ");
		if(divided.length == 2) text = divided[1];
		else text = "";
	}
	Message(MessageType type, String text, String... args) {
		this.prefix = "";
		this.type = type;
		this.args = args;
		this.argsJoined = String.join(" ", args);
		this.text = text;
	}
	public String toString(boolean includeNewline) {
		final StringBuilder sb = new StringBuilder();
		if(!prefix.isEmpty()) sb.append(':').append(prefix).append(' ');
		sb.append(type);
		if(!argsJoined.isEmpty()) sb.append(" ").append(argsJoined);
		if(!text.isEmpty()) sb.append(" :").append(text);
		if(includeNewline) sb.append("\r\n");
		return sb.toString();
	}
	@Override
	public String toString() {
		return toString(false);
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

	public List<String> getArgs() {
		return Arrays.asList(args);
	}

	public String getText() {
		return text;
	}
}