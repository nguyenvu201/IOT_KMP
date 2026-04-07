# SDP-001: Software Development Plan

| Field | Value |
|-------|-------|
| **Document ID** | SDP-001 |
| **Version** | 1.0.0 |
| **Status** | APPROVED |
| **Author** | KMP Engineering Team |
| **Created** | 2026-04-07 |
| **Last Updated** | 2026-04-07 |
| **Standard Reference** | IEC 62304 §5, FDA 21 CFR 820.30 |

---

## 1. Purpose & Scope

Tài liệu này định nghĩa quy trình phát triển phần mềm cho dự án **KMP IoT** — một hệ thống giám sát và điều khiển thiết bị IoT xây dựng trên nền Kotlin Multiplatform.

**Scope**: Toàn bộ phần mềm trong repository `KMPRoadMap`, bao gồm:
- `composeApp/` — Compose Multiplatform client (Android, Desktop)
- `server/` — Ktor backend server (JVM)
- `shared/` — KMP shared domain logic

**Not in scope**: ESP8266 firmware, MQTT broker configuration.

---

## 2. Software Safety Classification

Theo IEC 62304 §4.3 và mức độ rủi ro của hệ thống IoT:

| Module | Classification | Rationale |
|--------|---------------|-----------|
| `shared/` — domain logic | **Class B** | Gây ra tình trạng không an toàn nếu data sai (sensor readings) |
| `server/` — Ktor backend | **Class B** | Xử lý lệnh điều khiển thiết bị qua MQTT |
| `composeApp/` — UI | **Class A** | Chỉ hiển thị, không điều khiển trực tiếp phần cứng |
| IoT communication layer | **Class B** | MQTT command routing đến thiết bị vật lý |

---

## 3. Software Development Life Cycle (SDLC)

### 3.1 Process Overview

```
Feature Request
      │
      ▼
[PLAN] context_manager.py init → 00_workflow.json
      │
      ▼
[DESIGN] kmp-architect → 01_architect.json → SRS addendum + SDD
      │
      ├── [IMPLEMENT - Parallel]
      │   ├── kmp-shared    → 02_shared.json
      │   ├── kmp-iot       → 03_iot.json
      │   ├── kmp-ktor      → 04_ktor.json
      │   └── kmp-compose   → 05_compose.json
      │
      ▼
[VERIFY] kmp-qa → 07_qa.json → VVR
      │
      ├── [PARALLEL]
      │   ├── kmp-devops    → 06_devops.json
      │   └── kmp-security  → 08_security.json
      │
      ▼
[DOCUMENT] doc_generator.py → update SRS, SDD, VVR, TM, RM, CM
      │
      ▼
[RELEASE] git tag + REL document
```

### 3.2 Phase Entry/Exit Criteria

| Phase | Entry Criteria | Exit Criteria |
|-------|---------------|---------------|
| Design | `00_workflow.json` exists | `01_architect.json` written, SRS/SDD drafted |
| Implementation | SRS approved, SDD complete | All `NN_agent.json` files written, no blockers |
| Verification | All implementation complete | `07_qa.json` `all_tests_pass: true` |
| Release | V&V complete, no critical security issues | REL document created, git tag pushed |

---

## 4. Roles & Responsibilities

| Role | Responsibility | Agent |
|------|---------------|-------|
| Architect | System design, ADRs, API contracts | `kmp-architect` |
| Shared Developer | Domain models, Repository, Koin | `kmp-shared` |
| IoT Engineer | MQTT topics, device schemas | `kmp-iot` |
| Backend Developer | Ktor routes, DB, JWT | `kmp-ktor-backend` |
| UI Developer | Compose screens, ViewModels | `kmp-compose` |
| QA Engineer | Test planning, execution | `kmp-qa` |
| DevOps | CI/CD, Docker, deployment | `kmp-devops` |
| Security Engineer | MQTT security, JWT review | `kmp-security` |
| Orchestrator | Workflow coordination, doc generation | `kmp-orchestrator` |

---

## 5. Document Control

### 5.1 Document Types

| Type | ID Pattern | Description |
|------|-----------|-------------|
| Software Development Plan | `SDP-NNN` | This document |
| Software Requirements Spec | `SRS-NNN` | Requirements per feature |
| Software Design Description | `SDD-NNN` | Design per feature |
| V&V Plan | `VVP-NNN` | Test plan |
| V&V Results | `VVR-NNN` | Test execution results |
| Traceability Matrix | `TM-NNN` | REQ → SDD → TST linkage |
| Risk Management | `RM-NNN` | Hazard analysis, risk register |
| Change Management | `CM-NNN` | Change control log |
| SOUP Inventory | `SOUP-NNN` | Third-party software list |
| Release Notes | `REL-NNN` | Per-release documentation |
| Architecture Decision | `ADR-NNN` | Architecture decisions |

### 5.2 Requirement ID Convention

```
REQ-<layer><NNN>

Layer codes:
  S  = System level
  A  = Architecture
  N  = Network / IoT
  U  = UI / Compose
  B  = Backend / Ktor
  Q  = Quality / Test
  SC = Security
```

Examples: `REQ-S001`, `REQ-B010`, `REQ-SC001`

### 5.3 Version Control

- All documents stored in `docs/` under git version control
- Version format: `MAJOR.MINOR.PATCH`
- Changes tracked in `docs/CM/CM-001_change-log.md`
- Document status: `DRAFT` → `REVIEW` → `APPROVED` → `OBSOLETE`

---

## 6. Configuration Management

### 6.1 Branching Strategy
- `main` — production-ready code
- `develop` — integration branch
- `feature/<name>` — feature development

### 6.2 Commit Convention
```
<type>(<scope>): <description>

Types: feat, fix, docs, test, refactor, chore
Scopes: shared, server, composeApp, docs

Examples:
feat(server): add device registration endpoint REQ-B010
docs(SRS): add requirements for bluetooth feature
fix(shared): handle MQTT disconnect race condition RISK-004
```

---

## 7. SOUP Management

See `docs/SOUP/SOUP-001_ots-inventory.md` for the complete list of all third-party software components.

All SOUP components must:
1. Be listed with name, version, purpose, and source
2. Have documented risk assessment per IEC 62304 §8.1.2
3. Be updated when versions change (triggers CM entry)

---

## 8. Risk Management

Risk management follows `docs/RM/RM-001_risk-analysis.md`.

**Severity Scale:**

| Level | Definition | Examples |
|-------|-----------|---------|
| Critical | System failure, data loss, safety hazard | MQTT broker offline causing uncontrolled devices |
| High | Major feature broken, security breach | JWT secret exposed, RS485 data corruption |
| Medium | Feature degraded, misleading data | Wrong sensor reading displayed |
| Low | Minor UX issue, cosmetic bug | UI layout issue on desktop |

**Likelihood Scale:** `Rare / Unlikely / Possible / Likely / Almost Certain`

---

## 9. Verification & Validation

### 9.1 Test Levels

| Level | Framework | Target | Coverage Target |
|-------|----------|--------|----------------|
| Unit | `kotlin-test` + `coroutines-test` | Domain, Repository, ViewModel | 80%+ |
| Integration | `ktor-server-test-host` | API routes, DB, MQTT | 70%+ |
| System | Manual + automated | End-to-end IoT flow | Critical paths |

### 9.2 Test Execution
```bash
# All tests
./gradlew allTests

# Shared module only (fast)
./gradlew :shared:jvmTest

# Server tests
./gradlew :server:test
```

Results documented in `docs/VVR/VVR-NNN_<feature>_results.md`.

---

## 10. Release Process

1. All V&V tests pass (`07_qa.json` → `all_tests_pass: true`)
2. No Critical/High security issues (`08_security.json` → `critical_issues: []`)
3. Create `docs/REL/REL-NNN_v<x.y.z>.md`
4. Update `docs/TM/TM-001_traceability.md`
5. Git tag: `git tag -a v<x.y.z> -m "release: <description>"`
6. Docker build and deploy

---

*Document generated by KMP Documentation System — `doc_generator.py`*  
*Standard: IEC 62304-inspired, FDA 21 CFR 820.30*
