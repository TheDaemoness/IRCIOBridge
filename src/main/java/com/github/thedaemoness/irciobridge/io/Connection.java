package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.UserDataModel;
import com.github.thedaemoness.irciobridge.info.NetworkInfo;
import com.github.thedaemoness.irciobridge.info.UserInfo;
import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageOut;

import java.io.IOException;

public class Connection implements MessageIO {
	private final MessageIO io;
	private UserInfo us;
	private NetworkInfo them;

	Connection(MessageIO io, UserInfo us, NetworkInfo them) {
		this.io = io;
		this.us = us;
		this.them = them;
	}
	/* Going to avoid adding too many convenience functions (or a builder) here.
	 * Users are likely not going to interact with this directly.
	 * For programmers: the argument to "how" you want in most circumstances is Handshake.Basic.INSTANCE.
	 */
	public static Connection make(ServerAddress where, UserDataModel who, Handshake how) throws IOException {
		return how.shakeHands(new SocketWrapper(where.connect()), who, where.getPassword());
	}

	public int maxLine() {
		return 512; //This may not necessarily be fixed, depending on the IRCd and how IRCv3 progress.
	}

	public UserInfo getClientInfo() {
		return us;
	}
	public NetworkInfo getNetworkInfo() {
		return them;
	}

	//public boolean hasFlag(char flag) {return flags.contains(flag);}

	@Override
	public void close() throws Exception {
		io.close();
	}

	@Override
	public void accept(MessageOut messageOut) {
		io.accept(messageOut);
	}

	@Override
	public MessageIn get() {
		return io.get();
	}
}
