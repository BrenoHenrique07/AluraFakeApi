package br.com.alura.AluraFake.domain.task.repository;

import br.com.alura.AluraFake.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByCourseIdAndStatement(Long courseId, String statement);
    int countByCourseId(Long courseId);
    List<Task> findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(Long courseId, int order);
}
