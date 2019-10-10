package com.github.thedaemoness.irciobridge;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ServerDataModel implements Serializable {
	private final UserDataModel user;
	
	private final String address;
	
	private final String serverPassword;
	
	private final int port;
	
	private final boolean useSsl;
	
	private final Map<String, ChannelDataModel> channels = new HashMap<>();

	private final String partMessage;
	private final String quitMessage;
	
    private final Cache<UUID, ChannelMessageDataModel> serverMessageBuffer = 
       CacheBuilder.newBuilder()
       .maximumSize(1000)
       //.expireAfterAccess(30, TimeUnit.MINUTES)
       .build();
	
	public UserDataModel getUser() {
		return user;
	}
	public String getAddress() {
		return address;
	}
	public String getServerPassword() {
		return serverPassword;
	}
	public int getPort() {
		return port;
	}
	public boolean isUseSsl() {
		return useSsl;
	}
	public Map<String, ChannelDataModel> getChannels() {
		return channels;
	}
	public String getPartMessage() {
		return partMessage;
	}
	public String getQuitMessage() {
		return quitMessage;
	}
	public Charset getEncoding() {
		return encoding;
	}
	private final Charset encoding;
	
	private ServerDataModel(UserDataModel user,
		String address,
		String serverPassword,
		int port,
		boolean useSsl,
		Collection<ChannelDataModel> channels,
		String partMessage,
		String quitMessage,
		Charset encoding) {
		this.user = user;
		this.address = address;
		this.serverPassword = serverPassword;
		this.port = port;
		this.useSsl = useSsl;
		for (ChannelDataModel channel : channels) {
			Objects.requireNonNull(channel.getChannelName(), "Channel name cannot be null");
			this.channels.put(channel.getChannelName(), channel);
		}
		this.partMessage = partMessage;
		this.quitMessage = quitMessage;
		this.encoding = encoding;
	}
	public static final class Builder implements Supplier<ServerDataModel> {
		private UserDataModel user = null;
		private String address = "irc.freenode.net";
		private String serverPassword = "";
		private int port = 6667;
		private boolean useSsl = false;
		private Collection<ChannelDataModel> channels = new HashSet<>();
		private String partMessage;
		private String quitMessage;
		private Charset encoding = StandardCharsets.UTF_8;
		
		private Builder(UserDataModel user, int port, boolean useSsl) {
			this.port = port;
			this.useSsl = useSsl;
			this.user = user;
			partMessage = user.getIdent()+" left";
			quitMessage = user.getIdent()+" quit";
		}

		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}
		public Builder setServerPassword(String serverPassword) {
			this.serverPassword = serverPassword;
			return this;
		}
		public Builder setPort(int port, boolean usesSsl) {
			this.port = port;
			useSsl = usesSsl;
			return this;
		}
		public Builder addChannel(ChannelDataModel channel) {
			channels.add(channel);
			return this;
		}
		public Builder setPartMessage(String partMessage) {
			this.partMessage = quitMessage;
			return this;
		}
		public Builder setQuitMessage(String quitMessage) {
			this.quitMessage = quitMessage;
			return this;
		}
		public Builder setEncoding(Charset encoding) {
			this.encoding = encoding;
			return this;
		}

		@Override
		public ServerDataModel get() {
			Objects.requireNonNull(user, "User cannot be null.");
			return new ServerDataModel(user, address, serverPassword, port, useSsl, channels, partMessage, quitMessage, encoding);
		}
	}
	public static Builder builder(UserDataModel who, boolean useSsl) {
		return new Builder(who, useSsl ? 6697 : 6667, useSsl);
	}
	public static Builder builder(UserDataModel who) {
		return builder(who, false);
	}
}
