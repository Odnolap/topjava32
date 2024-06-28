package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int FIRST_USER_BREAKFAST_ID = START_SEQ + 3;
    public static final int FIRST_USER_LUNCH_ID = START_SEQ + 4;
    public static final int FIRST_USER_DINNER_ID = START_SEQ + 5;
    public static final int MIDNIGHT_MEAL_ID = START_SEQ + 6;
    public static final int SECOND_USER_BREAKFAST_ID = START_SEQ + 7;
    public static final int SECOND_USER_LUNCH_ID = START_SEQ + 8;
    public static final int SECOND_USER_DINNER_ID = START_SEQ + 9;


    public static final Meal firstUserBreakfast = new Meal(FIRST_USER_BREAKFAST_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal firstUserLunch = new Meal(FIRST_USER_LUNCH_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal firstUserDinner = new Meal(FIRST_USER_DINNER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal midnightDinner = new Meal(MIDNIGHT_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal secondUserBreakfast = new Meal(SECOND_USER_BREAKFAST_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal secondUserLunch = new Meal(SECOND_USER_LUNCH_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal secondUserDinner = new Meal(SECOND_USER_DINNER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.FEBRUARY, 1, 11, 0), "New", 450);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(firstUserBreakfast);
        updated.setDateTime(LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0));
        updated.setDescription("Updated description");
        updated.setCalories(333);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
