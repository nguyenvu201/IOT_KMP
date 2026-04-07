# SRS-002: Software Requirements — Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor

| Field | Value |
|-------|-------|
| **Document ID** | SRS-002 |
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
| (see §4 above) | SDD-002_hi-n-th-c-m-bi-n-nhi-t.md | VVR-002_hi-n-th-c-m-bi-n-nhi-t_results.md |

*Full traceability: [TM-001](../TM/TM-001_traceability.md)*

---
*Document: SRS-002 | Standard: IEC 62304 §5.2*
