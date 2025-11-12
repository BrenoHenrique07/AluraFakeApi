package br.com.alura.AluraFake.domain.task.repository;

import br.com.alura.AluraFake.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByCourseIdAndStatement(Long courseId, String statement);
    int countByCourseId(Long courseId);
    List<Task> findByCourseIdAndOrderGreaterThanEqualOrderByOrderAsc(Long courseId, int order);

    @Query("""
        SELECT t.course.id AS courseId, COUNT(t.id) AS totalTasks
        FROM Task t
        WHERE t.course.id IN (:courseIds)
        GROUP BY t.course.id
    """)
    List<Map<String, Object>> countTasksByCourseIds(@Param("courseIds") List<Long> courseIds);
}
