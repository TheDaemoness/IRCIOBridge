package com.github.thedaemoness.irciobridge;

import com.github.thedaemoness.irciobridge.messages.MessageType;

import java.io.IOException;

public class IRCException extends IOException {
	private MessageType.ResponseError error;
	public IRCException(MessageType.ResponseError what) {
		this.error = what;
	}

	@Override
	public String toString() {
		return this.error.toString();
	}
}
