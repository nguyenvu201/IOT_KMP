# Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor

> **Feature ID**: `Hiển thị Cảm biến Nhiệt độ`  
> **Updated**: 2026-04-07 13:48 UTC  
> **Platforms**: All

## Overview

Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor.

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

## UI Screens

### `TemperatureScreen`

**File**: `composeApp/src/commonMain/kotlin/caonguyen/vu/ui/dashboard/TemperatureScreen.kt`  
**ViewModel**: `TemperatureViewModel`  

**State fields**:
- `currentTemperature`
- `isLoading`
- `error`

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

