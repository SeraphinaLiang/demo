package com.example.strangeclass.service;

import com.example.strangeclass.dao.ChatRecordRepository;
import com.example.strangeclass.entity.ChatRecord;
import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.vo.ChatRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRecordService {

    @Autowired
    private ChatRecordRepository chatRecordRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    /**
     * 保存聊天记录
     *
     * @param userName
     * @param chatRecordVo
     * @return
     */
    public void saveChatRecord(String userName, ChatRecordVo chatRecordVo) throws Exception {

        User userDB = userService.findByName(userName);

        Optional<Course> optionalCourse = courseService.findById(chatRecordVo.getCourseId());
        if (optionalCourse.isPresent()) {
            Course courseDB = optionalCourse.get();
            ChatRecord chatRecordDB = new ChatRecord(userDB, courseDB, chatRecordVo.getContent());
            chatRecordRepository.save(chatRecordDB);
        } else {
            throw new Exception("聊天记录所在课程不存在");
        }
    }
}
