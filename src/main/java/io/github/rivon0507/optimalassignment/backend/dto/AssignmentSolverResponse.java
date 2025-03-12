package io.github.rivon0507.optimalassignment.backend.dto;

import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.model.SolverSnapshot;

import java.util.List;

public record AssignmentSolverResponse(
        SolverJob job,
        List<SolverSnapshot> snapshots
) {
}
