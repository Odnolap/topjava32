package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEAL_TO_MATCHER.contentJson(
                MealsUtil.getTos(List.of(meal7, meal6, meal5, meal4, meal3, meal2, meal1), MealsUtil.DEFAULT_CALORIES_PER_DAY)
            ));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(newMeal)))
            .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updated)))
            .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getBetweenRest() throws Exception {
        String dateFrom = "2020-01-31";
        String dateTo = "2020-01-31";
        String timeFrom = "08:00:00";
        String timeTo = "20:00:00";
        String queryParameters = String.format("startDate=%s&endDate=%s&startTime=%s&endTime=%s",
            dateFrom, dateTo, timeFrom, timeTo);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?" + queryParameters))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEAL_TO_MATCHER.contentJson(
                MealsUtil.getFilteredTos(
                    List.of(meal7, meal6, meal5, meal4),
                    MealsUtil.DEFAULT_CALORIES_PER_DAY,
                    LocalTime.parse(timeFrom),
                    LocalTime.parse(timeTo)
                    )
            ));

    }

    @Test
    void getBetweenRestWithEmptyParameters() throws Exception {
        String dateFrom = "2020-01-31";
        String timeTo = "20:00:00";
        String queryParameters = String.format("startDate=%s&endDate=&startTime=&endTime=%s",
            dateFrom, timeTo);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?" + queryParameters))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MEAL_TO_MATCHER.contentJson(
                MealsUtil.getFilteredTos(
                    List.of(meal7, meal6, meal5, meal4),
                    MealsUtil.DEFAULT_CALORIES_PER_DAY,
                    null,
                    LocalTime.parse(timeTo)
                    )
            ));

    }
}