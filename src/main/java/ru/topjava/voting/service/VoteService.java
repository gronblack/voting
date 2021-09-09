package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import ru.topjava.voting.error.IllegalRequestDataException;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.UserRepository;
import ru.topjava.voting.repository.VoteRepository;
import ru.topjava.voting.web.GlobalExceptionHandler;

import java.time.Clock;
import java.time.LocalTime;

@Service
public class VoteService {
    public static final LocalTime VOTE_TIME_BORDER = LocalTime.of(11, 0);
    private static Clock clock;   // https://stackoverflow.com/a/45833128

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        resetClock();
    }

    public Vote save(Vote vote, int userId) {
        vote.setUser(userRepository.getById(userId));
        return voteRepository.save(vote);
    }

    public Vote checkBelong(int id, int userId) {
        return voteRepository.getByIdAndUser(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }

    public static void setClock(Clock clock) {
        VoteService.clock = clock;
    }

    public static Clock getClock() {
        return clock;
    }

    public static void resetClock() {
        setClock(Clock.systemDefaultZone());
    }

    public void checkTime() {
        if (!LocalTime.now(clock).isBefore(VOTE_TIME_BORDER)) {
            throw new IllegalRequestDataException(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING + " before " + VOTE_TIME_BORDER);
        }
    }
}
