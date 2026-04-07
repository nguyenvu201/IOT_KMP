# SRS-005: Software Requirements — Màn hình giám sát biểu đồ đo nhiệt độ, độ ẩm, lưu lượng nước, nồng độ pH

| Field | Value |
|-------|-------|
| **Document ID** | SRS-005 |
| **Version** | 1.0.0 |
| **Status** | DRAFT |
| **Author** | kmp-architect |
| **Created** | 2026-04-07 |
| **Feature ID** | `MonitoringCharts` |
| **Parent SRS** | SRS-001 |
| **Standard** | IEC 62304 §5.2 |

---

## 1. Feature Overview

Màn hình giám sát biểu đồ đo nhiệt độ, độ ẩm, lưu lượng nước, nồng độ pH.

## 2. Parent Requirements Addressed

This feature contributes to the following system-level requirements (SRS-001):

- `REQ-B006`
- `REQ-B007`
- `REQ-N002`
- `REQ-U002`
- `REQ-UI-NAV-001`
- `REQ-UI-TEMP-002`

## 4. Feature-Specific Requirements

> IDs below are feature-scoped. Format: `REQ-<FEATURE>-<NNN>`

### 4.1 Domain Model Requirements

**REQ-MONI-001** — The system SHALL provide a `SensorData` domain model with fields: `deviceId: String`, `temperature: Double`, `humidity: Double`, `timestamp: Long`.

### 4.2 API Requirements

**REQ-MONI-002** — The server SHALL expose `GET /api/sensors/temperature/latest`.

### 4.3 IoT / MQTT Requirements

**REQ-MONI-003** — The server SHALL subscribe to MQTT topic `sensor/temperature/+/data`.

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
| (see §4 above) | SDD-005_monitoringcharts.md | VVR-005_monitoringcharts_results.md |

*Full traceability: [TM-001](../TM/TM-001_traceability.md)*

---
*Document: SRS-005 | Standard: IEC 62304 §5.2*
