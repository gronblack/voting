package ru.topjava.voting.util.validation;

import ru.topjava.voting.error.IllegalRequestDataException;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.HasId;
import ru.topjava.voting.util.DateTimeUtil;
import ru.topjava.voting.web.GlobalExceptionHandler;

import java.time.LocalTime;

public class ValidationUtil {
    public static final LocalTime VOTE_TIME_BORDER = LocalTime.of(11, 0);

    public static void checkNew(HasId bean) {
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