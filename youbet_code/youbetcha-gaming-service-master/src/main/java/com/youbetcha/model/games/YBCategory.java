package com.youbetcha.model.games;

public enum YBCategory {

    SLOTS_AND_GAMES("SLOTSANDGAMES"),
    LIVE_CASINO("LIVECASINO"),
    TABLE_GAMES("TABLEGAMES"),
    VIRTUAL_SPORTS("VIRTUALSPORTS"),
    VIDEO_POKER("VIDEOPOKER"),
    NO_CATEGORY_GAME("NO_CATEGORY_GAME");

    private final String category;

    YBCategory(String category) {
        this.category = category;
    }

    public static YBCategory getYBCategoryByValue(String category) {
        for (YBCategory v : values()) {
            if (v.getYBCategoryValue().equals(category)) {
                return v;
            }
        }
        return NO_CATEGORY_GAME;
    }

    public static YBCategory transformEMToYBCategory(final String categories) {

        boolean isSlotGame = EMCategory.getYBSlotTypeGames()
                .stream()
                .anyMatch(emCategory -> categories.contains(emCategory.getCategoryValue()));

        if (isSlotGame) {
            return SLOTS_AND_GAMES;
        }

        if (categories.contains(EMCategory.LIVE_DEALER.getCategoryValue())) {
            return LIVE_CASINO;
        }

        if (categories.contains(EMCategory.TABLE_GAMES.getCategoryValue())) {
            return TABLE_GAMES;
        }

        if (categories.contains(EMCategory.VIRTUAL_SPORTS.getCategoryValue())) {
            return VIRTUAL_SPORTS;
        }

        if (categories.contains(EMCategory.VIDEO_POKERS.getCategoryValue())) {
            return VIDEO_POKER;
        }

        return NO_CATEGORY_GAME;
    }

    public static boolean isEMGameIsJackPot(String categories) {
        String jackPot = EMCategory.JACKPOT_GAMES.getCategoryValue();
        if (categories.contains(jackPot)) {
            return true;
        }
        return false;
    }

    public String getYBCategoryValue() {
        return this.category;
    }


}
