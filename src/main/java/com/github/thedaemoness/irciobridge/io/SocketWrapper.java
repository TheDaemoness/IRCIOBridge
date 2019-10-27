package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageOut;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class SocketWrapper implements MessageIO {
	private final Scanner in;
	private final Writer out;

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
			out.write(messageIn.toLine());
			out.flush();
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
