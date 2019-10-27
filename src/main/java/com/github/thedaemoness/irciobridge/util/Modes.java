package com.github.thedaemoness.irciobridge.util;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Modes {
	//TODO: Parametered modes.
	public class Mode implements Comparable<Character> {
		private final char id;
		private Mode(char id) {
			this.id = id;
		}
		@Override
		public int compareTo(@Nonnull Character character) {
			return Character.compare(id, character);
		}
		@Override
		public int hashCode() {
			return id;
		}
	}
	protected final Map<Character, Mode> modes = new TreeMap<Character, Mode>();
	public Modes(String modelist) {
		modelist.chars().forEach(i -> {
			final char c = (char)i; //modelist.chars()
			modes.putIfAbsent(c, new Mode(c));
		});
	}
	public final Set<Mode> getModes(String modelist) {
		final Set<Mode> modeset = new TreeSet<Mode>();
		modelist.chars().forEach(i -> {
			final Mode mode = modes.get((char)i);
			if(mode == null) throw new IllegalArgumentException("No mode with character code "+i+" in "+this);
			modeset.add(mode);
		});
		return modeset;
	}
}
