package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.model.SolverSnapshot;
import io.github.rivon0507.optimalassignment.backend.repository.SolverSnapshotRepository;
import io.github.rivon0507.or.assignmentproblem.AssignmentSolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class SolverSnapshotService {
    private final RedisTemplate<String, String> redisTemplate;
    private final SolverSnapshotRepository solverSnapshotRepository;

    public SolverSnapshotService(RedisTemplate<?, ?> redisTemplate, SolverSnapshotRepository solverSnapshotRepository) {
        //noinspection unchecked
        this.redisTemplate = (RedisTemplate<String, String>) redisTemplate;
        this.solverSnapshotRepository = solverSnapshotRepository;
    }

    public SolverSnapshot saveSnapshot(String jobId, String currentStep, AssignmentSolver solver) {
        SolverSnapshot snapshot = SolverSnapshot.builder()
                .withSnapshot(solver)
                .withCurrentStep(currentStep)
                .withJobId(jobId)
                .build();
        SolverSnapshot saved = solverSnapshotRepository.save(snapshot);
        String key = "solverJob:" + jobId + ":snapshots";
        redisTemplate.opsForList().rightPush(key, saved.getId());
        redisTemplate.expire(key, Duration.ofMinutes(15));
        return saved;
    }

    public List<SolverSnapshot> retrieveSnapshots(String jobId) {
        String key = "solverJob:" + jobId + ":snapshots";
        long count = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
        List<String> strings = redisTemplate.opsForList().leftPop(key, count);
        if (strings == null) {
            return List.of();
        }
        List<SolverSnapshot> snapshotList = strings.stream()
                .map(solverSnapshotRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        solverSnapshotRepository.deleteAll(snapshotList);
        return snapshotList;
    }
}
