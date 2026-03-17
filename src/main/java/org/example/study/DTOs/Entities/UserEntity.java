package org.example.study.DTOs.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.study.DTOs.BaseUser;
import org.example.study.enums.Gender;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity extends BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "userEntity")
    private List<BorrowRecordEntity> borrowRecords;

    public UserEntity(Long id, Integer age, String fullName, Gender gender) {
        this.id = id;
        setAge(age);
        setFullName(fullName);
        setGender(gender);
    }
}
