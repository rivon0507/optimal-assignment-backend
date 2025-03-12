package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.dto.AssignmentRequest;
import io.github.rivon0507.optimalassignment.backend.dto.AssignmentSolverResponse;
import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import io.github.rivon0507.optimalassignment.backend.enums.Optimization;
import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.repository.SolverJobRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AssignmentJobManager {

    private final SolverJobRepository solverJobRepository;
    private final SolverSnapshotService solverSnapshotService;
    private final AsyncAssignmentSolver asyncAssignmentSolver;

    public AssignmentJobManager(SolverJobRepository solverJobRepository, SolverSnapshotService solverSnapshotService, AsyncAssignmentSolver asyncAssignmentSolver) {
        this.solverJobRepository = solverJobRepository;
        this.solverSnapshotService = solverSnapshotService;
        this.asyncAssignmentSolver = asyncAssignmentSolver;
    }

    public String launchJob(@NotNull AssignmentRequest request) {
        SolverJob job = solverJobRepository.save(SolverJob.builder().status(JobStatus.IN_PROGRESS).build());
        asyncAssignmentSolver.startSolvingJob(Optimization.of(request.optimization()), request.matrix(), job.getId());
        return job.getId();
    }

    public AssignmentSolverResponse getJob(String jobId) {
        SolverJob job = solverJobRepository.findById(jobId).orElseThrow();
        return new AssignmentSolverResponse(
                job,
                solverSnapshotService.retrieveSnapshots(job.getId())
        );
    }
}
