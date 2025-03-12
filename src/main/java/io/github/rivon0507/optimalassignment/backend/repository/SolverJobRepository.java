package io.github.rivon0507.optimalassignment.backend.repository;

import io.github.rivon0507.optimalassignment.backend.model.SolverJob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolverJobRepository extends CrudRepository<SolverJob, String> {
}
