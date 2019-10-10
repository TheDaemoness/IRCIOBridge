package com.github.thedaemoness.irciobridge;

import java.util.*;
import java.util.function.Supplier;

public class UserDataModel {
	public String getRealName() {
		return realName;
	}
	public String getIdent() {
		return ident;
	}
	public List<String> getNicks() {
		return nicks;
	}
	private final String realName;
	private final String ident;
	private final List<String> nicks;
	
	private UserDataModel(
			String realName,
			String ident,
			List<String> nicks) {
		this.realName = realName;
		this.ident = ident;
		this.nicks = Collections.unmodifiableList(nicks);
	}
	public static final class Builder implements Supplier<UserDataModel> {
		private final List<String> nicks = new ArrayList<>();
		private String ident;
		private String realName = "Beep Boop";

		private Builder(String nick) {
			ident = nick;
			nicks.add(nick);
		}
		
		public Builder setRealName(String realName) {
			this.realName = realName;
			return this;
		}
		
		public Builder setIdent(String ident) {
			this.ident = ident;
			return this;
		}
		
		public Builder addNick(String nick) {
			nicks.add(nick);
			return this;
		}

		@Override
		public UserDataModel get() {
			return new UserDataModel(realName, ident, nicks);
		}
	}
	public static Builder builder(String nick) {
		return new Builder(nick);
	}
	public static Builder builder(Iterable<String> nick) {
		final var iterator = nick.iterator();
		final var bob = builder(iterator.next());
		while(iterator.hasNext()) bob.addNick(iterator.next());
		return bob;
	}
}
