package org.example.study.util.Filtering;

import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.enums.Gender;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("unused")
public class UserEntitySpecification {

    public static Specification<UserEntity> byAge(Integer age) {
        return (root,criteria,builder) ->
                builder.equal(root.get("age"), age);
    }

    public static Specification<UserEntity> byName(String fullName) {
        return (root,criteria,builder) ->
                builder.equal(root.get("fullName"), fullName);
    }

    public static Specification<UserEntity> byGender(Gender gender) {
        return (root,criteria,builder) ->
                builder.equal(root.get("gender"), gender);
    }

    public static Specification<UserEntity> byAllFields(Integer age, String fullName, Gender gender) {
        Specification<UserEntity> spec = Specification.unrestricted();

        if (age != null) {
            spec = spec.and(byAge(age));
        }

        if (fullName != null) {
            spec = spec.and(byName(fullName));
        }

        if (gender != null) {
            spec = spec.and(byGender(gender));
        }

        return spec;
    }
}
