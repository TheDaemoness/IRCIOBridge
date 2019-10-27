package com.github.thedaemoness.irciobridge.messages;

import com.github.thedaemoness.irciobridge.info.SenderInfo;

import java.util.Arrays;

public class MessageOut extends Message<MessageType.Sendable> {
	MessageOut(MessageType.Sendable type, String[] args, String text) {
		super(type, Arrays.asList(args), String.join(" ", args), text);
	}

	public MessageIn asFrom(SenderInfo who) {
		return new MessageIn(who.toString(), getType(), getArgs(), getArgsJoined(), getText());
	}
}