package io.github.rivon0507.optimalassignment.backend.controller;

import io.github.rivon0507.optimalassignment.backend.dto.AssignmentRequest;
import io.github.rivon0507.optimalassignment.backend.dto.AssignmentSolverResponse;
import io.github.rivon0507.optimalassignment.backend.service.AssignmentJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/assignments")
public class AssignmentSolverController {
    private final AssignmentJobService assignmentJobService;

    @Autowired
    public AssignmentSolverController(AssignmentJobService assignmentJobService) {
        this.assignmentJobService = assignmentJobService;
    }

    @PostMapping
    public ResponseEntity<?> launchJob(@Validated @RequestBody AssignmentRequest request) {
        String jobId = assignmentJobService.launchJob(request);
        return ResponseEntity.accepted().body(Map.of("job_id", jobId));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJob(@PathVariable String jobId) {
        AssignmentSolverResponse job = assignmentJobService.getJob(jobId);
        return job == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(job);
    }
}
