package com.aoc;

import java.util.List;

public class SubscriptionService {

    private final UsersRepository usersRepository;

    public SubscriptionService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void expireSubscription() {
        List<User> users = usersRepository.getUsers();
        long now = System.currentTimeMillis();
        for (User user : users) {
            if (user.getSubscriptionEndTime() != null && user.getSubscriptionEndTime() <= now) {
                user.setSubscriptionEndTime(null);
            }
        }
    }
}
