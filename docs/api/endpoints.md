# API Reference

> **Updated**: 2026-04-07 14:10 UTC

**Base URL**: `http://localhost:8085`  
**Health**: `GET /health`

## Authentication

JWT Bearer token required for protected endpoints.

```http
Authorization: Bearer <token>
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{"email": "admin@iot.local", "password": "..."}
```

## REST Endpoints

### `/sensors`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/api/sensors/temperature/latest` | Public | |

## MQTT Topics (HiveMQ)

**Broker**: `tcp://localhost:1883`

### Subscribe (server → listens)

- `sensor/temperature/+/data`

### Publish (server → device)


## Error Responses

```json
{"error": "message", "code": "ERROR_CODE"}
```

| HTTP Code | Meaning |
|-----------|--------|
| 200 | OK |
| 201 | Created |
| 400 | Bad request (validation failed) |
| 401 | Unauthorized (missing/invalid JWT) |
| 404 | Not found |
| 500 | Internal server error |
