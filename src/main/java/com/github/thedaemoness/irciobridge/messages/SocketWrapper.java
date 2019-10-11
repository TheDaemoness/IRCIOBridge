package com.github.thedaemoness.irciobridge.messages;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SocketWrapper implements AutoCloseable, Supplier<MessageIn>, Consumer<MessageOut> {
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
	public void accept(MessageOut messageIn) {
		try {
			out.write(messageIn.toString(true));
		} catch (IOException e) {
			//Unfortunate.
			e.printStackTrace();
		}
	}

	@Override
	public MessageIn get() {
		if(in.hasNextLine()) return MessageIn.parse(in.nextLine());
		else return MessageIn.EMPTY;
	}
}
