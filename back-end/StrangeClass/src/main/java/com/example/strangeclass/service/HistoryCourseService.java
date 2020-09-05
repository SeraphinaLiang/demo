package com.example.strangeclass.service;

import com.example.strangeclass.dao.HistoryCourseRepository;
import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.HistoryCourse;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.vo.CourseIdVo;
import com.example.strangeclass.vo.CourseListVo;
import com.example.strangeclass.vo.CourseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryCourseService {

    @Autowired
    private HistoryCourseRepository historyCourseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;


    public void addHistoryCourse(CourseIdVo courseIdVo, User user) throws Exception {
        User userDB = userService.findByName(user.getName());
        Optional<Course> optionalCourseDB = courseService.findById(courseIdVo.getCourseId());
        if (!optionalCourseDB.isPresent()) {
            throw new Exception("该课程不存在");
        }
        Course courseDB = optionalCourseDB.get();
        HistoryCourse historyCourseDB = historyCourseRepository.findByUserAndCourse(userDB, courseDB);
        if (historyCourseDB == null) {
            historyCourseDB = new HistoryCourse(userDB, courseDB);
            historyCourseDB.createTime();
            historyCourseDB.updateTime();
            historyCourseRepository.save(historyCourseDB);
        } else {
            historyCourseDB.updateTime();
            historyCourseRepository.save(historyCourseDB);
        }
    }


    public CourseListVo getHistoryCourseList(User user) throws Exception {
        User userDB = userService.findByName(user.getName());
        List<HistoryCourse> historyCourseList = userDB.getHistoryCourseList();
        historyCourseList.sort((o1, o2) -> {
            if (o1.getUpdateAt().after(o2.getUpdateAt())) {
                return 1;
            }
            return -1;
        });
        Collections.reverse(historyCourseList);

        CourseListVo courseListVo = new CourseListVo();
        for (HistoryCourse historyCourse : historyCourseList) {
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(historyCourse.getCourse(), courseVo);
            BeanUtils.copyProperties(historyCourse.getCourse().getTeacher(), courseVo.getTeacher());
            courseVo.setCollected(courseService.isCollected(historyCourse.getCourse().getId(), user.getId()));
            courseListVo.getCourseVoList().add(courseVo);
        }

        return courseListVo;
    }


    public void deleteHistoryCourse(CourseIdVo courseIdVo, User user) throws Exception {
        User userDB = userService.findByName(user.getName());
        Optional<Course> optionalCourseDB = courseService.findById(courseIdVo.getCourseId());
        if (!optionalCourseDB.isPresent()) {
            throw new Exception("该课程不存在");
        }
        Course courseDB = optionalCourseDB.get();
        HistoryCourse historyCourseDB = historyCourseRepository.findByUserAndCourse(userDB, courseDB);
        if (historyCourseDB == null) {
            throw new Exception("用户没有观看该课程的记录");
        } else {
            historyCourseRepository.delete(historyCourseDB);
        }
    }
}
