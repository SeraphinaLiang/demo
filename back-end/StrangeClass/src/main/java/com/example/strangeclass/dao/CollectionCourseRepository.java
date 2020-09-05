package com.example.strangeclass.dao;

import com.example.strangeclass.entity.CollectionCourse;
import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionCourseRepository extends JpaRepository<CollectionCourse, Long> {

    List<CollectionCourse> findAllByUser(User user);

    CollectionCourse findByUserAndCourse(User userDB, Course course);
}
