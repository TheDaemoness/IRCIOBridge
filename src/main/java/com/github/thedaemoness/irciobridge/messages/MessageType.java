package com.github.thedaemoness.irciobridge.messages;

//WARNING: Contains enums which are NAME-SENSITIVE!

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public interface MessageType {
	enum Info {
		UNKNOWN,
		COMMAND,
		REPLY,
		ERROR;

		private static Map<String, MessageType> parseMap = new HashMap<>();
	}
	Info getInfo();

	static MessageType parse(String text) {
		return Info.parseMap.computeIfAbsent(text, key -> new MessageType() {
			@Override
			public String toString() { return key; }
			@Override
			public Info getInfo() { return Info.UNKNOWN; }
		});
	}

	interface Response extends MessageType {
		int getNumber();

		@Override
		default Info getInfo() {
			final int number = getNumber();
			return number >= 400 && number < 600 ? Info.ERROR : Info.REPLY;
		}
		default boolean clientServerOnly() {
			final int number = getNumber();
			return number >= 1 && number < 100;
		}
	}

	enum Command implements MessageType {
		ADMIN,
		AWAY,
		CAP,
		CONNECT,
		DIE,
		ENCAP,
		ERROR,
		INFO,
		INVITE,
		ISON,
		JOIN,
		KICK,
		KILL,
		LINKS,
		LIST,
		LUSERS,
		MODE,
		MOTD,
		NAMES,
		NICK,
		NOTICE,
		OPER,
		PART,
		PASS,
		PING,
		PONG,
		PRIVMSG,
		QUIT,
		REHASH,
		RESTART,
		SERVER,
		SERVICE,
		SERVLIST,
		SQUERY,
		SQUIT,
		STATS,
		SUMMON,
		TIME,
		TOPIC,
		TRACE,
		USER,
		USERHOST,
		USERS,
		VERSION,
		WALLOPS,
		WHO,
		WHOIS,
		WHOWAS;

		Command() {
			Info.parseMap.put(this.toString(), this);
		}

		@Override
		public Info getInfo() { return Info.COMMAND; }
	}

	enum Reply implements Response {
		WELCOME(1),
		YOURHOST(2),
		CREATED(3),
		MYINFO(4),
		BOUNCE(5),
		USERHOST(302),
		ISON(303),
		AWAY(301),
		UNAWAY(305),
		NOWAWAY(306),
		WHOISUSER(311),
		WHOISSERVER(312),
		WHOISOPERATOR(313),
		WHOISIDLE(317),
		ENDOFWHOIS(318),
		WHOISCHANNELS(319),
		WHOWASUSER(314),
		ENDOFWHOWAS(369),
		LISTSTART(321),
		LIST(322),
		LISTEND(323),
		UNIQOPIS(325),
		CHANNELMODEIS(324),
		NOTOPIC(331),
		TOPIC(332),
		INVITING(341),
		SUMMONING(342),
		INVITELIST(346),
		ENDOFINVITELIST(347),
		EXCEPTLIST(348),
		ENDOFEXCEPTLIST(349),
		VERSION(351),
		WHOREPLY(352),
		ENDOFWHO(315),
		NAMREPLY(353),
		ENDOFNAMES(366),
		LINKS(364),
		ENDOFLINKS(365),
		BANLIST(367),
		ENDOFBANLIST(368),
		INFO(371),
		ENDOFINFO(374),
		MOTDSTART(375),
		MOTD(372),
		ENDOFMOTD(376),
		YOUREOPER(381),
		REHASHING(382),
		YOURESERVICE(383),
		TIME(391),
		USERSSTART(392),
		USERS(393),
		ENDOFUSERS(394),
		NOUSERS(395),
		TRACELINK(200),
		TRACECONNECTING(201),
		TRACEHANDSHAKE(202),
		TRACEUNKNOWN(203),
		TRACEOPERATOR(204),
		TRACEUSER(205),
		TRACESERVER(206),
		TRACESERVICE(207),
		TRACENEWTYPE(208),
		TRACECLASS(209),
		TRACERECONNECT(210),
		TRACELOG(261),
		TRACEEND(262),
		STATSLINKINFO(211),
		STATSCOMMANDS(212),
		ENDOFSTATS(219),
		STATSUPTIME(242),
		STATSOLINE(243),
		UMODEIS(221),
		SERVLIST(234),
		SERVLISTEND(235),
		LUSERCLIENT(251),
		LUSEROP(252),
		LUSERUNKNOWN(253),
		LUSERCHANNELS(254),
		LUSERME(255),
		ADMINME(256),
		ADMINLOC1(257),
		ADMINLOC2(258),
		ADMINEMAIL(259),
		TRYAGAIN(263);

		private final int number;
		Reply(int number) {
			this.number = number;
			Info.parseMap.put(Integer.toString(number), this);
		}
		@Override
		public int getNumber() { return number; }
		@Override
		public Info getInfo() { return Info.REPLY; }
		@Override
		public String toString() { return "RPL_"+this.name(); }
	}

	enum Error implements Response {
		NOSUCHNICK(401),
		NOSUCHSERVER(402),
		NOSUCHCHANNEL(403),
		CANNOTSENDTOCHAN(404),
		TOOMANYCHANNELS(405),
		WASNOSUCHNICK(406),
		TOOMANYTARGETS(407),
		NOSUCHSERVICE(408),
		NOORIGIN(409),
		NORECIPIENT(411),
		NOTEXTTOSEND(412),
		NOTOPLEVEL(413),
		WILDTOPLEVEL(414),
		BADMASK(415),
		UNKNOWNCOMMAND(421),
		NOMOTD(422),
		NOADMININFO(423),
		FILEERROR(424),
		NONICKNAMEGIVEN(431),
		ERRONEUSNICKNAME(432),
		NICKNAMEINUSE(433),
		NICKCOLLISION(436),
		UNAVAILRESOURCE(437),
		USERNOTINCHANNEL(441),
		NOTONCHANNEL(442),
		USERONCHANNEL(443),
		NOLOGIN(444),
		SUMMONDISABLED(445),
		USERSDISABLED(446),
		NOTREGISTERED(451),
		NEEDMOREPARAMS(461),
		ALREADYREGISTRED(462),
		NOPERMFORHOST(463),
		PASSWDMISMATCH(464),
		YOUREBANNEDCREEP(465),
		YOUWILLBEBANNED(466),
		KEYSET(467),
		CHANNELISFULL(471),
		UNKNOWNMODE(472),
		INVITEONLYCHAN(473),
		BANNEDFROMCHAN(474),
		BADCHANNELKEY(475),
		BADCHANMASK(476),
		NOCHANMODES(477),
		BANLISTFULL(478),
		NOPRIVILEGES(481),
		CHANOPRIVSNEEDED(482),
		CANTKILLSERVER(483),
		RESTRICTED(484),
		UNIQOPPRIVSNEEDED(485),
		NOOPERHOST(491),
		UMODEUNKNOWNFLAG(501),
		USERSDONTMATCH(502);

		private final int number;
		Error(int number) {
			this.number = number;
			Info.parseMap.put(Integer.toString(number), this);
		}
		@Override
		public Info getInfo() { return Info.ERROR; }
		@Override
		public int getNumber() { return number; }
		@Override
		public boolean clientServerOnly() { return false; }
		@Override
		public String toString() { return "ERR_"+this.name(); }
	}
}
