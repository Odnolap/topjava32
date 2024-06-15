package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements IMealRepository {
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    private int getNewId() {
        return idCounter.getAndAdd(1);
    }

    public InMemoryMealRepository() {
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        addMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    public Collection<Meal> getAllMeals() {
        return mealMap.values();
    }

    @Override
    public Meal addMeal(LocalDateTime dateTime, String description, Integer calories) {
        return mealMap.computeIfAbsent(getNewId(), id -> new Meal(id, dateTime, description, calories));
    }

    @Override
    public Meal getMeal(Integer mealId) {
        return mealMap.get(mealId);
    }

    @Override
    public void updateMeal(Integer mealId, LocalDateTime dateTime, String description, Integer calories) {
        Meal meal = mealMap.get(mealId);
        if (meal != null) {
            meal.setDateTime(dateTime);
            meal.setDescription(description);
            meal.setCalories(calories);
        }
    }

    @Override
    public void deleteMeal(Integer mealId) {
        mealMap.remove(mealId);
    }

}
