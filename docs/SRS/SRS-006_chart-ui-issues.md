# SRS-006: Software Requirements — Chart is jittery and missing numeric annotations for readability

| Field | Value |
|-------|-------|
| **Document ID** | SRS-006 |
| **Version** | 1.0.0 |
| **Status** | DRAFT |
| **Author** | kmp-architect |
| **Created** | 2026-04-07 |
| **Feature ID** | `Chart UI Issues` |
| **Parent SRS** | SRS-001 |
| **Standard** | IEC 62304 §5.2 |

---

## 1. Feature Overview

Chart is jittery and missing numeric annotations for readability.

## 2. Parent Requirements Addressed

This feature contributes to the following system-level requirements (SRS-001):

- `REQ-B006`
- `REQ-B007`
- `REQ-N002`
- `REQ-SYS-CHART-01`
- `REQ-SYS-CHART-02`
- `REQ-SYS-CHART-03`
- `REQ-SYS-CHART-04`

## 4. Feature-Specific Requirements

> IDs below are feature-scoped. Format: `REQ-<FEATURE>-<NNN>`

### 4.2 API Requirements

**REQ-CHAR-001** — The server SHALL expose `GET /api/sensors/temperature/latest`.

### 4.3 IoT / MQTT Requirements

**REQ-CHAR-002** — The server SHALL subscribe to MQTT topic `sensor/temperature/+/data`.

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
| (see §4 above) | SDD-006_chart-ui-issues.md | VVR-006_chart-ui-issues_results.md |

*Full traceability: [TM-001](../TM/TM-001_traceability.md)*

---
*Document: SRS-006 | Standard: IEC 62304 §5.2*
