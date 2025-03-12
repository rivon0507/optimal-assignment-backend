package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.dto.AssignmentRequest;
import io.github.rivon0507.optimalassignment.backend.dto.AssignmentSolverResponse;
import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.repository.SolverJobRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class AssignmentJobManagerTest {
    @Mock
    private SolverJobRepository solverJobRepository;

    @Mock
    private SolverSnapshotService solverSnapshotService;

    @Mock
    private AsyncAssignmentSolver asyncAssignmentSolver;

    @InjectMocks
    private AssignmentJobManager assignmentJobManager;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    class LaunchJobTest {
        @Test
        void shouldReturnTheJobId() {
            String jobId = "an-id";
            SolverJob job = SolverJob.builder().id(jobId).status(JobStatus.IN_PROGRESS).build();
            given(solverJobRepository.save(any())).willReturn(job);
            given(asyncAssignmentSolver.startSolvingJob(any(), any(), any())).willReturn(null);
            String uuid = assignmentJobManager.launchJob(new AssignmentRequest("min", new long[][]{{1, 2}, {3, 4}}));
            assertThat(uuid).isEqualTo(jobId);
        }
    }

    @Nested
    class GetJobTest {
        @Test
        void jobFound() {
            String jobId = "an-id";
            SolverJob job = SolverJob.builder().id(jobId).status(JobStatus.IN_PROGRESS).build();
            given(solverJobRepository.findById(jobId)).willReturn(Optional.of(job));
            given(solverSnapshotService.retrieveSnapshots(any())).willReturn(List.of());
            AssignmentSolverResponse response = assignmentJobManager.getJob(jobId);
            assertThat(response).isNotNull();
            assertThat(response.job()).isEqualTo(job);
            assertThatIterable(response.snapshots()).isEqualTo(List.of());
        }

        @Test
        void jobNotFound() {
            String jobId = "an-id";
            given(solverJobRepository.findById(jobId)).willReturn(Optional.empty());
            assertThatThrownBy(() -> assignmentJobManager.getJob(jobId)).isInstanceOf(NoSuchElementException.class);
        }
    }
}