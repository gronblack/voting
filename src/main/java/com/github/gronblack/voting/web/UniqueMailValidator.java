package com.github.gronblack.voting.web;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import com.github.gronblack.voting.model.HasIdAndEmail;
import com.github.gronblack.voting.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {
    private final UserRepository repository;
    private final HttpServletRequest request;

    public UniqueMailValidator(UserRepository repository, @Nullable HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            repository.getByEmail(user.getEmail().toLowerCase())
                    .ifPresent(dbUser -> {
                        Assert.notNull(request, "HttpServletRequest missed");
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbUser.id();

                            // it is ok, if update ourself
                            if (user.getId() != null && dbId == user.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.authId() && requestURI.contains("/account"))) return;
                        }
                        errors.rejectValue("email", "", GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
