package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.UserDataModel;
import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageOut;

import java.io.IOException;

public class Connection implements MessageIO {
	private final MessageIO io;
	private String nick;
	//private Set<Character> flags; //TODO: Flag interface + enum? Also, boxing improvements.

	Connection(MessageIO io, String nick) {
		this.io = io;
		this.nick = nick;
	}
	/* Going to avoid adding too many convenience functions (or a builder) here.
	 * Users are likely not going to interact with this directly.
	 * For programmers: the argument to "how" you want in most circumstances is Handshake.Basic.INSTANCE.
	 */
	public static Connection make(ServerAddress where, UserDataModel who, Handshake how) throws IOException {
		return how.shakeHands(new SocketWrapper(where.connect()), who, where.getPassword());
	}

	public String getNick() {
		return nick;
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
