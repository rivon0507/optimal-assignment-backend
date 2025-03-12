package io.github.rivon0507.optimalassignment.backend.enums;

import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum Optimization {
    MINIMIZATION("min"),
    MAXIMIZATION("max");

    Optimization(String value) {
        this.value = value;
    }

    public AssignmentSolver.OptimizationType toOptimizationType() {
        return switch (this) {
            case MINIMIZATION -> AssignmentSolver.OptimizationType.MINIMIZE;
            case MAXIMIZATION -> AssignmentSolver.OptimizationType.MAXIMIZE;
        };
    }

    public static @NotNull Optimization of(String s) {
        for (Optimization optimization : values()) {
            if (optimization.value.equals(s)) {
                return optimization;
            }
        }
        throw new IllegalArgumentException("No enum constant io.github.rivon0507.optimalassignment.backend.enums.Optimization." + s);
    }

    private final String value;
}
