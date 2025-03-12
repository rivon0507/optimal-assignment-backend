package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.model.SolverSnapshot;
import io.github.rivon0507.optimalassignment.backend.repository.SolverJobRepository;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class SolverSnapshotServiceTest {
    @Autowired
    private SolverJobRepository solverJobRepository;
    @Autowired
    private SolverSnapshotService solverSnapshotService;

    @Test
    void serviceCorrectlySaveTheSnapshotsToRedis() {
        long[][] matrix = {
                {14, 6, 18, 16, 63, 15},
                {41, 78, 44, 73, 70, 25},
                {44, 81, 36, 80, 80, 78},
                {46, 74, 5, 25, 83, 3},
                {72, 32, 55, 51, 3, 81},
                {69, 76, 12, 99, 83, 80}
        };
        AssignmentSolver solver = new AssignmentSolver();
        solver.configure(matrix, AssignmentSolver.OptimizationType.MAXIMIZE);
        String uuid = UUID.randomUUID().toString();
        List<SolverSnapshot> expectedSnapshots = new ArrayList<>();
        solverJobRepository.save(SolverJob.builder().id(uuid).status(JobStatus.IN_PROGRESS).build());
        solver.getNotificationHandler().addListener(1, (step, theSolver) -> {
                    SolverJob job = solverJobRepository.findById(uuid).orElseThrow();
                    expectedSnapshots.add(solverSnapshotService.saveSnapshot(job.getId(), step.name(), theSolver));
                }
        );
        solver.solve();
        SolverJob job = solverJobRepository.findById(uuid).orElseThrow();
        List<SolverSnapshot> snapshots = solverSnapshotService.retrieveSnapshots(job.getId());
        SolverJob.Result result = new SolverJob.Result(solver.getOptimalValue(), solver.getSolution());
        job.setResult(result);
        solverJobRepository.save(job);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThatIterable(snapshots)
                .as("Snapshots should be saved to redis")
                .hasSameElementsAs(expectedSnapshots);
        softly.assertThat(solverJobRepository.findById(uuid).orElseThrow().getResult())
                .as("Result should exist after solving finished")
                .isEqualTo(result);
        softly.assertAll();
    }
}