package com.github.thedaemoness.irciobridge.info;

import java.util.Optional;

public class SenderInfo {
	private final String host;
	public SenderInfo(String host) {
		this.host = host;
	}
	final public String getHost() { return host; }
	public Optional<UserInfo> toUserInfo() {
		return Optional.empty();
	}
	@Override
	public String toString() {
		return host;
	}
}
