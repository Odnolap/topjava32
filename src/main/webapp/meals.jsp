<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h3><a href="meal?action=add">Добавить</a></h3>
<table style="border:1px solid;border-collapse:collapse;">
    <tr>
        <th style="border:1px solid black;">Время</th>
        <th style="border:1px solid black;">Наименование</th>
        <th style="border:1px solid black;">Калории</th>
        <th style="border:1px solid black;"></th>
        <th style="border:1px solid black;"></th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <tr style="color:${meal.excess ? 'red': 'green'}">
            <td style="border:1px solid black"><c:out value="${meal.dateTime.toString().replace('T',' ')}"/></td>
            <td style="border:1px solid black;"><c:out value="${meal.description}"/></td>
            <td style="border:1px solid black;"><c:out value="${meal.calories}"/></td>
            <td style="border:1px solid black;"><a href="meal?action=update&mealId=${meal.id}">Изменить</a></td>
            <td style="border:1px solid black;"><a href="meals?action=delete&mealId=${meal.id}">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
