package io.github.rivon0507.optimalassignment.backend.controller;

import io.github.rivon0507.optimalassignment.backend.dto.AssignmentSolverResponse;
import io.github.rivon0507.optimalassignment.backend.enums.JobStatus;
import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import io.github.rivon0507.optimalassignment.backend.model.SolverSnapshot;
import io.github.rivon0507.optimalassignment.backend.service.AssignmentJobService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AssignmentSolverController.class, includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {AssignmentJobService.class}
        )
})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class AssignmentSolverControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentJobService assignmentJobService;

    @Nested
    class LaunchJob {
        @Test
        void shouldReturnTheJobIdWith202Status() throws Exception {
            given(assignmentJobService.launchJob(any())).willReturn("a-good-id");
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": \"min\", \"matrix\": [[1, 2, 3], [4, 5, 6], [7, 8, 9]]}"))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.job_id").value("a-good-id"));
            verify(assignmentJobService).launchJob(argThat(assignmentRequest -> {
                long[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
                return assignmentRequest.optimization().equals("min")
                       && IntStream.range(0, 3).allMatch(i -> Arrays.equals(assignmentRequest.matrix()[i], matrix[i]));
            }));
        }

        @Test
        void withoutMatrixShouldReturn400() throws Exception {
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": 0}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
        }

        @Test
        void withNonArrayMatrixShouldReturn400() throws Exception {
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": 0, \"matrix\": 888}"))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": \"min\", \"matrix\": \"alala\"}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
        }

        @Test
        void withoutOptimizationShouldReturn400() throws Exception {
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"matrix\": [[1, 2], [3, 4]]}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
        }

        @Test
        void withInvalidOptimizationShouldReturn400() throws Exception {
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": -1, \"matrix\": [[1, 2, 3], [4, 5, 6], [7, 8, 9]]}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": 2, \"matrix\": [[1, 2, 3], [4, 5, 6], [7, 8, 9]]}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": \"hehe\", \"matrix\": [[1, 2, 3], [4, 5, 6], [7, 8, 9]]}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
            mockMvc.perform(post("/assignments")
                            .contentType("application/json")
                            .content("{\"optimization\": null, \"matrix\": [[1, 2, 3], [4, 5, 6], [7, 8, 9]]}"))
                    .andExpect(status().isBadRequest());
            verify(assignmentJobService, never()).launchJob(any());
        }
    }

    @Nested
    class GetJob {
        @Test
        void shouldReturnTheJobInfoWith200Status() throws Exception {
            String jobId = "a-good-id";
            AssignmentSolverResponse response = new AssignmentSolverResponse(
                    SolverJob.builder()
                            .id(jobId)
                            .status(JobStatus.IN_PROGRESS)
                            .result(new SolverJob.Result(800L, new int[]{0, 2, 1}))
                            .build(),
                    List.of(SolverSnapshot.builder()
                            .withJobId(jobId)
                            .withCurrentStep("a-step")
                            .build())
            );
            given(assignmentJobService.getJob(jobId)).willReturn(response);
            mockMvc.perform(get("/assignments/" + jobId))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.job.id").value(jobId),
                            jsonPath("$.job.status").value("IN_PROGRESS"),
                            jsonPath("$.job.result.optimalValue").value(800),
                            jsonPath("$.snapshots").isArray(),
                            jsonPath("$.snapshots[0].jobId").value(jobId),
                            jsonPath("$.snapshots[0].currentStep").value("a-step"),
                            jsonPath("$.snapshots[0].matrix").isEmpty()
                    );
            verify(assignmentJobService).getJob(eq(jobId));
        }

        @Test
        void shouldReturn404IfJobNotFound() throws Exception {
            String jobId = "a-good-id";
            given(assignmentJobService.getJob(jobId)).willReturn(null);
            mockMvc.perform(get("/assignments/" + jobId))
                    .andExpect(status().isNotFound());
        }
    }
}