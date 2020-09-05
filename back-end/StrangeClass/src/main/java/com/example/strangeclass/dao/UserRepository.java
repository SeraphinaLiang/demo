package com.example.strangeclass.dao;

import com.example.strangeclass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByName(String name);

    User findByNameAndPassword(String name, String password);

//    @Transactional
//    @Modifying
//    @Query(value = "update t_user set username=?2 where id=?1",nativeQuery = true)
//    int modifyById(long id,String name);

}
