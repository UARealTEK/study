package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class InvalidConstraintConfigurationException extends RuntimeException {

    String constraintName;
    public InvalidConstraintConfigurationException(String constraintName) {
        super("The following constraint is invalid " + constraintName);
        this.constraintName = constraintName;
    }

    public InvalidConstraintConfigurationException() {
        super("Invalid constraint provided");
    }

}
