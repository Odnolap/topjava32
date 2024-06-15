package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    private static final int maxCaloriesPerDay = 2000;

    public static void main(String[] args) {
        IMealRepository mealRepository = new InMemoryMealRepository();
        List<MealTo> mealsTo = getFilteredMealListTo(mealRepository.getAllMeals(), 7, 0, 12, 0);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> getMealListTo(Collection<Meal> meals) {
        return filteredByStreams(meals, null, null, maxCaloriesPerDay);
    }
    public static List<MealTo> getFilteredMealListTo(
        Collection<Meal> meals,
        int hoursFrom,
        int minutesFrom,
        int hoursTo,
        int minutesTo
    ) {
        return filteredByStreams(meals, LocalTime.of(hoursFrom, minutesFrom), LocalTime.of(hoursTo, minutesTo), maxCaloriesPerDay);
    }

    private static List<MealTo> filteredByStreams(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
            .collect(
                Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
            );

        return meals.stream()
            .filter(meal -> startTime == null || endTime == null
                || TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
            .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
            .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
