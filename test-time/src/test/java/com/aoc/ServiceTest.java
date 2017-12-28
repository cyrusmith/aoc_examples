package com.aoc;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class ServiceTest {

    private static final long NOW = 1000L;
    private static final long FUTURE = 1500L;

    public void testExpireWithClock() {

        UsersRepository usersRepository = mock(UsersRepository.class);

        User userNoSubscription = getUser(null);
        User userActiveSubscription = getUser(FUTURE);
        User userExpiredSubscriptionPast = getUser(NOW - 1);
        User userExpiredSubscriptionNow = getUser(NOW);

        when(usersRepository.getUsers())
            .thenReturn(asList(userNoSubscription, userActiveSubscription, userExpiredSubscriptionPast,
                               userExpiredSubscriptionNow));
        SubscriptionServiceUsingClock service =
            new SubscriptionServiceUsingClock(usersRepository, Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC));

        service.expireSubscription();
        assertThat(userNoSubscription.getSubscriptionEndTime()).isNull();
        assertThat(userActiveSubscription.getSubscriptionEndTime()).isEqualTo(FUTURE);
        assertThat(userExpiredSubscriptionPast.getSubscriptionEndTime()).isNull();
        assertThat(userExpiredSubscriptionNow.getSubscriptionEndTime()).isNull();
    }

    private static User getUser(Long subscriptionEndTime) {
        User user = new User();
        user.setSubscriptionEndTime(subscriptionEndTime);
        return user;
    }
}
