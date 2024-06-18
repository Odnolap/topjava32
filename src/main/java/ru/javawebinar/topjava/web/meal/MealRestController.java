package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(@Autowired MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create meal {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public Meal update(Meal meal, int id) {
        log.info("update meal {} with id {}", meal, id);
        assureIdConsistent(meal, id);
        int userId = authUserId();
        meal.setUserId(userId);
        return service.update(meal, userId);
    }

    public void delete(int id) {
        log.info("delete meal with id {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        log.info("getting meal with id {}", id);
        return service.get(id, authUserId());
    }

    public Collection<Meal> getAll() {
        log.info("getting all meals for the user");
        return service.getAll(authUserId());
    }

    public List<MealTo> getAllMealTos() {
        log.info("getting tos for the user without filters");
        return getFilteredMealTos(null, null, null, null);
    }

    public List<MealTo> getFilteredMealTos(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getting filtered tos for the user");
        return service.getFilteredMealTos(authUserId(), authUserCaloriesPerDay(),
            startDate, endDate, startTime, endTime);
    }

}
