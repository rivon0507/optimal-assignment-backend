package io.github.rivon0507.optimalassignment.backend.model;

import io.github.rivon0507.optimalassignment.backend.util.Coords;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RedisHash(value = "solverSnapshot", timeToLive = 600)
@Data
public class SolverSnapshot {
    @Id
    private String id;
    private String jobId;
    private String currentStep;
    private AssignmentSolver.SolverState state;
    private List<String> matrix;
    private AssignmentSolver.OptimizationType optimizationType;
    private Long[] solution;
    private Long optimalValue;
    private Long ceiling;
    private Integer[] markedRows;
    private Integer[] markedCols;
    private Coords[] framedZeroes;
    private Coords[] struckOutZeroes;
    private Integer[] rowMinCols;
    private Integer[] colMinRow;

    @Contract(mutates = "this")
    public void copySnapshot(@NotNull AssignmentSolver solver) {
        setState(solver.getState());
        setMatrix(Arrays.stream(solver.getMatrix())
                .map(longs -> Arrays.stream(longs)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "))
                )
                .collect(Collectors.toList())
        );
        setOptimizationType(solver.getOptimization());
        setSolution(solver.isSolved() ? Arrays.stream(solver.getSolution()).boxed().toArray(Long[]::new) : null);
        setOptimalValue(solver.getOptimalValue());
        setCeiling(solver.getCeiling());
        setMarkedRows(solver.getMarkedRows().isEmpty() ? null : solver.getMarkedRows().toArray(Integer[]::new));
        setMarkedCols(solver.getMarkedCols().isEmpty() ? null : solver.getMarkedCols().toArray(Integer[]::new));
        setFramedZeroes(solver.getFramedZeroes().isEmpty() ? null : solver.getFramedZeroes().stream().map(Coords::new).toArray(Coords[]::new));
        setStruckOutZeroes(solver.getStruckOutZeroes().isEmpty() ? null : solver.getStruckOutZeroes().stream().map(Coords::new).toArray(Coords[]::new));
        setRowMinCols(solver.getRowMinCols().isEmpty() ? null : solver.getRowMinCols().toArray(Integer[]::new));
        setColMinRow(solver.getColMinRows().isEmpty() ? null : solver.getColMinRows().toArray(Integer[]::new));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull SolverSnapshot.SolverSnapshotBuilder builder() {
        return new SolverSnapshotBuilder();
    }

    public static class SolverSnapshotBuilder {
        private final SolverSnapshot snapshot = new SolverSnapshot();

        private SolverSnapshotBuilder() {
        }

        public SolverSnapshotBuilder withSnapshot(AssignmentSolver solver) {
            snapshot.copySnapshot(solver);
            return this;
        }

        public SolverSnapshotBuilder withCurrentStep(@NotNull String currentStep) {
            snapshot.setCurrentStep(currentStep);
            return this;
        }

        public SolverSnapshotBuilder withJobId(String jobId) {
            snapshot.setJobId(jobId);
            return this;
        }

        public SolverSnapshot build() {
            return snapshot;
        }
    }

}
