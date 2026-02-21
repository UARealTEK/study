package org.example.study.repository;

import org.example.study.enums.Gender;
import org.example.study.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByAgeAndGender(Integer age, Gender gender);
}
