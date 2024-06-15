package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.IMealRepository;
import ru.javawebinar.topjava.util.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final Logger log = getLogger(MealServlet.class);
    private final IMealRepository mealRepository = new InMemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            Integer mealId = Integer.valueOf(req.getParameter("mealId"));
            log.info("Deleting meal with id '{}'", mealId);
            mealRepository.deleteMeal(mealId);
            log.debug("Redirecting to meals.jsp");
            resp.sendRedirect("meals");
        } else {
            redirectToMealsJsp(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");
        String dateTime = req.getParameter("dateTime");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        if (id == null || id.isEmpty()) {
            mealRepository.addMeal(parsedDateTime, description, Integer.valueOf(calories));
        } else {
            mealRepository.updateMeal(Integer.valueOf(id), parsedDateTime, description, Integer.valueOf(calories));
        }
        redirectToMealsJsp(req, resp);
    }

    private void redirectToMealsJsp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Forwarding to meals.jsp");
        Collection<Meal> meals = mealRepository.getAllMeals();
        List<MealTo> mealsTo = MealsUtil.getMealListTo(meals);
        req.setAttribute("meals", mealsTo);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
