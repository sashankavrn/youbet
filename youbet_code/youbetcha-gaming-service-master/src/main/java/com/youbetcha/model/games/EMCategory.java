package com.youbetcha.model.games;

import java.util.EnumSet;

public enum EMCategory {

    //Game
    BINGO("BINGO"),
    CLASSICS_LOTS("CLASSICSLOTS"),
    LOTTERY("LOTTERY"),
    OTHER_GAMES("OTHERGAMES"),
    OTHER_SLOTS("OTHERSLOTS"),
    SCRATCH_CARDS("SCRATCHCARDS"),
    VIDEO_SLOTS("VIDEOSLOTS"),
    THREE_D_SLOTS("3DSLOTS"),
    VIDEO_POKERS("VIDEOPOKERS"),
    VIRTUAL_SPORTS("VIRTUALSPORTS"),

    //Table
    TABLE_GAMES("TABLEGAMES"),
    LIVE_DEALER("LIVEDEALER"),

    //Game //Table
    JACKPOT_GAMES("JACKPOTGAMES");

    private String category;

    EMCategory(String category) {
        this.category = category;
    }

    public static final EnumSet<EMCategory> getYBSlotTypeGames() {
        EnumSet<EMCategory> slotGames = EnumSet.of(EMCategory.BINGO, EMCategory.CLASSICS_LOTS,
                EMCategory.LOTTERY, EMCategory.OTHER_GAMES, EMCategory.OTHER_SLOTS, EMCategory.SCRATCH_CARDS,
                EMCategory.VIDEO_SLOTS, EMCategory.THREE_D_SLOTS);
        return slotGames;
    }

    public String getCategoryValue() {
        return this.category;
    }
}
