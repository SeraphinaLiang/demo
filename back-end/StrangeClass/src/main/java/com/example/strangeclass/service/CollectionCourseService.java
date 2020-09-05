package com.example.strangeclass.service;

import com.example.strangeclass.dao.CollectionCourseRepository;
import com.example.strangeclass.entity.CollectionCourse;
import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.vo.CourseIdVo;
import com.example.strangeclass.vo.CourseListVo;
import com.example.strangeclass.vo.CourseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionCourseService {

    @Autowired
    private CollectionCourseRepository collectionCourseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    /**
     * 收藏一个课程
     *
     * @param courseIdVo
     * @param user
     * @return
     */
    public void addCollectionCourse(CourseIdVo courseIdVo, User user) throws Exception {
        User userDB = userService.findByName(user.getName());

        Optional<Course> optionalCourseDB = courseService.findById(courseIdVo.getCourseId());

        if (!optionalCourseDB.isPresent()) {
            throw new Exception("该课程不存在");
        }
        List<CollectionCourse> collectionCourseList = userDB.getCollectionCourseList();
        for (CollectionCourse collectionCourse : collectionCourseList) {
            if (collectionCourse.getCourse().getId().equals(courseIdVo.getCourseId())) {
                throw new Exception("该用户已经收藏过该课程");
            }
        }

        Course courseDB = optionalCourseDB.get();
        CollectionCourse collectionCourseDB = new CollectionCourse(userDB, courseDB);
        collectionCourseDB.createTime();
        collectionCourseRepository.save(collectionCourseDB);
    }

    /**
     * 查看所有收藏课程
     *
     * @param userName
     * @return
     */
    public CourseListVo getCollectionCourseList(String userName) throws Exception {
        User userDB = userService.findByName(userName);

        CourseListVo courseListVo = new CourseListVo();
        List<CollectionCourse> collectionCourseList = userDB.getCollectionCourseList();
        for (CollectionCourse collectionCourse : collectionCourseList) {
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(collectionCourse.getCourse(), courseVo);
            BeanUtils.copyProperties(collectionCourse.getCourse().getTeacher(), courseVo.getTeacher());
            courseVo.setCollected(true);
            courseListVo.getCourseVoList().add(courseVo);
        }

        return courseListVo;
    }

    public void cancelCollectionCourse(CourseIdVo courseIdVo, User user) throws Exception {
        User userDB = userService.findByName(user.getName());
        Optional<Course> optionalCourseDB = courseService.findById(courseIdVo.getCourseId());

        if (!optionalCourseDB.isPresent()) {
            throw new Exception("该课程不存在");
        }

        CollectionCourse collectionCourseDB = collectionCourseRepository.findByUserAndCourse(userDB, optionalCourseDB.get());
        if (collectionCourseDB == null) {
            throw new Exception("该课程已不在用户收藏列表");
        } else {
            collectionCourseRepository.delete(collectionCourseDB);
        }

    }

    public CollectionCourse findByUserAndCourse(User user, Course course) {
        return collectionCourseRepository.findByUserAndCourse(user, course);
    }
}
