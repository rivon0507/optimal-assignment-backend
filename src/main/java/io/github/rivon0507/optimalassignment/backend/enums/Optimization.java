package io.github.rivon0507.optimalassignment.backend.enums;

import lombok.Getter;

@Getter
public enum Optimization {
    MINIMIZATION("min"),
    MAXIMIZATION("max");

    Optimization(String value) {
        this.value = value;
    }

    private final String value;
}
