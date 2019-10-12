package com.github.thedaemoness.irciobridge.messages;

public class MessageOut extends Message<MessageType.Sendable> {
	public final int MAX_LENGTH = 512;

	MessageOut(MessageType.Sendable type, String[] args, String text) {
		super(type, args, String.join(" ", args), text);
	}

	//TODO: Split overlong messages.
}