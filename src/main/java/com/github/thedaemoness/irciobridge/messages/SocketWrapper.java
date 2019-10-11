package com.github.thedaemoness.irciobridge.messages;

import io.reactivex.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SocketWrapper implements AutoCloseable, Supplier<Message>, Consumer<Message> {
	private Scanner in;
	private Writer out;

	public SocketWrapper(Socket sock) throws IOException {
		this.in = new Scanner(sock.getInputStream());
		this.out = new OutputStreamWriter(sock.getOutputStream());
	}

	@Override
	public void close() throws Exception {
		in.close();
		out.close();
	}

	@Override
	public void accept(Message message) {
		try {
			out.write(message.toString(true));
		} catch (IOException e) {
			//Unfortunate.
			e.printStackTrace();
		}
	}

	@Override
	public Message get() {
		if(in.hasNextLine()) return new Message(in.nextLine());
		else return Message.EMPTY;
	}
}
