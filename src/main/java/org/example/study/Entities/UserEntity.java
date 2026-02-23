package org.example.study.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.study.enums.Gender;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    @Column(name = "full_name",length = 100)
    private String fullName;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
