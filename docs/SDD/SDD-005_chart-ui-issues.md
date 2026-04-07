# SDD-005: Software Design — Chart is jittery and missing numeric annotations for readability

| Field | Value |
|-------|-------|
| **Document ID** | SDD-005 |
| **Version** | 1.0.0 |
| **Status** | DRAFT |
| **Feature ID** | `Chart UI Issues` |
| **Created** | 2026-04-07 |
| **Parent SDD** | SDD-001 |
| **Standard** | IEC 62304 §5.4 |

---

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

## 5. SOUP Components Used

- HiveMQ
- Ktor Server
- Exposed

---
*Document: SDD-005 | Standard: IEC 62304 §5.4 | Feature: Chart UI Issues*
