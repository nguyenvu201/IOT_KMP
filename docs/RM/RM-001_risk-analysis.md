# RM-001: Risk Management — Risk Analysis & Register

| Field | Value |
|-------|-------|
| **Document ID** | RM-001 |
| **Version** | 1.0.0 |
| **Status** | APPROVED |
| **Author** | kmp-security |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §7, ISO 14971 §4, FDA 21 CFR 820.30(g) |

---

## 1. Purpose

Tài liệu này là **master risk register** cho hệ thống KMP IoT. Mỗi hazard được phân tích về severity, likelihood, và risk control measures linked đến requirements.

---

## 2. Risk Assessment Matrix

### 2.1 Severity Levels

| Level | Definition |
|-------|-----------|
| **Critical** | Mất kiểm soát thiết bị, gây nguy hiểm vật lý hoặc mất dữ liệu hoàn toàn |
| **High** | Vi phạm bảo mật, data corruption, major feature failure |
| **Medium** | Feature bị giảm chức năng, hiển thị data sai |
| **Low** | Cosmetic issues, minor UX degradation |

### 2.2 Likelihood Levels

| Level | Probability |
|-------|------------|
| **Almost Certain** | > 70% |
| **Likely** | 40–70% |
| **Possible** | 20–40% |
| **Unlikely** | 5–20% |
| **Rare** | < 5% |

### 2.3 Risk Level Calculation

| | Rare | Unlikely | Possible | Likely | Almost Certain |
|--|------|---------|---------|--------|---------------|
| **Critical** | High | High | Critical | Critical | Critical |
| **High** | Medium | High | High | Critical | Critical |
| **Medium** | Low | Medium | Medium | High | High |
| **Low** | Low | Low | Low | Medium | Medium |

---

## 3. Risk Register

### RISK-001 — MQTT Connection Loss

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-001 |
| **Hazard** | MQTT broker connection lost while devices are receiving commands |
| **Potential Harm** | Device continues previous command state without new updates from server |
| **Severity** | High |
| **Likelihood** | Possible (network instability, broker restart) |
| **Risk Level** | **HIGH** |
| **Control Measure** | Auto-reconnect với exponential backoff (1s→2s→4s→...→60s) REQ-N003 |
| **Residual Risk** | Medium (khi reconnecting thành công) |
| **Verification** | TST-N002 |
| **Status** | Open — Not yet tested |
| **SOUP Reference** | SOUP-I001 (HiveMQ Client) |

---

### RISK-002 — Invalid Sensor Data Corruption

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-002 |
| **Hazard** | Faulty sensor sends out-of-range values (e.g., temperature = 9999°C) |
| **Potential Harm** | Corrupt analytics, wrong alerts, incorrect automation decisions |
| **Severity** | High |
| **Likelihood** | Unlikely (hardware faults are occasional) |
| **Risk Level** | **HIGH** |
| **Control Measure** | Server-side validation of all sensor payloads — REQ-N001, REQ-S005 |
| **Control Detail** | `SensorPayload.isValid()` rejects values outside safe bounds |
| **Residual Risk** | Low |
| **Verification** | TST-N001 |
| **Status** | Open — Validation code exists, not formally tested |

---

### RISK-003 — JWT Secret Exposure

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-003 |
| **Hazard** | JWT_SECRET hardcoded in source code or committed to git |
| **Potential Harm** | Attacker mints valid tokens → unauthorized device control |
| **Severity** | Critical |
| **Likelihood** | Unlikely (if env vars enforced) |
| **Risk Level** | **HIGH** |
| **Control Measure** | Application fails to start if `JWT_SECRET` env var missing — REQ-SC002 |
| **Control Detail** | `System.getenv("JWT_SECRET") ?: error("JWT_SECRET not set")` |
| **Residual Risk** | Low (env var secret management remains user responsibility) |
| **Verification** | TST-SC002 |
| **Status** | Open |

---

### RISK-004 — Anonymous MQTT Access

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-004 |
| **Hazard** | MQTT broker allows anonymous connections |
| **Potential Harm** | Unauthorized client subscribes to device data, publishes fake commands |
| **Severity** | Critical |
| **Likelihood** | Possible (default Mosquitto config allows anonymous) |
| **Risk Level** | **CRITICAL** |
| **Control Measure** | `allow_anonymous false` in `mosquitto.conf` — REQ-SC003 |
| **Control Detail** | ACL file restricts per-device topic access |
| **Residual Risk** | Medium (if TLS not enabled, traffic can be intercepted) |
| **Verification** | Manual config review |
| **Status** | Open — `mosquitto.conf` needs production hardening |

---

### RISK-005 — RS485 Communication Failure

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-005 |
| **Hazard** | jSerialComm fails to open serial port (driver issue, port busy) |
| **Potential Harm** | RS485 devices unreachable, readings missed |
| **Severity** | Medium |
| **Likelihood** | Possible (especially on Linux permission issues) |
| **Risk Level** | **MEDIUM** |
| **Control Measure** | Handle `SerialPortInvalidPortException`, log error, expose in health endpoint |
| **Residual Risk** | Low |
| **Status** | Open |
| **SOUP Reference** | SOUP-I002 (jSerialComm) |

---

### RISK-006 — Database Connection Exhaustion

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-006 |
| **Hazard** | High MQTT message rate causes DB connection pool exhaustion |
| **Potential Harm** | Server unable to save sensor readings, data loss |
| **Severity** | High |
| **Likelihood** | Unlikely (HikariCP manages pool) |
| **Risk Level** | **HIGH** |
| **Control Measure** | HikariCP connection pool — REQ-B004 |
| **Control Detail** | Configure `maximumPoolSize` based on expected load |
| **Residual Risk** | Medium (requires tuning per deployment) |
| **Status** | Open |
| **SOUP Reference** | SOUP-D006 (HikariCP) |

---

### RISK-007 — Koin wasm-alpha Instability

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-007 |
| **Hazard** | Koin `3.6.0-wasm-alpha2` has breaking API changes |
| **Potential Harm** | Build failure after Koin update |
| **Severity** | Medium |
| **Likelihood** | Unlikely (not actively updating) |
| **Risk Level** | **MEDIUM** |
| **Control Measure** | Pin exact version in `libs.versions.toml`, update only when stable |
| **Residual Risk** | Low |
| **Status** | Accepted — Monitor for stable release |
| **SOUP Reference** | SOUP-C004, SOUP-C005 |

---

### RISK-008 — No Data Encryption at Rest

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-008 |
| **Hazard** | PostgreSQL database stores sensor data without encryption |
| **Potential Harm** | Sensitive location/behavior data exposed if DB accessed directly |
| **Severity** | Medium |
| **Likelihood** | Rare (internal network access required) |
| **Risk Level** | **Low** |
| **Control Measure** | DB only accessible via internal Docker network, not exposed |
| **Residual Risk** | Low |
| **Status** | Accepted for current scope |

---

## 4. Risk Summary

| Risk Level | Count | Risk IDs |
|-----------|-------|---------|
| Critical | 1 | RISK-004 |
| High | 3 | RISK-001, RISK-002, RISK-003 (6 after residual: RISK-006) |
| Medium | 2 | RISK-005, RISK-007 |
| Low | 1 | RISK-008 |

### 4.1 Critical Risks — Immediate Action Required

> ⚠️ **RISK-004** (Anonymous MQTT) — `mosquitto.conf` PHẢI được hardened trước khi production deployment.

---


### RISK-009 — (Feature: Hiển thị Cảm biến Nhiệt độ)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-009 |
| **Hazard** | MQTT data payload bị giả mạo (Data Injection/Tampering) |
| **Severity** | High |
| **Likelihood** | Possible |
| **Control** | Implement data validation logic before saving to database (e.g. range check: temp -50 to 100) |
| **REQ Ref** | REQ-N001 |
| **Status** | Open |

---

### RISK-010 — (Feature: Hiển thị Cảm biến Nhiệt độ)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-010 |
| **Hazard** | Network chập chờn mất dữ liệu cảm biến |
| **Severity** | Medium |
| **Likelihood** | Likely |
| **Control** | Sử dụng QoS 1 trong MQTT Subscriber để đảm bảo delivery, app Compose handle timeout/error state |
| **REQ Ref** | REQ-B008 |
| **Status** | Open |

---


### RISK-009 — (Feature: Temperature Sensor Dashboard)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-009 |
| **Hazard** | MQTT data payload bị giả mạo (Data Injection/Tampering) |
| **Severity** | High |
| **Likelihood** | Possible |
| **Control** | Implement data validation logic before saving to database (e.g. range check: temp -50 to 100) |
| **REQ Ref** | REQ-N001 |
| **Status** | Open |

---

### RISK-010 — (Feature: Temperature Sensor Dashboard)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-010 |
| **Hazard** | Network chập chờn mất dữ liệu cảm biến |
| **Severity** | Medium |
| **Likelihood** | Likely |
| **Control** | Sử dụng QoS 1 trong MQTT Subscriber để đảm bảo delivery, app Compose handle timeout/error state |
| **REQ Ref** | REQ-B008 |
| **Status** | Open |

---


### RISK-009 — (Feature: TemperatureDashBack)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-009 |
| **Hazard** | MQTT data payload bị giả mạo (Data Injection/Tampering) |
| **Severity** | High |
| **Likelihood** | Possible |
| **Control** | Implement data validation logic before saving to database (e.g. range check: temp -50 to 100) |
| **REQ Ref** | REQ-N001 |
| **Status** | Open |

---

### RISK-010 — (Feature: TemperatureDashBack)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-010 |
| **Hazard** | Network chập chờn mất dữ liệu cảm biến |
| **Severity** | Medium |
| **Likelihood** | Likely |
| **Control** | Sử dụng QoS 1 trong MQTT Subscriber để đảm bảo delivery, app Compose handle timeout/error state |
| **REQ Ref** | REQ-B008 |
| **Status** | Open |

---


### RISK-NEW — (Feature: Chart UI Issues)

| Field | Value |
|-------|-------|
| **Risk ID** | RISK-NEW |
| **Hazard** |  |
| **Severity** | Medium |
| **Likelihood** | Possible |
| **Control** |  |
| **REQ Ref** |  |
| **Status** | Open |

---

## 5. Risk Update Log

| Date | Risk ID | Change | Changed By |
|------|---------|--------|-----------|
| 2026-04-07 | All | Initial risk register | kmp-security |

---
*Document: RM-001 | Standard: IEC 62304 §7, ISO 14971 §4*  
*Auto-updated by `doc_generator.py --risk` after each security review*
| 2026-04-07 | New risks from `Hiển thị Cảm biến Nhiệt độ` | kmp-architect |
| 2026-04-07 | New risks from `Temperature Sensor Dashboard` | kmp-architect |
| 2026-04-07 | New risks from `TemperatureDashBack` | kmp-architect |
| 2026-04-07 | New risks from `Chart UI Issues` | kmp-architect |
