package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.messages.SocketWrapper;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class Connection {
	private SocketWrapper wrappedSocket;
	private String nick;
	private Set<Character> flags; //TODO: Flag interface + enum? Also, boxing improvements.

	public Connection(Socket sock, String nick, Set<Character> flags) throws IOException {
		this.wrappedSocket = new SocketWrapper(sock);
		this.nick = nick;
		this.flags = flags;
	}

	public SocketWrapper getIO() {
		return wrappedSocket;
	}

	public String getNick() {
		return nick;
	}

	public boolean hasFlag(char flag) {
		return flags.contains(flag);
	}
}
