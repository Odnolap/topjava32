package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        int userId = SecurityUtil.authUserId();
        MealsUtil.meals.forEach(m -> save(m, userId));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }

        // Processing update (meal.id is presented)
        // Use remove to avoid collisions (when 2 requests update the same meal - the second will fail)
        Meal found = repository.remove(meal.getId());
        if (found == null) {
            return null;
        }
        if (found.getUserId() != userId) {
            // Wrong user - return meal back to repository
            repository.put(meal.getId(), found);
            return null;
        }
        repository.put(meal.getId(), meal);
        return meal;

    }

    @Override
    public boolean delete(int id, int userId) {
        Meal found = repository.get(id);
        if (found != null) {
            if (found.getUserId() == userId) {
                repository.remove(id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal found = repository.get(id);
        if (found == null || found.getUserId() != userId) {
            return null;
        } else {
            return found;
        }
    }

    @Override
    public Collection<Meal> getFilteredByDates(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.values().stream()
            .filter(m -> m.getUserId() == userId
                && (startDate == null || endDate == null
                  || isBetweenHalfOpen(
                    m.getDateTime(),
                    LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                    LocalDateTime.of(endDate.plusDays(1), LocalTime.of(0,0))
                  )
                )
            )
            .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
            .collect(Collectors.toList());
    }
}

