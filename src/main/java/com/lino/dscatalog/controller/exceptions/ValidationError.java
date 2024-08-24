package com.lino.dscatalog.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{

    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){//Vai adicionar os erros trazendo como argumento fieldname e message
        errors.add(new FieldMessage(fieldName,message));
    }
}
