package by.itacademy.profiler.persistence.repository;

import by.itacademy.profiler.persistence.model.Course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
