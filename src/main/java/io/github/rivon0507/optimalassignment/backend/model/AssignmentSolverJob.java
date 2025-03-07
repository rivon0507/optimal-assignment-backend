package io.github.rivon0507.optimalassignment.backend.model;

import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;

public record AssignmentSolverJob(
        String jobId,
        JobStatus status,
        String currentStep,
        AssignmentSolverSnapshot solver
) {
}
