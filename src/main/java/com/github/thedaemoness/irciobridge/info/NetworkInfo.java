package com.github.thedaemoness.irciobridge.info;

import com.github.thedaemoness.irciobridge.util.Modes;

public class NetworkInfo {
	private final Modes modesChannel;
	private final Modes modesUser;
	public NetworkInfo(Modes user, Modes channel) {
		this.modesChannel = channel;
		this.modesUser = user;
	}
	public Modes getModesChannel() { return modesChannel; }
	public Modes getModesUser() { return modesUser; }
}
