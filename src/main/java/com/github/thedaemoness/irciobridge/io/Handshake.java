package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.IRCException;
import com.github.thedaemoness.irciobridge.UserDataModel;
import com.github.thedaemoness.irciobridge.info.NetworkInfo;
import com.github.thedaemoness.irciobridge.info.UserInfo;
import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageType;
import com.github.thedaemoness.irciobridge.util.Modes;

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
			if(!password.isEmpty()) sock.accept(MessageType.Command.PASS.create(password));
			sock.accept(MessageType.Command.NICK.create(nick));
			sock.accept(MessageType.Command.USER.createWithText(config.getRealName(), config.getIdent(), "8", "*"));
			//Requirement
			MessageIn msg;
			UserInfo userinfo = null;
			NetworkInfo netinfo = null;
			while(!(msg = sock.get()).isEmpty()) {
				final var type = msg.getType();
				if(type == MessageType.Reply.MYINFO) {
					netinfo = new NetworkInfo(
						new Modes(msg.getArgs().get(3)),
						new Modes(msg.getArgs().get(4))
					);
					sock.accept(MessageType.Command.WHOIS.create(nick));
				} else if(type == MessageType.Reply.WHOISUSER) {
					userinfo = new UserInfo(
						msg.getArgs().get(1),
						msg.getArgs().get(2),
						msg.getArgs().get(3)
					);
				}
				else if(type == MessageType.Command.PING) {
					final var args = msg.getArgs();
					if(args.isEmpty()) throw new HandshakeFailedException();
					sock.accept(MessageType.Command.PONG.createWithText(msg.getText(), args.get(0)));
				}
				else if(type == MessageType.Error.NICKNAMEINUSE) {
					if (nickit.hasNext()) {
						nick = nickit.next();
						sock.accept(MessageType.Command.NICK.createWithText("", nick));
					} else throw new IRCException(MessageType.Error.NICKNAMEINUSE);
				} else if(type.getInfo() == MessageType.Info.ERROR) {
					throw new IRCException((MessageType.Error)type);
				}
				if(netinfo != null && userinfo != null) {
					return new Connection(sock, userinfo, netinfo);
				}
			}
			throw new HandshakeFailedException();
		}
	}
}
