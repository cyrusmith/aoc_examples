package com.aoc;

import java.time.Clock;
import java.util.List;

public class SubscriptionServiceUsingClock {

    private final UsersRepository usersRepository;
    private final Clock clock;

    public SubscriptionServiceUsingClock(UsersRepository usersRepository, Clock clock) {
        this.usersRepository = usersRepository;
        this.clock = clock;
    }

    public void expireSubscription() {
        List<User> users = usersRepository.getUsers();
        long now = clock.millis();
        for (User user : users) {
            if (user.getSubscriptionEndTime() != null && user.getSubscriptionEndTime() <= now) {
                user.setSubscriptionEndTime(null);
            }
        }
    }
}
