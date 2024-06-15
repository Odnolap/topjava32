package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;

public interface IMealRepository {
    Collection<Meal> getAllMeals();
    Meal addMeal(LocalDateTime dateTime, String description, Integer calories);
    Meal getMeal(Integer mealId);
    void updateMeal(Integer mealId, LocalDateTime dateTime, String description, Integer calories);
    void deleteMeal(Integer mealId);
}
