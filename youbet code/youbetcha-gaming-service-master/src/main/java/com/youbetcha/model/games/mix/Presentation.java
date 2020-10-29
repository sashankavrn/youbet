package com.youbetcha.model.games.mix;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {
    GameName gameName;
    TableName tableName;
    JackpotName jackpotName;
    ShortName shortName;
    Description description;
    Thumbnail thumbnail;
    Logo logo;
    BackgroundImage backgroundImage;
    //Icons icons;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class GameName {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class TableName {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class JackpotName {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ShortName {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Description {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BackgroundImage {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Logo {
        @SerializedName("*")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Thumbnail {
        @SerializedName("*")
        private String value;
    }
}


