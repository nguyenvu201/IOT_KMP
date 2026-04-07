# SDD-001: Software Design — Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor

| Field | Value |
|-------|-------|
| **Document ID** | SDD-001 |
| **Version** | 1.0.0 |
| **Status** | DRAFT |
| **Feature ID** | `Temperature Sensor Dashboard` |
| **Created** | 2026-04-07 |
| **Parent SDD** | SDD-001 |
| **Standard** | IEC 62304 §5.4 |

---

## 1. Shared Module Design

### 1.1 Domain Models

**`SensorData`** — `shared/src/commonMain/kotlin/caonguyen/vu/shared/models/SensorData.kt`

```kotlin
data class SensorData(
    val deviceId: String,
    val temperature: Double,
    val humidity: Double,
    val timestamp: Long,
)
```

### 1.2 Repository Interfaces

| Interface | Fake Impl | File |
|-----------|----------|------|
| `SensorApi` | `` | `shared/src/commonMain/kotlin/caonguyen/vu/shared/network/SensorApi.kt` |

### 1.3 SOUP Dependencies

- kotlinx.serialization

## 2. IoT / MQTT Design

### 2.1 Devices

| ID Prefix | Type | Sensors | Actuators |
|-----------|------|---------|-----------|
| `esp8266_` | Temperature Sensor Node | DHT11, DS18B20 |  |

### 2.2 MQTT Topics

**Subscribe**: `sensor/temperature/+/data`

**Publish**: 

### 2.3 Data Flow

`ESP8266 -> MQTT -> TemperatureSubscriber -> SensorTable`

## 3. Backend Design (Ktor)

### 3.1 REST Routes

| Method | Path | Handler | Auth |
|--------|------|---------|------|
| `GET` | `/api/sensors/temperature/latest` | `SensorRoutes.kt` | none |

### 3.3 Database Tables

- `SensorTable`

## 4. UI Design (Compose)

### 4.1 Screens

**`TemperatureScreen`** (Voyager Screen)

- File: `composeApp/src/commonMain/kotlin/caonguyen/vu/ui/dashboard/TemperatureScreen.kt`
- ViewModel: `TemperatureViewModel`
- State: `currentTemperature`, `isLoading`, `error`

## 5. SOUP Components Used

- kotlinx.serialization
- HiveMQ
- Ktor Server
- Exposed

---
*Document: SDD-001 | Standard: IEC 62304 §5.4 | Feature: Temperature Sensor Dashboard*
