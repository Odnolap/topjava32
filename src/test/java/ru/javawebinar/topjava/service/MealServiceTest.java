package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.CommonTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
    "classpath:spring/spring-app.xml",
    "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(FIRST_USER_BREAKFAST_ID, USER_ID);
        assertMatch(meal, firstUserBreakfast);
    }

    @Test
    public void getWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> service.get(FIRST_USER_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(FIRST_USER_BREAKFAST_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(FIRST_USER_BREAKFAST_ID, USER_ID));
    }

    @Test
    public void deleteWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_USER_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(
            LocalDate.of(2020, Month.JANUARY, 30),
            LocalDate.of(2020, Month.JANUARY, 30),
            USER_ID);
        assertMatch(actual, firstUserDinner, firstUserLunch, firstUserBreakfast);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, secondUserDinner, secondUserLunch, secondUserBreakfast, midnightDinner,
            firstUserDinner, firstUserLunch, firstUserBreakfast);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateWrongUserId() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        updated.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal expected = getNew();
        expected.setId(newId);
        assertMatch(created, expected);
        assertMatch(service.get(newId, USER_ID), expected);
    }

    @Test
    public void createWithTheSameDateTime() {
        service.create(getNew(), USER_ID);
        Meal anotherMeal = getNew();
        anotherMeal.setCalories(444);
        anotherMeal.setDescription("Another description");
        assertThrows(DataAccessException.class, () -> service.create(anotherMeal, USER_ID));
    }

    @Test
    public void createWithNonExistentUserId() {
        assertThrows(DataAccessException.class, () -> service.create(getNew(), NOT_FOUND));
    }
}
