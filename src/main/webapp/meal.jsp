<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <link type="text/css"
          href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
    <title>Add new user</title>
</head>
<body>

<form method="POST" action='meals' name="frmAddMeal">
    <input hidden="hidden" type="text" readonly="readonly" name="id" value="<c:out value="${meal.id}" />" />
    <table style="border:1px solid;border-collapse:collapse;">
        <tr>
            <td style="border:1px solid black">Описание</td>
            <td style="border:1px solid black;">
                <input type="text" name="description" value="<c:out value="${meal.description}" />" />
            </td>
        </tr>
        <tr>
            <td style="border:1px solid black">Калории</td>
            <td style="border:1px solid black;">
                <input type="text" name="calories" value="<c:out value="${meal.calories}" />" />
            </td>
        </tr>
        <tr>
            <td style="border:1px solid black">Дата и время</td>
            <td style="border:1px solid black;">
                <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}" />" />
            </td>
        </tr>
    </table>

    <input type="submit" value="${actionNameDescr}" /><br />
    <a href="meals">Отмена</a>
</form>
</body>
</html>
