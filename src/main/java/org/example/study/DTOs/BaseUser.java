package org.example.study.DTOs;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.example.study.enums.Gender;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUser {

    private Integer age;

    @Column(name = "full_name",length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
