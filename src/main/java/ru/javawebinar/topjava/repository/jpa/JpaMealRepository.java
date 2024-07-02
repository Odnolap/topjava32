package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.getUser() == null) {
            User ref = em.getReference(User.class, userId);
            ref.setId(userId);
            meal.setUser(ref);
        }
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            int numAffected = em.createNamedQuery(Meal.UPDATE)
                .setParameter("id", meal.getId())
                .setParameter("userId", userId)
                .setParameter("dateTime", meal.getDateTime())
                .setParameter("description", meal.getDescription())
                .setParameter("calories", meal.getCalories())
                .executeUpdate();
            if (numAffected > 0) {
                return meal;
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
            .setParameter("id", id)
            .setParameter("userId", userId)
            .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.GET_ONE_BY_ID_AND_USER, Meal.class)
            .setParameter("id", id)
            .setParameter("userId", userId)
            .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALF_OPEN_SORTED, Meal.class)
            .setParameter("userId", userId)
            .setParameter("startDateTime", startDateTime)
            .setParameter("endDateTime", endDateTime)
            .getResultList();
    }
}
