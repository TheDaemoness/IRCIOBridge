package com.github.thedaemoness.irciobridge.info;

import java.util.Optional;

public class UserInfo extends SenderInfo {
	private final String nick;
	private final String uname;
	//private final String realname;
	//private final Collection[ChannelInfo] channels;
	public UserInfo(String nick, String uname, String host) {
		super(host);
		this.nick = nick;
		this.uname = uname;
	}
	final public String getNick() { return nick; }
	final public String getUser() { return uname; }
	@Override
	public String toString() {
		return nick+"!"+uname+"@"+getHost();
	}
	@Override
	final public Optional<UserInfo> toUserInfo() {
		return Optional.of(this);
	}
}
