# Goals API Reference

## Authentication

All endpoints require an `x-auth-token` header with a valid token. A `403 Forbidden` is returned if the token is missing, invalid, or does not belong to the resource owner.

---

## Endpoints

### List Goals

`GET /goals/{username}`

Returns all goals for the given user.

**Headers:** `x-auth-token: <token>`

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "user": { ... },
    "weight": 80.0,
    "goalDate": "2026-09-01",
    "createdAt": "2026-05-08T12:00:00.000Z",
    "modifiedAt": "2026-05-08T12:00:00.000Z"
  }
]
```

---

### Get Goal by ID

`GET /goals/{username}/{id}`

**Headers:** `x-auth-token: <token>`

**Response `200 OK`:** Single goal object (same shape as above)

**Errors:**
- `404` — goal not found
- `403` — goal does not belong to the authenticated user

---

### Create Goal

`POST /goals/{username}`

**Headers:** `x-auth-token: <token>`, `Content-Type: application/json`

**Request Body:**
```json
{
  "weight": 80.0,
  "goalDate": "2026-09-01"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `weight` | number | Target weight in **kilograms** |
| `goalDate` | string (ISO date) | Target date (`YYYY-MM-DD`) |

**Response `200 OK`:** The created goal object

---

### Update Goal

`PUT /goals/{username}/{id}`

**Headers:** `x-auth-token: <token>`, `Content-Type: application/json`

**Request Body:** Same shape as Create.

**Response `200 OK`:** The updated goal object

**Errors:**
- `404` — goal not found
- `403` — goal does not belong to the authenticated user

---

### Delete Goal

`DELETE /goals/{username}/{id}`

**Headers:** `x-auth-token: <token>`

**Response `200 OK`**

**Errors:**
- `404` — goal not found
- `403` — goal does not belong to the authenticated user

---

### Calorie Breakdown

`GET /goals/{username}/goal/calorieBreakdown`

Calculates how many calories per day/week the user needs to cut to reach their most recent goal by the goal date. Uses their most recent weight entry as the current weight, and their TDEE for the daily calorie target.

**Headers:** `x-auth-token: <token>`

**Response `200 OK`:**
```json
{
  "weightDiff": 5.0,
  "daysUntilGoal": 116,
  "calorieDeficitPerWeek": 2939.66,
  "calorieDeficitPerDay": 419.95,
  "todayCalorieGoal": 2080.05
}
```

| Field | Type | Description |
|-------|------|-------------|
| `weightDiff` | number | kg difference between current weight and goal weight |
| `daysUntilGoal` | number | Days remaining until the goal date |
| `calorieDeficitPerWeek` | number | Weekly calorie deficit needed |
| `calorieDeficitPerDay` | number | Daily calorie deficit needed |
| `todayCalorieGoal` | number | Recommended daily calorie intake (TDEE − deficit) |

**Returns all-zero object `{}` when:**
- The user has no weight entries recorded
- The user has no goal set
- Current weight is already at or below goal weight
- The goal date has already passed

> The calculation uses 7,700 kcal per kg of body weight.

---

## Data Model

### Goal Object

| Field | Type | Notes |
|-------|------|-------|
| `id` | number | Auto-generated primary key |
| `user` | object | Linked user object |
| `weight` | number | Goal weight in **kg** |
| `goalDate` | string | Target date (`YYYY-MM-DD`) |
| `createdAt` | string (ISO datetime) | Set on creation |
| `modifiedAt` | string (ISO datetime) | Updated on each save |

---

## UI Notes

- **Weight unit:** The API stores and expects weight in **kilograms**. Convert in the UI if displaying in lbs.
- **Most recent goal wins:** The calorie breakdown always uses the most recently created goal. If a user has multiple goals, only the latest one drives the breakdown calculation.
- **No server-side input validation:** The API does not enforce that `weight > 0` or that `goalDate` is in the future on create/update. Validate these in the UI before submitting.
- **Empty calorie breakdown:** When `calorieBreakdown` returns all zeros, it means a prerequisite is missing (no weight logged, no goal set, goal date passed, or already at goal weight). Show a contextual message rather than displaying zeroes.
- **No "active" flag:** There is no status field on a goal. Treat the most recently created goal as the active one when displaying a summary view.
