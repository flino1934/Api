package com.lino.dscatalog.services.validation;

import com.lino.dscatalog.controller.exceptions.FieldMessage;
import com.lino.dscatalog.dto.UserUpdateDTO;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request;//Vamos usar para acessar as informações da requisição já que vamos precisar do id que estara na requicisão

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        //Pegando o código
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail());

        //Validação
        if (user != null && userId != user.getId()) {//Vai estar verificando se o email existe em outro usuario se existir vai informar

            list.add(new FieldMessage("Email","Este email já esta cadastrado para outro usuario!"));

        }


        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();//Esta adicionando a lista fieldMessage os erros esta add a classe ValidationError
        }
        return list.isEmpty();
    }
}
