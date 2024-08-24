package com.lino.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lino.dscatalog.controller.exceptions.FieldMessage;
import com.lino.dscatalog.dto.UserInsertDTO;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail());

        //Validação
        if (user != null) {

            list.add(new FieldMessage("Email","Este email já existe!"));

        }


        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();//Esta adicionando a lista fieldMessage os erros esta add a classe ValidationError
        }
        return list.isEmpty();
    }
}
