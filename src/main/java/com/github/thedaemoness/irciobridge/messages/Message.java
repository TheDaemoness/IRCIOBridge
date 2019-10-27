package com.github.thedaemoness.irciobridge.messages;

import com.github.thedaemoness.irciobridge.util.StringAccumulator;

import java.util.List;

public abstract class Message<Type extends MessageType> {
	private final Type type;
	private final List<String> args;
	private final String argsJoined;
	private final String text;
	protected Message(Type type, List<String> args, String argsJoined, String text) {
		this.type = type;
		this.args = args;
		this.argsJoined = argsJoined;
		this.text = text;
	}
	protected <T> StringAccumulator<T> accumulateIn(StringAccumulator<T> sb, boolean includeNewline) {
		sb.append(type);
		if(!argsJoined.isEmpty()) sb.append(" ").append(argsJoined);
		if(includeNewline) sb.append("\r\n");
		if(!text.isEmpty()) sb.append(" :").append(text);
		return sb;
	}
	/** Returns the length of one line in bytes. */
	final public int length() {
		return accumulateIn( new StringAccumulator.StringLength(), true).get();
	}
	@Override
	final public String toString() {
		return accumulateIn( new StringAccumulator.StringBuilder(), false).get();
	}
	final public String toLine() {
		return accumulateIn( new StringAccumulator.StringBuilder(), true).get();
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
		return args;
	}
	public String getArgsJoined() {
		return argsJoined;
	}

	public String getText() {
		return text;
	}
}