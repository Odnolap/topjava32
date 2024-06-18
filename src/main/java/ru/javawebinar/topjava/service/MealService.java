package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(@Autowired MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    // null if meal does not belong to userId
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    // ORDERED dateTime desc
    public Collection<Meal> getAll(int userId) {
        return repository.getFilteredByDates(userId, null, null);
    }

    public List<MealTo> getFilteredMealTos(
        int userId, int caloriesPerDay,
        LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime
    ) {
        Collection<Meal> mealsFilteredByDates = repository.getFilteredByDates(userId, startDate, endDate);
        return (startTime == null || endTime == null)
            ? MealsUtil.getTos(mealsFilteredByDates, caloriesPerDay)
            : MealsUtil.getFilteredTos(mealsFilteredByDates, caloriesPerDay, startTime, endTime);
    }
}
