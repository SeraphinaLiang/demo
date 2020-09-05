package com.example.strangeclass.dao;

import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.HistoryCourse;
import com.example.strangeclass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryCourseRepository extends JpaRepository<HistoryCourse, Long> {

    HistoryCourse findByUserAndCourse(User user, Course course);

}
