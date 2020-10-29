package com.youbetcha.model.games;

public enum Terminal {

    DESKTOP("PC"),
    MOBILE("IPAD", "IPHONE", "ANDROID"),
    CROSS_PLATFORM("PC", "IPAD", "IPHONE", "ANDROID");

    private String pc;
    private String iPad;
    private String iPhone;
    private String android;

    Terminal(String pc, String iPad, String iPhone, String android) {
        this(iPad, iPhone, android);
        this.pc = pc;
    }

    Terminal(String iPad, String iPhone, String android) {
        this.iPad = iPad;
        this.iPhone = iPhone;
        this.android = android;
    }

    Terminal(String pc) {
        this.pc = pc;
    }

    public static Terminal transformEMTerminalToDBType(String terminals) {
        final boolean mobile = terminals.toUpperCase().contains(MOBILE.iPad)
                || terminals.toUpperCase().contains(MOBILE.iPhone)
                || terminals.toUpperCase().contains(MOBILE.android);

        if (mobile && terminals.toUpperCase().contains(DESKTOP.pc)) {
            return Terminal.CROSS_PLATFORM;
        }

        if (mobile) {
            return Terminal.MOBILE;
        }

        return Terminal.DESKTOP;
    }
}
