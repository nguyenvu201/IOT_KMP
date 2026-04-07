# VVP-001: Verification & Validation Plan

| Field | Value |
|-------|-------|
| **Document ID** | VVP-001 |
| **Version** | 1.0.0 |
| **Status** | APPROVED |
| **Author** | kmp-qa |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §5.6, §5.7, FDA 21 CFR 820.30(f) |

---

## 1. Purpose

Tài liệu này định nghĩa chiến lược kiểm thử (Verification & Validation) cho toàn bộ hệ thống KMP IoT.

- **Verification**: "Are we building the product right?" — Code implements design correctly
- **Validation**: "Are we building the right product?" — System meets user needs

---

## 2. Test Levels

| Level | Scope | Framework | Who |
|-------|-------|----------|-----|
| Unit | Domain models, Repositories, ViewModels | `kotlin-test` + `coroutines-test` | kmp-qa |
| Integration | Ktor routes, DB operations, MQTT bridge | `ktor-server-test-host` | kmp-qa |
| System (V) | End-to-end critical paths | Manual + script | kmp-qa + kmp-devops |
| Security (V) | Auth, MQTT config, secrets | Code review + manual | kmp-security |

---

## 3. Test Coverage Targets (REQ-Q001)

| Module | Layer | Target |
|--------|-------|--------|
| `shared/` | Domain models | 90%+ |
| `shared/` | Repository interfaces + Fakes | 85%+ |
| `server/` | Business services | 80%+ |
| `server/` | API routes | 75%+ |
| `composeApp/` | ViewModels | 80%+ |
| `composeApp/` | Screens | Critical paths |

---

## 4. Test Suite Structure

```
shared/src/commonTest/kotlin/caonguyen/vu/
├── models/                   # Domain model validation tests
├── repository/               # Repository + Fake tests  
└── usecase/                  # Use case tests (if applicable)

server/src/test/kotlin/caonguyen/vu/
├── routes/                   # API route tests (testApplication)
│   ├── DeviceRoutesTest.kt
│   └── AuthRoutesTest.kt
└── service/                  # Service + DAO tests

composeApp/src/commonTest/kotlin/caonguyen/vu/
└── ui/                       # ViewModel tests (runTest)
```

---

## 5. Test Execution Commands

```bash
# All tests (CI gate — REQ-Q002)
./gradlew allTests

# Shared module (fast feedback)
./gradlew :shared:jvmTest

# Server tests
./gradlew :server:test

# Android unit tests
./gradlew :composeApp:testDebugUnitTest

# Module analyzer — no circular deps (TST-Q002)
python .claude/skills/kmp-tools/scripts/architecture/module_analyzer.py .
```

---

## 6. Test Case Registry

All test cases are defined in `docs/TM/TM-001_traceability.md` §4.

---
*Document: VVP-001 | Standard: IEC 62304 §5.6 | References: SRS-001, TM-001*
