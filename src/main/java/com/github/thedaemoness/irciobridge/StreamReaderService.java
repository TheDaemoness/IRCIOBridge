package com.github.thedaemoness.irciobridge;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.google.common.util.concurrent.AbstractIdleService;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class StreamReaderService extends AbstractIdleService {
	private final PublishSubject<MessageIn> messageSubject = PublishSubject.create();
	private final InputStream inputStream;
	private final Charset charset;
	public final Observable<MessageIn> onMessage() {
		return messageSubject;
	}

	public StreamReaderService(InputStream inputStream, Charset charset) {
		this.inputStream = inputStream;
		this.charset = charset;
	}

	@Override
	protected void startUp() throws Exception {
		try (Scanner input = new Scanner(inputStream, charset)) {
			while (input.hasNextLine() && state() == State.RUNNING) { // WARNING: break;
				final MessageIn m = MessageIn.parse(input.nextLine());

				messageSubject.onNext(m);
			}
		}
	}

	@Override
	protected void shutDown() throws Exception {
		try {
			inputStream.close();
		} catch (IOException ioe) {

		}
	}

}
