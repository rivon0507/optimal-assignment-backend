package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import io.github.rivon0507.optimalassignment.backend.enums.Optimization;
import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.repository.SolverJobRepository;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncAssignmentSolver {
    private final SolverJobRepository solverJobRepository;
    private final SolverSnapshotService solverSnapshotService;

    public AsyncAssignmentSolver(SolverJobRepository solverJobRepository, SolverSnapshotService solverSnapshotService) {
        this.solverJobRepository = solverJobRepository;
        this.solverSnapshotService = solverSnapshotService;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Async
    public Future<Void> startSolvingJob(@NotNull Optimization optimization, long[][] matrix, String jobId) {
        AssignmentSolver solver = new AssignmentSolver();
        solver.configure(matrix, optimization.toOptimizationType());
        SolverJob job = solverJobRepository.findById(jobId).orElseThrow();
        solver.getNotificationHandler().addListener(1, (step, theSolver) -> {
            log.info("Job {} --- Step #{}: {}", jobId, step, step.name());
            solverSnapshotService.saveSnapshot(jobId, step.name(), theSolver);
        });
        solver.solve();

        job.setResult(new SolverJob.Result(solver.getOptimalValue(), solver.getSolution()));
        job.setStatus(JobStatus.COMPLETED);
        solverJobRepository.save(job);
        return CompletableFuture.completedFuture(null);
    }
}
