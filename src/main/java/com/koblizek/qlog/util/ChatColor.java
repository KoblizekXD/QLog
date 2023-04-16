package com.koblizek.qlog.util;

public enum ChatColor {
    RED("c"),
    YELLOW("e"),
    GOLD("6"),
    BLACK("0"),
    WHITE("f"),
    PINK("d"),
    GREEN("a"),
    DARK_GREEN("2"),
    AQUA("b");

    private static final String CHAT_CHAR = "\u00A7";

    private final String ansi;

    ChatColor(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String toString() {
        return (CHAT_CHAR + ansi).trim();
    }
}
