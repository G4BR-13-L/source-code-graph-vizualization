package com.ti.youtubeminer.kind;

public enum KindEnum {

    YOUTUBE_SEARCH_LIST("youtube#searchResult"),
    YOUTUBE_VIDEO_LIST("youtube#videoListResponse"),
    YOUTUBE_VIDEO("youtube#video");

    String value;

    KindEnum(String value) {
        this.value = value;
    }

    public static final String KIND_NULL_CHECK_CONSTRAINT = "VARCHAR (255) CHECK (kind IN (" +
            "'YOUTUBE_SEARCH_LIST'," +
            "'YOUTUBE_VIDEO_LIST'," +
            "'YOUTUBE_VIDEO'" +
            "))";

}
