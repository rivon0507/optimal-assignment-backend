package io.github.rivon0507.optimalassignment.backend.service;

import io.github.rivon0507.optimalassignment.backend.dto.AssignmentRequest;
import io.github.rivon0507.optimalassignment.backend.dto.AssignmentSolverResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssignmentJobService {

    public String launchJob(AssignmentRequest request) {
        return UUID.randomUUID().toString();
    }

    public AssignmentSolverResponse getJob(String jobId) {
        return null;
    }
}
