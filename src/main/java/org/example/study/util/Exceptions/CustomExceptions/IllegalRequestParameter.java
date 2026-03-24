package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class IllegalRequestParameter extends RuntimeException {

    private final String parameter;

    public IllegalRequestParameter(String parameter) {
        super("The parameter with name -> " + parameter + " is NOT a legal parameter");
        this.parameter = parameter;
    }

}
