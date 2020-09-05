package com.example.strangeclass.dao;

import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByTeacher(User user);

    Course findByName(String name);
}
