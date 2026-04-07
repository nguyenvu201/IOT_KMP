# Thêm nút back và mock data cho Temperature Dashboard

> **Feature ID**: `TemperatureDashBack`  
> **Updated**: 2026-04-07 14:10 UTC  
> **Platforms**: All

## Overview

Thêm nút back và mock data cho Temperature Dashboard.

## Domain Models

### `SensorData`

**File**: `shared/src/commonMain/kotlin/caonguyen/vu/shared/models/SensorData.kt`

```kotlin
data class SensorData(
    val deviceId: String,
    val temperature: Double,
    val humidity: Double,
    val timestamp: Long,
)
```

## Repositories

- **`SensorApi`** → ``
  - File: `shared/src/commonMain/kotlin/caonguyen/vu/shared/network/SensorApi.kt`

## IoT Integration

### Devices

| ID Prefix | Type | Sensors | Actuators |
|-----------|------|---------|----------|
| `esp8266_` | Temperature Sensor Node | DHT11, DS18B20 |  |

### MQTT Topics

**Subscribe** (server listens):
- `sensor/temperature/+/data`

**Publish** (server sends):

### Data Flow

`ESP8266 -> MQTT -> TemperatureSubscriber -> SensorTable`

## API Reference

### REST Endpoints

| Method | Path | Auth | Handler |
|--------|------|------|--------|
| `GET` | `/api/sensors/temperature/latest` | none | `SensorRoutes.kt` |

## Testing

### Coverage

| Layer | Coverage |
|-------|----------|
| Shared Domain | 85% |
| Server Routes | 78% |
| Compose Viewmodel | 82% |

### Test Files

- `TemperatureSubscriberTest.kt` — Unit (3 tests)

```bash
./gradlew allTests
```

