package com.youbetcha.model.games.mix;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    //table specific
    private String category;
    private boolean enabled;
    private boolean isOpen;
    //end table
    private long id;
    private String slug;
    private String vendor;
    private String contentProvider;
    private String gameCode;
    private String gameID;
    private List<String> categories;
    private List<String> tags;
    private float theoreticalPayOut;
    private float thirdPartyFee;
    private float fpp;
    private List<String> restrictedTerritories;
    private List<String> languages;
    private List<String> currencies;
    private String url;
    private String helpUrl;
    private Creation creation;
    private Property property;
    private OpeningTime openingTime;
    private HitFrequency HitFrequencyObject;
    private Report report;
    private Presentation presentation;
    private Popularity popularity;
    private PlayMode playMode;
    private Jackpot jackpot;
    private Bonus bonus;

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Report {
        private String category;
        private String invoicingGroup;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bonus {
        @JsonProperty("*")
        private float contribution;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Jackpot {
        private String type;
        private float contribution;
        private boolean contributionEnable;
        private boolean calculateJackpotContribution;
        private Contributions ContributionsObject;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contributions {
        private float Global;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayMode {
        private boolean fun;
        private boolean anonymity;
        private boolean realMoney;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Popularity {
        private float coefficient;
        private float ranking;
        private Object rankingDetail;
    }

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OpeningTime {
        private boolean is24HoursOpen;
        private String timeZone;
        private String startTime;
        private String endTime;

        @Override
        public String toString() {
            return "{" +
                    "is24HoursOpen:" + is24HoursOpen + "," +
                    "timeZone:" + timeZone + "," +
                    "startTime:" + startTime + "," +
                    "endTime:" + endTime + "" +
                    '}';
        }
    }
}
