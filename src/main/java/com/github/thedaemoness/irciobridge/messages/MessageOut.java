package com.github.thedaemoness.irciobridge.messages;

public class MessageOut extends Message<MessageType.Sendable> {
	MessageOut(MessageType.Sendable type, String[] args, String text) {
		super(type, args, String.join("", args), text);
	}
}