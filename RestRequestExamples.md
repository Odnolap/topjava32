# REST API Request examples

## 1. Meals API

### 1.1 Filter meals by start date and end time
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=&startTime=&endTime=15%3A34'
```

### 1.2 Get all meals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'
```

### 1.3 Get 1 meal by id
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100009'
```

### 1.4 Create a meal
```shell
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
    "dateTime": "2020-01-31T21:00:00",
    "description": "Ужин 2",
    "calories": 520
}'
```

### 1.5 Update a meal by id
```shell
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100012' \
--header 'Content-Type: application/json' \
--data '{
    "id": 100012,
    "dateTime": "2020-01-31T21:00:00",
    "description": "Ужин 2 обновлённый",
    "calories": 530
}'
```

### 1.6 Delete a meal by id
```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100012'
```
