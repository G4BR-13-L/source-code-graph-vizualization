package com.ti.youtubeminer.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum AnalyzedAsEnum {

    PYTHON,
    JAVA,
    C,
    CPP,
    CSHARP,
    JAVASCRIPT,
    TYPESCRIPT,
    PHP,
    SHELL,
    RUBY,
    OTHER,
    PROGRAMMING_LANGUAGE_NOT_IDENTIFIED;

    public static final String ANALYZED_AS_NULL_CHECK_CONSTRAINT = "VARCHAR (255) CHECK (analyzed_as IN (" +
            "'PYTHON', " +
            "'JAVA', " +
            "'C', " +
            "'CPP', " +
            "'CSHARP', " +
            "'JAVASCRIPT', " +
            "'TYPESCRIPT', " +
            "'PHP', " +
            "'SHELL', " +
            "'RUBY', " +
            "'OTHER'," +
            "'PROGRAMMING_LANGUAGE_NOT_IDENTIFIED'" +
            "))";
}

