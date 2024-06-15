package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.IMealRepository;
import ru.javawebinar.topjava.util.InMemoryMealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class AddMealServlet extends HttpServlet {
    public static final Logger log = getLogger(AddMealServlet.class);
    private final IMealRepository mealRepository = new InMemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            Meal newMeal = new Meal();
            req.setAttribute("meal", newMeal);
            req.setAttribute("actionNameDescr", "Добавить");
        }
        if ("update".equals(action)) {
            Integer mealId = Integer.valueOf(req.getParameter("mealId"));
            Meal meal = mealRepository.getMeal(mealId);
            req.setAttribute("meal", meal);
            req.setAttribute("actionNameDescr", "Обновить");
        }
        log.debug("Forwarding to meal.jsp");
        req.getRequestDispatcher("/meal.jsp").forward(req, resp);
    }
}
