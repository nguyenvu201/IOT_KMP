# TM-001: Traceability Matrix

| Field | Value |
|-------|-------|
| **Document ID** | TM-001 |
| **Version** | 1.0.0 |
| **Status** | LIVE — Updated per feature |
| **Author** | kmp-orchestrator |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §5.7, FDA 21 CFR 820.30(e) |

---

## 1. Purpose

Tài liệu này cung cấp **bidirectional traceability** từ User Needs → Requirements → Design → Implementation → Tests → Results.

> [!IMPORTANT]
> Đây là **LIVE document** — được update tự động bởi `doc_generator.py --tm` sau mỗi feature workflow.

---

## 2. Traceability Notation

| Column | Meaning |
|--------|---------|
| `REQ-ID` | Requirement ID từ SRS |
| `Description` | Tóm tắt requirement |
| `SDD Ref` | Section trong SDD document |
| `Implementation` | File(s) implement |
| `TST-ID` | Test case ID |
| `Test Result` | PASS / FAIL / N/A / PENDING |
| `VVR Ref` | V&V Results document |

---

## 3. System Requirements Traceability

### 3.1 System Level (REQ-S)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-S001 | Multi-platform client (Android, Desktop) | SDD-001 §3 | `composeApp/androidMain/`, `composeApp/desktopMain/` | TST-S001 | PENDING | — |
| REQ-S002 | Real-time data ≤ 2s latency | SDD-001 §4.3 | `server/mqtt/RealtimeDeviceHub.kt` | TST-S002 | PENDING | — |
| REQ-S003 | Offline state display | SDD-001 §3.2 | `composeApp/…/ui/` (isOffline state) | TST-S003 | PENDING | — |
| REQ-S004 | JWT auth for write ops | SDD-001 §5.3 | `server/plugins/Authentication.kt` | TST-SC001 | PENDING | — |
| REQ-S005 | Sensor data validation | SDD-001 §4.2 | `server/service/SensorService.kt` | TST-N001 | PENDING | — |

### 3.2 Architecture Level (REQ-A)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-A001 | Business logic in commonMain | SDD-001 §6 | `shared/src/commonMain/` | — | VERIFIED | — |
| REQ-A002 | Repository pattern | SDD-001 §6.2 | `shared/…/repository/` | — | VERIFIED | — |
| REQ-A003 | Koin DI | SDD-001 §2.4 | `shared/…/di/`, `server/di/`, `composeApp/di/` | — | VERIFIED | — |
| REQ-A004 | No GlobalScope | SDD-001 §2 | Code review | — | PENDING | — |

### 3.3 Network / IoT Level (REQ-N)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-N001 | MQTT payload validation | SDD-001 §4.2 | `server/mqtt/MqttMessageHandler.kt` | TST-N001 | PENDING | — |
| REQ-N002 | MQTT topic convention | SDD-001 §4.1 | `server/mqtt/MqttTopicRouter.kt` | — | VERIFIED | — |
| REQ-N003 | MQTT auto-reconnect | SDD-001 §4.2 | `server/mqtt/MqttService.kt` | TST-N002 | PENDING | — |
| REQ-N004 | WebSocket bridge ≤ 1s | SDD-001 §4.3 | `server/mqtt/RealtimeDeviceHub.kt` | TST-N003 | PENDING | — |

### 3.4 Backend Level (REQ-B)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-B001 | REST API prefix `/api/` | SDD-001 §5 | `server/routes/` | — | VERIFIED | — |
| REQ-B002 | JWT secret from env var | SDD-001 §5.3 | `server/plugins/Authentication.kt` | TST-SC002 | PENDING | — |
| REQ-B003 | Health endpoint `/health` | SDD-001 §5.1 | `server/routes/HealthRoutes.kt` | TST-B001 | PENDING | — |
| REQ-B004 | HikariCP connection pool | SDD-001 §5.2 | `server/database/DatabaseFactory.kt` | — | VERIFIED | — |
| REQ-B005 | Input validation 400 | SDD-001 §5.4 | `server/model/request/` (validate()) | TST-B002 | PENDING | — |
| REQ-B006 | Standard error format | SDD-001 §5.4 | `server/model/response/ErrorResponse.kt` | — | PENDING | — |

### 3.5 UI Level (REQ-U)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-U001 | Voyager navigation | SDD-001 §3.1 | `composeApp/…/App.kt` | — | VERIFIED | — |
| REQ-U002 | Loading + error state | SDD-001 §3.2 | All `…State.kt` files | — | VERIFIED | — |
| REQ-U003 | Offline state display | SDD-001 §3.2 | All screens | TST-U001 | PENDING | — |
| REQ-U004 | Unidirectional data flow | SDD-001 §3.2 | All `…ViewModel.kt` | — | VERIFIED | — |

### 3.6 Security Level (REQ-SC)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-SC001 | JWT expiry 1 hour | SDD-001 §5.3 | `server/service/AuthService.kt` | TST-SC001 | PENDING | — |
| REQ-SC002 | No secret hardcoding | SDD-001 §5.3 | All auth/config files | TST-SC002 | PENDING | — |
| REQ-SC003 | No anonymous MQTT | SDD-001 §7 | `mosquitto.conf` | — | PENDING | — |
| REQ-SC004 | HTTPS production | SDD-001 §7 | `Dockerfile`, `docker-compose.yml` | — | PENDING | — |

### 3.7 Quality Level (REQ-Q)

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|---------|
| REQ-Q001 | ≥ 80% test coverage shared/ | — | `shared/src/commonTest/` | TST-Q001 | PENDING | — |
| REQ-Q002 | CI gate on main | SDP-001 §9 | `.github/workflows/ci.yml` | — | PENDING | — |
| REQ-Q003 | No circular deps | — | `module_analyzer.py` | TST-Q002 | PENDING | — |

---

## 4. Test Case Definitions

| TST-ID | REQ-ID | Description | Method | Pass Criteria |
|--------|--------|-------------|--------|--------------|
| TST-S001 | REQ-S001 | Run app on Android API 24 and Desktop | Manual | App launches, no crashes |
| TST-S002 | REQ-S002 | Measure MQTT → UI latency | Automated + timer | ≤ 2000ms |
| TST-S003 | REQ-S003 | Kill server, observe UI | Manual | Shows "Disconnected" within 5s |
| TST-N001 | REQ-N001, REQ-S005 | Publish out-of-range sensor data | Unit test | Server rejects, data NOT saved |
| TST-N002 | REQ-N003 | MQTT service restart | Integration | Auto-reconnects within 60s |
| TST-N003 | REQ-N004 | Measure MQTT → WebSocket latency | Integration | ≤ 1000ms |
| TST-B001 | REQ-B003 | GET /health when all services OK | Automated | 200 OK with `"status":"ok"` |
| TST-B002 | REQ-B005 | POST with invalid body | Automated | 400 Bad Request |
| TST-U001 | REQ-U003 | Disconnect from server | Manual | isOffline indicator visible |
| TST-SC001 | REQ-SC001, REQ-S004 | Request with expired JWT | Automated | 401 Unauthorized |
| TST-SC002 | REQ-SC002, REQ-B002 | Start server without JWT_SECRET | Automated | Server fails to start (error) |
| TST-Q001 | REQ-Q001 | Run coverage report | `./gradlew :shared:jvmTest` | ≥ 80% line coverage |
| TST-Q002 | REQ-Q003 | Run module analyzer | `module_analyzer.py` | "No circular dependencies" |

---

## 5. Feature Addendum Traceability

*New features add rows here and create `SRS-NNN_<feature>.md`:*

| Feature | SRS | SDD | VVR | Status |
|---------|-----|-----|-----|--------|
| System Base | SRS-001 | SDD-001 | — | In Progress |

---

## 6. Risk-to-Requirement Cross Reference

| RISK-ID | Description | Mitigated By REQ | Status |
|---------|-------------|-----------------|--------|
| RISK-001 | MQTT connection loss → uncontrolled devices | REQ-N003 (reconnect) | Open |
| RISK-002 | Invalid sensor data corrupts analytics | REQ-N001, REQ-S005 | Open |
| RISK-003 | JWT secret leaked → unauthorized control | REQ-SC002 (env var) | Open |
| RISK-004 | MQTT anonymous access → device hijack | REQ-SC003 | Open |

*See `docs/RM/RM-001_risk-analysis.md` for full risk register.*

---
*Document: TM-001 | Auto-updated by `doc_generator.py --tm` | Standard: IEC 62304 §5.7*


### Hiển thị Cảm biến Nhiệt độ — Added 2026-04-07

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|--------|
| `REQ-N002` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `TST-N002` | PASS | VVR-003 |
| `REQ-B006` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `TST-B006` | PASS | VVR-003 |
| `REQ-B007` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |
| `REQ-U002` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |
| `REQ-U002` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |
| `REQ-B007` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |
| `REQ-N002` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `TST-N002` | PASS | VVR-003 |
| `REQ-B006` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `TST-B006` | PASS | VVR-003 |
| `REQ-B007` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |
| `REQ-U002` | | (feature: Hiển thị Cảm biến Nhiệt độ) | | `—` | PENDING | VVR-003 |


### Temperature Sensor Dashboard — Added 2026-04-07

| REQ-ID | Description | SDD Ref | Implementation | TST-ID | Result | VVR Ref |
|--------|-------------|---------|----------------|--------|--------|--------|
| `REQ-N002` | | (feature: Temperature Sensor Dashboard) | | `TST-N002` | PASS | VVR-002 |
| `REQ-B006` | | (feature: Temperature Sensor Dashboard) | | `TST-B006` | PASS | VVR-002 |
| `REQ-B007` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
| `REQ-U002` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
| `REQ-U002` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
| `REQ-B007` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
| `REQ-N002` | | (feature: Temperature Sensor Dashboard) | | `TST-N002` | PASS | VVR-002 |
| `REQ-B006` | | (feature: Temperature Sensor Dashboard) | | `TST-B006` | PASS | VVR-002 |
| `REQ-B007` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
| `REQ-U002` | | (feature: Temperature Sensor Dashboard) | | `—` | PENDING | VVR-002 |
