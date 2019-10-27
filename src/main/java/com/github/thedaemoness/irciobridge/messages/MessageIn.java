package com.github.thedaemoness.irciobridge.messages;

import com.github.thedaemoness.irciobridge.util.StringAccumulator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MessageIn extends Message<MessageType> {
	private final String prefix;
	protected MessageIn(String prefix, MessageType type, String argsJoined, String text) {
		this(prefix, type, Arrays.asList(argsJoined.split(" ")), argsJoined, text);
	}

	public static final MessageIn EMPTY = new MessageIn("", MessageType.EMPTY, "", "");

	protected MessageIn(String prefix, MessageType type, List<String> args, String argsJoined, String text) {
		super(type, args, argsJoined, text);
		this.prefix = prefix;
	}

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
	protected <T> StringAccumulator<T> accumulateIn(StringAccumulator<T> sb, boolean includeNewline) {
		if(!prefix.isEmpty()) sb.append(":").append(prefix).append(" ");
		return super.accumulateIn(sb, includeNewline);
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isEmpty() {
		return this.getType() == MessageType.EMPTY;
	}
}