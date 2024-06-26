package com.ti.youtubeminer.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ProgrammingLanguageEnum {

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
    NOT_SPECIFIED;

    public static final String PROGRAMMING_LANGUAGE_NULL_CHECK_CONSTRAINT = "VARCHAR (255) CHECK (programming_language IN (" +
            "'PYTHON', " +
            "'JAVA', " +
            "'C', " +
            "'CPP', " +
            "'CSHARP', " +
            "'JAVASCRIPT', " +
            "'TYPESCRIPT', " +
            "'PHP'," +
            "'SHELL', " +
            "'RUBY', " +
            "'OTHER'," +
            "'NOT_SPECIFIED'" +
            "))";
}
