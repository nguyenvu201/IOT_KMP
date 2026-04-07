# CM-001: Change Management Log

| Field | Value |
|-------|-------|
| **Document ID** | CM-001 |
| **Version** | 1.0.0 |
| **Status** | LIVE — Updated per change |
| **Author** | kmp-orchestrator |
| **Created** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §6, FDA 21 CFR 820.30(i), 21 CFR 820.70(b) |

---

## 1. Purpose

Tài liệu này ghi lại tất cả **thay đổi có ý nghĩa** đối với phần mềm, bao gồm features mới, bug fixes, SOUP updates, và configuration changes. Mỗi entry phải có impact assessment.

> **LIVE document** — được update tự động bởi `doc_generator.py --cm` sau mỗi workflow.

---

## 2. Change Entry Format

```
## CM-NNN: [YYYY-MM-DD] <Title>

| Field | Value |
|-------|-------|
| Change ID | CM-NNN |
| Date | YYYY-MM-DD |
| Type | Feature / Bug Fix / SOUP Update / Config / Refactor |
| Author | agent-name |
| REQ impacted | REQ-xxx, REQ-yyy |
| RISK impacted | RISK-xxx |
| SDD ref | SDD-NNN |
| VVR ref | VVR-NNN |

### Description
What was changed and why.

### Impact Assessment
- Modules affected: ...
- Requirements affected: ...
- Risk delta: ... (introduced / resolved / unchanged)
- Tests required: TST-xxx

### Approval
- [ ] Code review passed
- [ ] All tests pass (./gradlew allTests)
- [ ] Documentation updated
- [ ] Security review (if security-impacted)
```

---

## 3. Change Log


### CM-002: [2026-04-07] Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor

| Field | Value |
|-------|-------|
| **Change ID** | CM-002 |
| **Date** | 2026-04-07 |
| **Type** | Feature |
| **Feature ID** | `Hiển thị Cảm biến Nhiệt độ` |
| **Author** | kmp-orchestrator |
| **REQ Impacted** | `REQ-B006`, `REQ-B007`, `REQ-N002`, `REQ-U002` |
| **RISK Impacted** | `RISK-009`, `RISK-010` |
| **SDD Ref** | SDD-002_hiển thị cảm biến nhiệt độ.md |
| **VVR Ref** | VVR-002_hiển thị cảm biến nhiệt độ_results.md |

#### Description

Implemented: **Lấy data MQTT từ esp8266 hiển thị lên app Compose thông qua backend Ktor**.

Domain models added: `SensorData`.

API endpoints: `GET /api/sensors/temperature/latest`.

UI screens: `TemperatureScreen`.

#### Impact Assessment

- **Tests**: ✅ All tests pass
- **Security**: ✅ No critical issues
- **SOUP changes**: None new lib(s)

#### Approval Checklist

- [ ] Code review passed
- [x] All tests pass (`./gradlew allTests`)
- [ ] Documentation updated and committed
- [x] Security review passed

---
### CM-001: [2026-04-07] Initial System Setup

| Field | Value |
|-------|-------|
| **Change ID** | CM-001 |
| **Date** | 2026-04-07 |
| **Type** | Feature |
| **Author** | kmp-orchestrator |
| **REQ Impacted** | SRS-001 (all base requirements) |
| **RISK Impacted** | RISK-001 through RISK-008 |
| **SDD Ref** | SDD-001 |
| **VVR Ref** | Pending |

#### Description
Thiết lập kiến trúc ban đầu của hệ thống KMP IoT:
- Multi-module KMP project: `shared/`, `composeApp/`, `server/`, `iosApp/`
- Kotlin Multiplatform 2.3.0 + Compose Multiplatform 1.10.0
- Ktor 3.3.3 backend với Exposed ORM + PostgreSQL
- HiveMQ MQTT client + Mosquitto broker
- Voyager navigation + Koin DI
- GitHub Actions CI/CD

#### Impact Assessment
- **Modules affected**: All (initial setup)
- **Requirements affected**: All REQs in SRS-001
- **Risk delta**: Introduced RISK-001 through RISK-008 (baseline risks)
- **Tests required**: All test cases in TM-001

#### Approval
- [x] Initial code structure reviewed
- [ ] All tests pass (not yet written)
- [x] Documentation: SDP-001, SRS-001, SDD-001, TM-001, RM-001, SOUP-001 created
- [ ] Security: RISK-004 (anonymous MQTT) not yet resolved

---

## 4. SOUP Update Log

*Tracked separately to quickly identify library-related changes:*

| Date | SOUP ID | Old Version | New Version | Reason | CM Ref |
|------|---------|------------|------------|--------|--------|
| 2026-04-07 | — | — | See SOUP-001 | Initial inventory | CM-001 |

---
*Document: CM-001 | Auto-updated by `doc_generator.py --cm` | Standard: IEC 62304 §6*
