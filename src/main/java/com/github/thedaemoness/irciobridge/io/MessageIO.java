package com.github.thedaemoness.irciobridge.io;

import com.github.thedaemoness.irciobridge.messages.MessageIn;
import com.github.thedaemoness.irciobridge.messages.MessageOut;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface MessageIO extends AutoCloseable, Supplier<MessageIn>, Consumer<MessageOut> {}
