# SRS-001: System Requirements Specification

| Field | Value |
|-------|-------|
| **Document ID** | SRS-001 |
| **Version** | 1.0.0 |
| **Status** | APPROVED |
| **Author** | kmp-architect |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §5.2, FDA 21 CFR 820.30(c) |

---

## 1. Purpose & Scope

Tài liệu này là **master requirements specification** cho toàn bộ hệ thống KMP IoT. Mỗi feature mới sẽ tạo thêm `SRS-NNN_<feature>.md` như một addendum.

**System**: KMP IoT Platform — giám sát và điều khiển thiết bị IoT qua MQTT, WebSocket và REST API.

---

## 2. System-Level Requirements

### REQ-S001 — Multi-platform Client
> Hệ thống PHẢI cung cấp client chạy trên Android (API 24+) và Desktop (JVM).

**Rationale**: Operators cần monitor từ nhiều devices.  
**Priority**: Must Have  
**Verification**: TST-S001

### REQ-S002 — Real-time Data Display
> Hệ thống PHẢI hiển thị sensor data với độ trễ ≤ 2 giây kể từ khi thiết bị publish MQTT.

**Rationale**: Monitoring an toàn yêu cầu near-real-time.  
**Priority**: Must Have  
**Verification**: TST-S002

### REQ-S003 — Offline Resilience
> Khi mất kết nối server, client PHẢI hiển thị trạng thái "Disconnected" rõ ràng và không crash.

**Priority**: Must Have  
**Verification**: TST-S003

### REQ-S004 — Authentication
> Tất cả write operations (create/update/delete device, send command) PHẢI yêu cầu JWT authentication.

**Priority**: Must Have  
**Verification**: TST-SC001

### REQ-S005 — Data Validation
> Tất cả sensor data PHẢI được validate range trước khi lưu vào database.

**Rationale**: Prevent corrupt data from faulty sensors.  
**Priority**: Must Have  
**Verification**: TST-N001

---

## 3. Architecture Requirements

### REQ-A001 — KMP-First Architecture
> Business logic PHẢI được implement trong `shared/commonMain` để tái sử dụng đa platform.

**Priority**: Must Have

### REQ-A002 — Repository Pattern
> Data access PHẢI thông qua Repository interface. Không được gọi trực tiếp network/database từ ViewModel.

**Priority**: Must Have

### REQ-A003 — Dependency Injection
> Tất cả dependencies PHẢI được inject qua Koin, không hardcode.

**Priority**: Must Have

### REQ-A004 — No GlobalScope
> Code sản xuất PHẢI KHÔNG sử dụng `GlobalScope`. Tất cả coroutines phải dùng structured concurrency.

**Priority**: Must Have

---

## 4. Network / IoT Requirements

### REQ-N001 — MQTT Device Data Validation
> Server PHẢI reject và log sensor payload ngoài valid range (temperature: -40..125°C, humidity: 0..100%).

**Priority**: Must Have  
**Verification**: TST-N001

### REQ-N002 — MQTT Topic Convention
> Tất cả MQTT topics PHẢI theo pattern: `devices/{deviceId}/{type}` với type ∈ {data, status, command, response}.

**Priority**: Must Have

### REQ-N003 — MQTT Reconnect
> MQTT client PHẢI tự động reconnect khi mất kết nối broker với exponential backoff (1s, 2s, 4s, max 60s).

**Priority**: Must Have  
**Verification**: TST-N002

### REQ-N004 — WebSocket Real-time Bridge
> Server PHẢI forward MQTT device data đến WebSocket clients trong vòng 1 giây.

**Priority**: Should Have  
**Verification**: TST-N003

---

## 5. Backend Requirements (Ktor)

### REQ-B001 — REST API Versioning
> Tất cả API endpoints PHẢI có prefix `/api/` và theo RESTful conventions.

**Priority**: Must Have

### REQ-B002 — JWT Authentication
> JWT secret PHẢI được load từ environment variable `JWT_SECRET`. Không được hardcode.

**Priority**: Must Have  
**Verification**: TST-SC002

### REQ-B003 — Health Endpoint
> Server PHẢI expose `GET /health` trả về `200 OK` khi tất cả dependencies (DB, MQTT) healthy.

**Priority**: Must Have  
**Verification**: TST-B001

### REQ-B004 — Database Connection Pooling
> Server PHẢI dùng HikariCP với pool size phù hợp. Không tạo connection mới cho mỗi request.

**Priority**: Must Have

### REQ-B005 — Input Validation
> Tất cả API request bodies PHẢI được validate trước khi xử lý. Return 400 Bad Request nếu invalid.

**Priority**: Must Have  
**Verification**: TST-B002

### REQ-B006 — Error Response Format
> Tất cả error responses PHẢI theo format: `{"error": "message", "code": "ERROR_CODE"}`.

**Priority**: Should Have

---

## 6. UI Requirements (Compose)

### REQ-U001 — Voyager Navigation
> Navigation PHẢI dùng Voyager library. Không dùng Navigation Compose.

**Priority**: Must Have

### REQ-U002 — Loading State
> Tất cả async operations PHẢI hiển thị loading indicator và handle error state.

**Priority**: Must Have

### REQ-U003 — Offline State
> UI PHẢI hiển thị rõ ràng khi không có kết nối server.

**Priority**: Must Have  
**Verification**: TST-U001

### REQ-U004 — Unidirectional Data Flow
> ViewModels PHẢI dùng `MutableStateFlow` + `update{}`. UI PHẢI chỉ emit events, không modify state trực tiếp.

**Priority**: Must Have

---

## 7. Security Requirements

### REQ-SC001 — JWT Expiry
> JWT access tokens PHẢI expire trong vòng 1 giờ.

**Priority**: Must Have  
**Verification**: TST-SC001

### REQ-SC002 — No Secret Hardcoding
> Tất cả secrets (JWT_SECRET, DB_PASSWORD, MQTT_PASSWORD) PHẢI được load từ environment variables.

**Priority**: Must Have  
**Verification**: TST-SC002

### REQ-SC003 — MQTT Authentication
> MQTT broker PHẢI KHÔNG cho phép anonymous connections trong production.

**Priority**: Must Have

### REQ-SC004 — HTTPS in Production
> Production deployment PHẢI chạy qua HTTPS (Nginx reverse proxy + TLS).

**Priority**: Must Have

---

## 8. Quality Requirements

### REQ-Q001 — Unit Test Coverage
> `shared/` module PHẢI đạt ≥ 80% test coverage cho domain và repository layers.

**Priority**: Should Have  
**Verification**: TST-Q001

### REQ-Q002 — CI Gate
> Pull requests PHẢI pass CI (all tests green) trước khi merge vào `main`.

**Priority**: Must Have

### REQ-Q003 — No Circular Dependencies
> Modules PHẢI KHÔNG có circular dependencies. `module_analyzer.py` phải report 0 circular deps.

**Priority**: Must Have  
**Verification**: TST-Q002

---

## 9. Traceability Summary

| REQ ID | Layer | Priority | Test ID | SDD Ref |
|--------|-------|----------|---------|---------|
| REQ-S001 | System | Must Have | TST-S001 | SDD-001 §3 |
| REQ-S002 | System | Must Have | TST-S002 | SDD-001 §4 |
| REQ-S003 | System | Must Have | TST-S003 | SDD-001 §4 |
| REQ-S004 | System | Must Have | TST-SC001 | SDD-001 §5 |
| REQ-S005 | System | Must Have | TST-N001 | SDD-001 §4.2 |
| REQ-A001 | Arch | Must Have | — | SDD-001 §2 |
| REQ-A002 | Arch | Must Have | — | SDD-001 §2.3 |
| REQ-A003 | Arch | Must Have | — | SDD-001 §2.4 |
| REQ-A004 | Arch | Must Have | — | SDD-001 §2 |
| REQ-N001 | Network | Must Have | TST-N001 | SDD-001 §4.2 |
| REQ-N002 | Network | Must Have | — | SDD-001 §4.1 |
| REQ-N003 | Network | Must Have | TST-N002 | SDD-001 §4.2 |
| REQ-N004 | Network | Should Have | TST-N003 | SDD-001 §4.3 |
| REQ-B001 | Backend | Must Have | — | SDD-001 §5 |
| REQ-B002 | Backend | Must Have | TST-SC002 | SDD-001 §5.3 |
| REQ-B003 | Backend | Must Have | TST-B001 | SDD-001 §5.1 |
| REQ-B004 | Backend | Must Have | — | SDD-001 §5.2 |
| REQ-B005 | Backend | Must Have | TST-B002 | SDD-001 §5.4 |
| REQ-B006 | Backend | Should Have | — | SDD-001 §5.4 |
| REQ-U001 | UI | Must Have | — | SDD-001 §6 |
| REQ-U002 | UI | Must Have | — | SDD-001 §6.2 |
| REQ-U003 | UI | Must Have | TST-U001 | SDD-001 §6.2 |
| REQ-U004 | UI | Must Have | — | SDD-001 §6.1 |
| REQ-SC001 | Security | Must Have | TST-SC001 | SDD-001 §5.3 |
| REQ-SC002 | Security | Must Have | TST-SC002 | SDD-001 §5.3 |
| REQ-SC003 | Security | Must Have | — | SDD-001 §4.1 |
| REQ-SC004 | Security | Must Have | — | SDD-001 §7 |
| REQ-Q001 | Quality | Should Have | TST-Q001 | — |
| REQ-Q002 | Quality | Must Have | — | SDP-001 §9 |
| REQ-Q003 | Quality | Must Have | TST-Q002 | — |

---

*See `docs/TM/TM-001_traceability.md` for full bidirectional traceability.*  
*Feature-specific requirements: `docs/SRS/SRS-NNN_<feature>.md`*

---
*Document: SDP-001 | Generated by `doc_generator.py` | Standard: IEC 62304 §5.2*
