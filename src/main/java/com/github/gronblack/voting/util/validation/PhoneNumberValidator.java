package com.github.gronblack.voting.util.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern PATTERN = Pattern.compile("^\\+\\d{1,3}-\\d{1,4}-\\d{1,4}(-\\d{1,4})?$");
    private static final int MAX_LENGTH = 18;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value)
                && value.length() <= MAX_LENGTH
                && PATTERN.matcher(value).matches();
    }
}
