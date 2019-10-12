package com.github.thedaemoness.irciobridge.io;

import io.reactivex.annotations.Nullable;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public final class ServerAddress {
	public static final int DEFAULT_PORT = 6667;
	public static final int DEFAULT_PORT_SSL = 6697;
	private static SocketFactory defaultSocketFactory(boolean useSsl) {
		return useSsl ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
	}

	public static final ServerAddress LOCALHOST = ServerAddress.get(null, false);
	public static final ServerAddress FREENODE = ServerAddress.get("irc.freenode.net", true);

	@Nullable private final String address;
	@Nullable private final InetAddress resolved;
	private final int port;
	private final SocketFactory sockFactory;
	//Keeping this here because it's critical info for connecting to a specific server.
	@Nullable private final String password;

	private ServerAddress(String address, InetAddress resolved, int port, SocketFactory sockFactory, String password) {
		this.address = address;
		this.resolved = resolved;
		this.port = port;
		this.sockFactory = sockFactory;
		this.password = password;
	}
	public static ServerAddress get(String address, int port, boolean useSsl) {
		return new ServerAddress(address, null, port, defaultSocketFactory(useSsl), "");
	}
	public static ServerAddress get(String address, boolean useSsl) {
		return get(address, useSsl ? DEFAULT_PORT_SSL : DEFAULT_PORT, useSsl);
	}
	public static ServerAddress get(String address) {
		return get(address, false);
	}

	public ServerAddress withAddress(String address) {
		return new ServerAddress(address, null, port, sockFactory, password);
	}
	public ServerAddress withAddress(InetAddress address) {
		Objects.requireNonNull(address);
		return new ServerAddress(address.getHostName(), address, port, sockFactory, password);
	}
	public ServerAddress withPort(int port, SocketFactory sockFactory) {
		Objects.requireNonNull(sockFactory);
		return new ServerAddress(address, resolved, port, sockFactory, password);
	}
	public ServerAddress withPort(int port, boolean useSsl) {
		return withPort(port, defaultSocketFactory(useSsl));
	}
	public ServerAddress withPassword(String password) {
		return new ServerAddress(address, resolved, port, sockFactory, password == null ? "" : password);
	}
	public InetAddress resolve() throws UnknownHostException {
		return (resolved != null) ? resolved : InetAddress.getByName(address);
	}
	public String getAddress() { return (address == null ? "localhost" : address); }
	public int getPort() { return port; }
	public String getPassword() { return password; }

	public boolean isReachable(int timeout) {
		try {
			return resolve().isReachable(timeout);
		} catch (IOException e) {
			return false;
		}
	}
	public Socket connect() throws IOException {
		return sockFactory.createSocket(resolve(), getPort());
	}
	@Override
	public String toString() {
		return getAddress()+':'+port;
	}
}
