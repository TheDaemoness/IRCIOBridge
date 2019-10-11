package com.github.thedaemoness.irciobridge.messages;

import java.util.Arrays;
import java.util.List;

public abstract class Message<Type extends MessageType> {
	private final Type type;
	private final String[] args;
	private final String argsJoined;
	private final String text;
	protected Message(Type type, String[] args, String argsJoined, String text) {
		this.type = type;
		this.args = args;
		this.argsJoined = argsJoined;
		this.text = text;
	}
	final protected void buildString(StringBuilder sb, boolean includeNewline) {
		sb.append(type);
		if(!argsJoined.isEmpty()) sb.append(" ").append(argsJoined);
		if(!text.isEmpty()) sb.append(" :").append(text);
		if(includeNewline) sb.append("\r\n");
	}
	public String toString(boolean includeNewline) {
		final StringBuilder sb = new StringBuilder();
		buildString(sb, includeNewline);
		return sb.toString();
	}
	@Override
	public String toString() {
		return toString(false);
	}

	public Type getType() {
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