package com.github.thedaemoness.irciobridge;

import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.google.common.util.concurrent.AbstractIdleService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class IRCIOBridgeCommandReceiver extends AbstractIdleService implements Observer<MessageIn>, AutoCloseable {
	
	private StreamReaderService streamReaderService;
	
	private ServerDataModel server;
	
	public IRCIOBridgeCommandReceiver(ServerDataModel server, InputStream input, Writer output, Charset charset) {
		this.server = server;
		
		this.streamReaderService = new StreamReaderService(input, charset);
	}
    
    private final PublishSubject<MessageIn> pingSubject = PublishSubject.create();

    public final Observable<MessageIn> onPing() {
    	return pingSubject;
    }

	@Override
	protected void startUp() throws Exception {
		streamReaderService.startAsync();
		
		streamReaderService.onMessage().subscribe(this);
	}

	@Override
	protected void shutDown() throws Exception {
		pingSubject.onComplete();
		
		streamReaderService.stopAsync();
	}

	@Override
	public void close() throws Exception {
		shutDown();
	}

	@Override
	public void onSubscribe(Disposable d) {
		
	}

	@Override
	public void onNext(MessageIn m) {
		if("PING".equals(m.getCommand())) {
			pingSubject.onNext(m);
		}
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onComplete() {
		try {
			close();
		} catch (Exception e) {
			onError(e);
		}
	}
}
