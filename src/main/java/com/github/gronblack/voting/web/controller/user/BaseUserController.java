package com.github.gronblack.voting.web.controller.user;

import com.github.gronblack.voting.model.User;
import com.github.gronblack.voting.repository.UserRepository;
import com.github.gronblack.voting.util.UserUtil;
import com.github.gronblack.voting.web.UniqueMailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Slf4j
public abstract class BaseUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    protected void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}
