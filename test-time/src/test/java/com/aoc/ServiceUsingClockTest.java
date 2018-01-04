package com.aoc;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class ServiceUsingClockTest {

    private static final long NOW_MILLIS = 1000L;
    private static final Clock NOW_CLOCK = Clock.fixed(Instant.ofEpochMilli(NOW_MILLIS), ZoneOffset.UTC);

    private static final long FUTURE_MILLIS = 1500L;

    @Test
    public void testExpireWithClock() {

        UsersRepository usersRepository = mock(UsersRepository.class);

        User userNoSubscription = getUser(null);
        User userActiveSubscription = getUser(FUTURE_MILLIS);
        User userExpiredSubscriptionPast = getUser(NOW_MILLIS - 1);
        User userExpiredSubscriptionNow = getUser(NOW_MILLIS);

        when(usersRepository.getUsers())
            .thenReturn(asList(userNoSubscription, userActiveSubscription, userExpiredSubscriptionPast,
                               userExpiredSubscriptionNow));
        SubscriptionServiceUsingClock service =
            new SubscriptionServiceUsingClock(usersRepository, NOW_CLOCK);

        service.expireSubscription();

        assertThat(userNoSubscription.getSubscriptionEndTime()).isNull();
        assertThat(userActiveSubscription.getSubscriptionEndTime()).isEqualTo(FUTURE_MILLIS);
        assertThat(userExpiredSubscriptionPast.getSubscriptionEndTime()).isNull();
        assertThat(userExpiredSubscriptionNow.getSubscriptionEndTime()).isNull();
    }

    private static User getUser(Long subscriptionEndTime) {
        User user = new User();
        user.setSubscriptionEndTime(subscriptionEndTime);
        return user;
    }
}
