# SOUP-001: Software of Unknown Provenance — OTS Inventory

| Field | Value |
|-------|-------|
| **Document ID** | SOUP-001 |
| **Version** | 1.0.0 |
| **Status** | APPROVED |
| **Author** | kmp-devops |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §8.1, FDA Guidance "OTS Software" |
| **Source** | `gradle/libs.versions.toml` |

---

## 1. Purpose

Tài liệu này liệt kê tất cả **Software of Unknown Provenance (SOUP)** — các thư viện và framework bên thứ ba được sử dụng trong dự án KMP IoT. Theo IEC 62304 §8, mỗi SOUP component phải được đánh giá rủi ro và track version.

**Update trigger**: Mỗi khi thêm dependency mới hoặc update version → tạo entry trong `docs/CM/CM-001_change-log.md`.

---

## 2. Core Language & Runtime

| SOUP ID | Name | Version | Category | Purpose | Risk Assessment |
|---------|------|---------|----------|---------|----------------|
| SOUP-S001 | Kotlin Multiplatform | 2.3.0 | Runtime | Core language, KMP compilation | Low — JetBrains, widely used |
| SOUP-S002 | JVM (Temurin) | 17 LTS | Runtime | Server + Desktop execution | Low — Eclipse Foundation, LTS |
| SOUP-S003 | Android SDK | API 24–36 | Platform | Android target | Low — Google, official SDK |

---

## 3. UI Framework

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-U001 | Compose Multiplatform | 1.10.0 | `composeApp` | Shared UI framework Android + Desktop | Medium — JetBrains, beta for some targets |
| SOUP-U002 | Voyager Navigator | 1.1.0-beta03 | `composeApp` | Screen navigation | Medium — beta version, API may change |
| SOUP-U003 | Voyager Transitions | 1.1.0-beta03 | `composeApp` | Page transitions | Medium — same as above |
| SOUP-U004 | Voyager Koin | 1.1.0-beta03 | `composeApp` | Voyager + Koin integration | Medium — beta version |
| SOUP-U005 | Material3 | 1.10.0-alpha05 | `composeApp` | UI design system | Medium — alpha version |
| SOUP-U006 | AndroidX Activity Compose | 1.12.2 | `composeApp` (Android) | Android Activity integration | Low — Google, stable |
| SOUP-U007 | AndroidX AppCompat | 1.7.1 | `composeApp` (Android) | Android backward compat | Low — Google, stable |
| SOUP-U008 | AndroidX Lifecycle | 2.9.6 | `composeApp` | ViewModel, lifecycle-aware | Low — Google, stable |

---

## 4. Backend Framework

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-B001 | Ktor Server Core | 3.3.3 | `server` | HTTP server framework | Low — JetBrains, production-ready |
| SOUP-B002 | Ktor Netty Engine | 3.3.3 | `server` | HTTP server engine | Low — Netty widely proven |
| SOUP-B003 | Ktor WebSockets | 3.3.3 | `server` | WebSocket server | Low — stable |
| SOUP-B004 | Ktor Auth JWT | 3.3.3 | `server` | JWT authentication | Low — standard library |
| SOUP-B005 | Ktor CORS | 3.3.3 | `server` | Cross-origin policy | Low — standard library |
| SOUP-B006 | Ktor Client Core | 3.3.3 | `shared` | HTTP client (KMP) | Low — JetBrains |
| SOUP-B007 | Ktor Client OkHttp | 3.3.3 | `composeApp` (Android) | HTTP client engine Android | Low — Square, widely used |
| SOUP-B008 | Logback Classic | 1.5.24 | `server` | Logging framework | Low — widely used, SLF4J compatible |

---

## 5. Database

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-D001 | Exposed Core | 0.49.0 | `server` | Kotlin ORM | Low — JetBrains, actively maintained |
| SOUP-D002 | Exposed DAO | 0.49.0 | `server` | DAO pattern | Low — same |
| SOUP-D003 | Exposed JDBC | 0.49.0 | `server` | JDBC bridge | Low — same |
| SOUP-D004 | Exposed Kotlin DateTime | 0.49.0 | `server` | DateTime support | Low — same |
| SOUP-D005 | PostgreSQL JDBC | 42.7.2 | `server` | PostgreSQL driver | Low — official driver, widely used |
| SOUP-D006 | HikariCP | 5.1.0 | `server` | Connection pooling | Low — industry standard |

---

## 6. IoT / Communication

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-I001 | HiveMQ MQTT Client | 1.3.3 | `server`, `shared` | MQTT 5 client | **Medium** — IoT communication, connection loss risk |
| SOUP-I002 | jSerialComm | 2.10.4 | `server` | RS485 serial communication | **Medium** — hardware I/O, platform-specific |

**Risk mitigation for SOUP-I001 (HiveMQ)**:
- Implement automatic reconnect logic (REQ-N003)
- Validate all incoming MQTT payloads (REQ-N001)
- Log all connection events

**Risk mitigation for SOUP-I002 (jSerialComm)**:
- Handle `SerialPortInvalidPortException` gracefully
- Implement read timeout
- Log all serial communication errors

---

## 7. Shared / Core

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-C001 | kotlinx.serialization | 1.7.3 | All | JSON serialization/deserialization | Low — JetBrains, stable |
| SOUP-C002 | kotlinx.coroutines | 1.10.2 | All | Async/concurrent programming | Low — JetBrains, core library |
| SOUP-C003 | kotlinx.coroutines-swing | 1.10.2 | `composeApp` (Desktop) | Swing dispatcher for Desktop | Low — same |
| SOUP-C004 | Koin Core | 3.6.0-wasm-alpha2 | All | Dependency injection | **Medium** — wasm-alpha version, unstable suffix |
| SOUP-C005 | Koin Compose | 3.6.0-wasm-alpha2 | `composeApp` | Koin for Compose | **Medium** — same |
| SOUP-C006 | BuildKonfig | 0.15.1 | All | Build-time config injection | Low — small scope tool |

**Risk note for SOUP-C004/C005 (Koin wasm-alpha)**: Koin `3.6.0-wasm-alpha2` suffix suggests pre-release for Wasm target. Core KMP functionality is stable. Monitor release notes for stable version and update when available.

---

## 8. Testing

| SOUP ID | Name | Version | Module | Purpose | Risk Assessment |
|---------|------|---------|--------|---------|----------------|
| SOUP-T001 | kotlin-test | 2.3.0 | All (test) | Unit testing framework | Low — JetBrains, matches Kotlin version |
| SOUP-T002 | kotlin-test-junit | 2.3.0 | `server` (test) | JUnit runner integration | Low — stable |
| SOUP-T003 | kotlinx-coroutines-test | 1.10.2 | All (test) | Coroutine test utilities | Low — JetBrains |
| SOUP-T004 | JUnit 4 | 4.13.2 | `composeApp` (test) | Android unit test runner | Low — widely used |
| SOUP-T005 | AndroidX Test Espresso | 3.7.0 | `composeApp` (Android test) | UI automation tests | Low — Google |
| SOUP-T006 | Ktor Server Test Host | 3.3.3 | `server` (test) | In-process server testing | Low — same as SOUP-B001 |
| SOUP-T007 | Compose Hot Reload | 1.0.0 | Dev only | Development live reload | Low — dev tool only, not in production |

---

## 9. Risk Summary

| Risk Level | Count | SOUP IDs |
|-----------|-------|---------|
| Critical | 0 | — |
| High | 0 | — |
| Medium | 5 | SOUP-U001, SOUP-U002–004, SOUP-I001, SOUP-I002, SOUP-C004–005 |
| Low | 24 | All others |

---

## 10. Update Log

| Date | Change | Changed By |
|------|--------|-----------|
| 2026-04-07 | Initial inventory from `libs.versions.toml` | kmp-devops |

---
*Document: SOUP-001 | Source: `gradle/libs.versions.toml` | Standard: IEC 62304 §8.1*
