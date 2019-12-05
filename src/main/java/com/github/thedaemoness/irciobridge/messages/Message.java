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
		if(includeNewline) sb.append("\r\n");
		if(!text.isEmpty()) sb.append(" :");
	}
	public String toString(boolean includeNewline) {
		final StringBuilder sb = new StringBuilder();
		buildString(sb, includeNewline);
		sb.append(text);
		return sb.toString();
	}
	@Override
	public String toString() {
		return toString(false);
	}

	public boolean isAnyOf(MessageType... which) {
		for(MessageType type: which) {
			if(type == this.type) return true;
		}
		return false;
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