package com.github.thedaemoness.irciobridge;

import com.github.thedaemoness.irciobridge.io.Connection;
import com.github.thedaemoness.irciobridge.io.Handshake;
import com.github.thedaemoness.irciobridge.io.ServerAddress;
import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageType;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.net.*;
//import javax.annotation.Nonnull;

/** Implements a single-channel connection to an IRC network as an object exposing both a PrintStream and InputStream.
 * Zero support for capabilities, SSL, or more interesting connection processes. Use at your own risk.
 * Provides a helper function to set System.out and System.in.
 * @author TheDaemoness
 * */
public class IRCIOBridge implements AutoCloseable {
	public final int PORT;
	public final String SERVER;
	public final String CHANNEL;
	public final String NICK;

	private final Socket s;

	private final PrintStream out;
	private final InputStream in;

	public IRCIOBridge(
		String server,
		String channel,
		List<String> nicks,
		int port
	) throws IOException {
		Objects.requireNonNull(server);
		Objects.requireNonNull(channel);
		Objects.requireNonNull(nicks);
		if(nicks.isEmpty()) throw new IllegalArgumentException("Passed nick list is empty");
		PORT = port;
		SERVER = server;
		CHANNEL = channel;
		s = new Socket(SERVER, PORT);
		final var address = ServerAddress.get(SERVER, PORT, false);
		Scanner input = new Scanner(s.getInputStream(), StandardCharsets.UTF_8);
		Writer output = new OutputStreamWriter(s.getOutputStream());
		//Side effect: also handles most of the initial messaging.
		final var connection = Connection.make(address, UserDataModel.builder(nicks).get(), Handshake.Basic.INSTANCE);
		NICK = connection.getClientInfo().getNick();
		while(input.hasNextLine()) { //WARNING: break;
			final MessageIn m = connection.get();
			System.err.println(m);
			if(m.getType() == MessageType.Reply.MYINFO) {
				connection.accept(MessageType.Command.JOIN.createWithText("", CHANNEL));
			} else if(m.getType() == MessageType.Reply.ENDOFNAMES) {
				break;
			}
		}
		this.out = new PrintStream(new Output(s.getOutputStream(), CHANNEL));
		this.in = new Input(input, output, CHANNEL);
	}
	public IRCIOBridge(
		String server,
		String channel,
		List<String> nicks
	) throws IOException {
		this(server, channel, nicks, 6667);
	}
	public IRCIOBridge(
		String server,
		String channel,
		String... nicks
	) throws IOException {
		this(server, channel, Arrays.asList(nicks));
	}

	@Override
	public void close() throws IOException {
		if(s.isConnected()) {
			out.flush();
			s.close();
		}
	}

	public IRCIOBridge setSystemStreams() {
		System.setOut(out);
		System.setIn(in);
		return this;
	}

	public static IRCIOBridge setSystemStreams(String server, String channel, String... nicks)
	throws IOException {
		return new IRCIOBridge(server, channel, nicks).setSystemStreams();
	}
}

//Example use: IRCIOBridge.setSystemStreams("irc.freenode.net", "#somechannel", "MyBotName");
