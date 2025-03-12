package io.github.rivon0507.optimalassignment.backend.model;

import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Arrays;

@RedisHash(value = "solverJob", timeToLive = 900)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolverJob {
    @Id
    private String id;
    private JobStatus status;
    private Result result;

    public record Result(
            long optimalValue,
            int[] solution
    ) {
        public Result(long optimalValue, long[] solution) {
            this(optimalValue, Arrays.stream(solution).mapToInt(l -> (int) l).toArray());
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Result(long value, int[] solution1)) {
                return optimalValue == value && Arrays.equals(solution, solution1);
            }
            return false;
        }
    }
}
