package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    MealService service;

    @PostMapping("/meals")
    public String upsertMeal(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
            LocalDateTime.parse(request.getParameter("dateTime")),
            request.getParameter("description"),
            Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = getId(request);
            int userId = SecurityUtil.authUserId();
            assureIdConsistent(meal, id);
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            int userId = SecurityUtil.authUserId();
            checkNew(meal);
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        return "redirect:meals";
    }

    @GetMapping("/deleteMeal")
    public String deleteMeal(HttpServletRequest request) {
        int id = getId(request);
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:meals";
    }

    @GetMapping("/createMeal")
    public String createMealRedirect(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/updateMeal")
    public String updateMealRedirect(HttpServletRequest request, Model model) {
        int id = getId(request);
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        final Meal meal = service.get(id, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals")
    public String listMeals(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> resultMeals = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", resultMeals);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
