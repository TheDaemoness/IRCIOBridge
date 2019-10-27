package com.github.thedaemoness.irciobridge.util;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class StringAccumulator<T> implements Consumer<String>, Supplier<T> {
	final public StringAccumulator<T> append(Object what) {
		accept(what.toString());
		return this;
	}

	public static class StringBuilder extends StringAccumulator<String> implements CharSequence {
		private final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		@Override
		public void accept(String s) { sb.append(s); }
		@Override
		public String get() { return sb.toString(); }
		@Override
		public int length() { return sb.length(); }
		@Override
		public char charAt(int i) { return sb.charAt(i); }
		@Override
		public CharSequence subSequence(int i, int e) { return sb.subSequence(i, e); }
		@Override
		public IntStream chars() { return sb.chars(); }
		@Override
		public IntStream codePoints() { return sb.codePoints(); }
		@Override
		@Nonnull
		public String toString() { return sb.toString(); }
	}
	public static class StringLength extends StringAccumulator<Integer> {
		private int count = 0;
		@Override
		public void accept(String s) { count += s.length(); }
		@Override
		public Integer get() { return count; }
	}
}
