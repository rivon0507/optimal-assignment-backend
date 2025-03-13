package io.github.rivon0507.optimalassignment.backend.util;

import io.github.rivon0507.or.assignmentproblem.Coord;
import org.jetbrains.annotations.NotNull;

public record Coords(
        int r,
        int c
) {
    public Coords(@NotNull Coord c) {
        this(c.row(), c.col());
    }
}
