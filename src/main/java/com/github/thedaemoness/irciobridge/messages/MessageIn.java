package com.github.thedaemoness.irciobridge.messages;

import java.util.Scanner;

public class MessageIn extends Message<MessageType> {
	private final String prefix;
	protected MessageIn(String prefix, MessageType type, String argsJoined, String text) {
		super(type, argsJoined.split(" "), argsJoined, text);
		this.prefix = prefix;
	}

	public static final MessageIn EMPTY = new MessageIn("", MessageType.EMPTY, "", "");
	public static MessageIn parse(String s) {
		String prefix, text, argsJoined;
		MessageType type;
		final Scanner line = new Scanner(s);
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
		if(divided.length == 2) text = divided[1];
		else text = "";
		return new MessageIn(prefix, type, argsJoined, text);
	}

	@Override
	public String toString(boolean includeNewline) {
		final StringBuilder sb = new StringBuilder();
		if(!prefix.isEmpty()) sb.append(':').append(prefix).append(' ');
		super.buildString(sb, includeNewline);
		sb.append(getText());
		return sb.toString();
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isEmpty() {
		return this.getType() == MessageType.EMPTY;
	}
}