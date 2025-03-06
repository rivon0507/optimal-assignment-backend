package io.github.rivon0507.optimalassignment.backend.model;

import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver.OptimizationType;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver.SolverState;
import io.github.rivon0507.or.assignmentproblem.Coord;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record AssignmentSolverSnapshot(
        SolverState state,
        long[][] matrix,
        OptimizationType optimizationType,
        long[] solution,
        long optimalValue,
        long ceiling,
        int[] markedRows,
        int[] markedCols,
        Coord[] framedZeroes,
        Coord[] struckOutZeroes,
        int[] rowMinCols,
        int[] colMinRows
) {
    @Contract("_ -> new")
    public static @NotNull AssignmentSolverSnapshot fromSolver(@NotNull AssignmentSolver solver) {
        return new AssignmentSolverSnapshot(
                solver.getState(),
                solver.getMatrix(),
                solver.getOptimization(),
                solver.getSolution(),
                solver.getOptimalValue(),
                solver.getCeiling(),
                solver.getMarkedRows().stream().mapToInt(Integer::intValue).toArray(),
                solver.getRowMinCols().stream().mapToInt(Integer::intValue).toArray(),
                solver.getFramedZeroes().toArray(new Coord[0]),
                solver.getStruckOutZeroes().toArray(new Coord[0]),
                solver.getRowMinCols().stream().mapToInt(Integer::intValue).toArray(),
                solver.getColMinRows().stream().mapToInt(Integer::intValue).toArray()
        );
    }
}
