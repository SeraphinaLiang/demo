package com.example.strangeclass.dao;

import com.example.strangeclass.entity.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {

}
