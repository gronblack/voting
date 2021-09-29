package com.github.gronblack.voting.util.validation;

import com.github.gronblack.voting.error.IllegalRequestDataException;
import com.github.gronblack.voting.error.NotFoundException;
import com.github.gronblack.voting.model.HasId;
import com.github.gronblack.voting.util.DateTimeUtil;
import com.github.gronblack.voting.web.GlobalExceptionHandler;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class ValidationUtil {
    public static final LocalTime VOTE_TIME_BORDER = LocalTime.of(11, 0);

    public static void checkNew(HasId bean) {
        if (bean == null) {
            throw new IllegalRequestDataException("checkNew: bean must not be null");
        }
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public static void checkTime() {
        if (!LocalTime.now(DateTimeUtil.getClock()).isBefore(VOTE_TIME_BORDER)) {
            throw new IllegalRequestDataException(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING + " before " + VOTE_TIME_BORDER);
        }
    }
}