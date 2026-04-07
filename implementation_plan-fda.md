# FDA-Compliant Documentation System — KMP IoT

## Background

FDA software documentation (dựa trên **21 CFR Part 820**, **IEC 62304**, và FDA Guidance "General Principles of Software Validation") yêu cầu:

1. **Traceability** — Mỗi requirement phải traced đến design → implementation → test
2. **Document Control** — Mỗi doc có ID, version, status, author, date
3. **Bidirectional Traceability Matrix** — User Needs → SRS → SDD → Tests → Results
4. **Risk Management** — Hazard analysis, risk controls linked to requirements
5. **Change Control** — Mọi thay đổi phải có impact assessment và approval
6. **SOUP/OTS tracking** — Software Of Unknown Provenance (third-party libs)

---

## User Review Required

> [!IMPORTANT]
> FDA documentation chuẩn áp dụng cho **medical device software**. Với dự án KMP IoT này (không phải medical device), tôi sẽ áp dụng **FDA documentation spirit** — tức là rigor, traceability, structured format — nhưng scale-down hợp lý cho một IoT project. Điều này có nghĩa:
> - Có document ID + version + status header
> - Có requirement tracing (REQ-xxx → TST-xxx)
> - Có risk assessment per feature
> - Có SOUP/OTS lib inventory
> - Có change log với impact assessment

> [!WARNING]
> Việc thêm FDA fields vào **agent context JSON** sẽ làm các file `NN_agent.json` phức tạp hơn đáng kể. Agents sẽ phải điền thêm: requirement IDs, risk level, SOUP components sử dụng. Bạn cần confirm đây là hướng muốn đi.

---

## Proposed Changes

### Tài liệu FDA Documents (docs/ structure mới)

```
docs/
├── README.md                        ← Navigation index
│
├── SDP/                             ← Software Development Plan
│   └── SDP-001_software-dev-plan.md
│
├── SRS/                             ← Software Requirements Specification
│   ├── SRS-001_system-requirements.md   ← Master SRS
│   └── SRS-<feature>/               ← Per-feature SRS addendum
│       └── SRS-NNN_<feature>.md
│
├── SDD/                             ← Software Design Description
│   ├── SDD-001_architecture.md      ← System architecture
│   └── SDD-<feature>/               ← Per-feature design
│       └── SDD-NNN_<feature>.md
│
├── VVP/                             ← Verification & Validation Plan
│   └── VVP-001_test-plan.md
│
├── VVR/                             ← Verification & Validation Results
│   └── VVR-NNN_<feature>_results.md
│
├── TM/                              ← Traceability Matrix
│   └── TM-001_traceability.md       ← LIVE document, updated per feature
│
├── RM/                              ← Risk Management
│   ├── RM-001_risk-analysis.md      ← Master risk register
│   └── RM-NNN_<feature>_risk.md
│
├── CM/                              ← Change Management
│   └── CM-001_change-log.md         ← Thay thế CHANGELOG.md cũ
│
├── SOUP/                            ← Software of Unknown Provenance
│   └── SOUP-001_ots-inventory.md    ← All third-party libs
│
├── REL/                             ← Release Notes
│   └── REL-NNN_v<x.y.z>.md
│
└── ADR/                             ← Architecture Decision Records
    └── ADR-NNN_<title>.md
```

---

### Agent Context Schema mới — FDA-aligned fields

Mỗi agent JSON output sẽ thêm section `_fda`:

#### Architect (`01_architect.json`)
```json
{
  "_fda": {
    "doc_refs": ["SRS-001", "SDD-001"],
    "soups_introduced": [
      { "name": "Voyager", "version": "1.1.0-beta03", "purpose": "Navigation", "source": "Maven" }
    ],
    "risks": [
      { "id": "RISK-001", "hazard": "MQTT connection loss", "severity": "medium",
        "control": "Reconnect logic with exponential backoff" }
    ]
  }
}
```

#### Shared (`02_shared.json`)
```json
{
  "_fda": {
    "requirements_implemented": ["REQ-010", "REQ-011"],
    "doc_ref": "SDD-NNN",
    "soups_used": ["Koin 3.6.0", "kotlinx.serialization 1.7.3"]
  }
}
```

#### Ktor (`04_ktor.json`)
```json
{
  "_fda": {
    "requirements_implemented": ["REQ-020", "REQ-021"],
    "api_contracts_ref": "SRS-NNN §3.2",
    "soups_used": ["Ktor 3.3.3", "Exposed 0.49.0", "HiveMQ 1.3.3"]
  }
}
```

#### QA (`07_qa.json`)
```json
{
  "_fda": {
    "vvr_ref": "VVR-NNN",
    "tests": [
      { "id": "TST-001", "req_ref": "REQ-010", "result": "PASS", "date": "..." }
    ],
    "coverage_meets_threshold": true
  }
}
```

---

### Scripts cần tạo/update

#### [MODIFY] `doc_generator.py`
- Thêm generators cho: SRS addendum, SDD, VVR, RM, CM, TM update, SOUP inventory

#### [NEW] `docs/SDP/SDP-001_software-dev-plan.md`
- Master Software Development Plan — document cố định, viết 1 lần

#### [NEW] `docs/SRS/SRS-001_system-requirements.md`
- Master system requirements — base requirements cho toàn bộ IoT system

#### [NEW] `docs/TM/TM-001_traceability.md`
- Traceability matrix format: `REQ-xxx | Description | SDD ref | TST-xxx | Result`

#### [NEW] `docs/SOUP/SOUP-001_ots-inventory.md`
- Inventory tất cả third-party libs từ `libs.versions.toml`

#### [NEW] `docs/RM/RM-001_risk-analysis.md`
- Risk register với hazard analysis, severity, likelihood, controls

---

## Document ID Convention

```
<TYPE>-<NNN>_<slug>
```

| Type | Document | Example |
|------|----------|---------|
| `SDP` | Software Dev Plan | `SDP-001_software-dev-plan` |
| `SRS` | Software Requirements Spec | `SRS-002_bluetooth-feature` |
| `SDD` | Software Design Description | `SDD-002_bluetooth-feature` |
| `VVP` | V&V Plan | `VVP-001_test-plan` |
| `VVR` | V&V Results | `VVR-002_bluetooth-results` |
| `TM` | Traceability Matrix | `TM-001_traceability` |
| `RM` | Risk Management | `RM-001_risk-analysis` |
| `CM` | Change Management | `CM-001_change-log` |
| `SOUP` | OTS/Library Inventory | `SOUP-001_ots-inventory` |
| `REL` | Release Notes | `REL-001_v1.0.0` |
| `ADR` | Architecture Decision | `ADR-001_use-voyager-navigation` |
| `REQ` | Requirement ID (inline, not file) | `REQ-010` |
| `TST` | Test Case ID (inline) | `TST-010` |
| `RISK` | Risk item ID (inline) | `RISK-010` |

---

## Requirement ID Numbering

```
REQ-<layer><NNN>

REQ-S010   System level (S)
REQ-A010   Architecture (A)  
REQ-N010   Network / IoT (N)
REQ-U010   UI / Compose (U)
REQ-B010   Backend / Ktor (B)
REQ-Q010   Quality / Test (Q)
REQ-SC010  Security (SC)
```

---

## Verification Plan

1. Tạo toàn bộ base FDA documents (SDP, SRS-001, TM, SOUP, RM)
2. Update `doc_generator.py` để generate FDA-format documents
3. Chạy dry-run và verify output
4. Test với existing workflow context

## Open Questions

> [!IMPORTANT]
> 1. **SOUP tracking level**: Có cần track từng transitive dependency không, hay chỉ top-level (những gì trong `libs.versions.toml`)?
> 2. **Requirement numbering**: Tôi đề xuất prefix theo layer (REQ-B010 cho Backend). Bạn có muốn đơn giản hơn không, ví dụ REQ-001, REQ-002, ...?
> 3. **Language**: Documents viết hoàn toàn bằng **tiếng Anh** (FDA standard), hay mix Anh-Việt để dễ đọc hơn?
> 4. **Risk severity scale**: Dùng **Low/Medium/High/Critical** hay scale số (1-5)?
