# SRS-003: Software Requirements — Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor

| Field | Value |
|-------|-------|
| **Document ID** | SRS-003 |
| **Version** | 1.0.0 |
| **Status** | DRAFT |
| **Author** | kmp-architect |
| **Created** | 2026-04-07 |
| **Feature ID** | `Hiển thị Cảm biến Nhiệt độ` |
| **Parent SRS** | SRS-001 |
| **Standard** | IEC 62304 §5.2 |

---

## 1. Feature Overview

Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor.

## 2. Parent Requirements Addressed

This feature contributes to the following system-level requirements (SRS-001):

- `REQ-B006`
- `REQ-B007`
- `REQ-N002`
- `REQ-U002`

## 4. Feature-Specific Requirements

> IDs below are feature-scoped. Format: `REQ-<FEATURE>-<NNN>`

### 4.1 Domain Model Requirements

**REQ-HINT-001** — The system SHALL provide a `SensorData` domain model with fields: `deviceId: String`, `temperature: Double`, `humidity: Double`, `timestamp: Long`.

### 4.2 API Requirements

**REQ-HINT-002** — The server SHALL expose `GET /api/sensors/temperature/latest`.

### 4.3 IoT / MQTT Requirements

**REQ-HINT-003** — The server SHALL subscribe to MQTT topic `sensor/temperature/+/data`.

### 4.4 UI Requirements

**REQ-HINT-004** — The client SHALL provide a `TemperatureScreen` screen displaying: `currentTemperature`, `isLoading`, `error`.

## 5. Non-Functional Requirements

| Requirement | Inherited From |
|------------|---------------|
| Performance: UI updates ≤ 2s latency | REQ-S002 |
| Security: JWT auth for write ops | REQ-S004 |
| Reliability: No GlobalScope coroutines | REQ-A004 |
| Testability: ≥ 80% coverage on domain | REQ-Q001 |

## 6. Traceability

| Requirement | SDD Ref | TST Ref |
|------------|---------|--------|
| (see §4 above) | SDD-003_hi-n-th-c-m-bi-n-nhi-t.md | VVR-003_hi-n-th-c-m-bi-n-nhi-t_results.md |

*Full traceability: [TM-001](../TM/TM-001_traceability.md)*

---
*Document: SRS-003 | Standard: IEC 62304 §5.2*
