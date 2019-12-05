package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.IRCException;
import com.github.thedaemoness.irciobridge.UserDataModel;
import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageType;

import java.io.IOException;
import java.util.Iterator;

public abstract class Handshake {
	public static class HandshakeFailedException extends IOException {}
	public abstract Connection shakeHands(MessageIO socket, UserDataModel config, String password) throws IOException;

	public static class Basic extends Handshake {
		public static final Basic INSTANCE = new Basic();

		@Override
		public Connection shakeHands(MessageIO sock, UserDataModel config, String password) throws IOException {
			final Iterator<String> nickit = config.getNicks().iterator();
			String nick = nickit.next();
			if(!password.isEmpty()) sock.accept(MessageType.Command.PASS.createRaw("", password));
			sock.accept(MessageType.Command.NICK.createRaw("", nick));
			sock.accept(MessageType.Command.USER.createRaw(config.getRealName(), config.getIdent(), "8", "*"));
			MessageIn msg;
			while(!(msg = sock.get()).isEmpty()) {
				final var type = msg.getType();
				if(type == MessageType.Reply.WELCOME) return new Connection(sock, nick); //RETURN IS HERE!
				else if(type == MessageType.Command.PING) {
					final var args = msg.getArgs();
					if(args.isEmpty()) throw new HandshakeFailedException();
					sock.accept(MessageType.Command.PONG.createRaw(msg.getText(), args.get(0)));
				}
				else if(type == MessageType.Error.NICKNAMEINUSE) {
					if (nickit.hasNext()) {
						nick = nickit.next();
						sock.accept(MessageType.Command.NICK.createRaw("", nick));
					} else throw new IRCException(MessageType.Error.NICKNAMEINUSE);
				} else if(type.getInfo() == MessageType.Info.ERROR) {
					throw new IRCException((MessageType.Error)type);
				}
			}
			throw new HandshakeFailedException();
		}
	}
}
