package br.com.alura.AluraFake.domain.course.repository;

import br.com.alura.AluraFake.domain.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    List<Course> findByInstructorId(Long instructorId);
}
