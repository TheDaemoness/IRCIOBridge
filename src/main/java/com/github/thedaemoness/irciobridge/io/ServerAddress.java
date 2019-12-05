package com.github.thedaemoness.irciobridge.io;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Supplier;

public class ServerAddress {
	public static final ServerAddress FREENODE = ServerAddress.build("irc.freenode.net").get();

	private final String address;
	private final int port;
	private final boolean useSsl;
	private final String password; //Keeping this here because it's critical info for connecting to a specific server.

	private ServerAddress(String address, int port, boolean useSsl, String password) {
		this.address = address;
		this.port = port;
		this.useSsl = useSsl;
		this.password = password;
	}

	public String getAddress() { return address; }
	public int getPort() { return port; }
	public boolean shouldUseSSL() { return useSsl; }
	public String getPassword() { return password; }

	public Socket connect() throws IOException {
		return new Socket(address, port);
	}

	public static final class Builder implements Supplier<ServerAddress> {
		private String address;
		private String password = "";
		private boolean useSsl = false;
		private int port = -1; // -1 is magic.

		private Builder(String address) { this.address = address; }
		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}
		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}
		public Builder useSsl() {
			useSsl = true;
			return this;
		}
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}

		@Override
		public ServerAddress get() {
			return new ServerAddress(address, port < 0 ? (useSsl ? 6697 : 6667) : port, useSsl, password);
		}
	}

	public static Builder build(String address) {
		return new Builder(address);
	}
}
