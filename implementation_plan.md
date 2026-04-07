# Chuyển đổi claude-code → .claude cho KMP Tech Stack

## Mục tiêu

Chuyển đổi toàn bộ nội dung thư mục `claude-code/` (được thiết kế cho Web/Node.js stack) thành phù hợp với **Kotlin Multiplatform (KMP)** tech stack của dự án IoT này, rồi sắp xếp vào `.claude/` để có thể tạo rules và workflows.

---

## Tech Stack thực tế của dự án

| Layer | Công nghệ |
|-------|-----------|
| **Shared logic** | Kotlin Multiplatform (KMP) |
| **UI (Android/Desktop)** | Compose Multiplatform |
| **UI (iOS)** | SwiftUI + KMP shared |
| **Backend** | Ktor (Kotlin) trên server |
| **Database** | SQLDelight (local), PostgreSQL (server) |
| **Networking** | Ktor Client (KMP), MQTT, WebSocket |
| **DI** | Koin |
| **Async** | Kotlin Coroutines + Flow |
| **IoT** | ESP8266, RS485, MQTT protocol |
| **Build** | Gradle KTS, Kotlin DSL |
| **CI/CD** | Docker, docker-compose |
| **Testing** | Kotlin Test, Kotest, MockK |

---

## Cấu trúc đề xuất cho `.claude/`

```
.claude/
├── agents/                         # Specialist agents cho KMP
│   ├── kmp-orchestrator.md         # Master coordinator (thay engineering-orchestrator)
│   ├── kmp-architect.md            # KMP architecture (thay senior-architect)
│   ├── kmp-shared.md               # Shared module specialist (THÊM MỚI)
│   ├── kmp-compose.md              # Compose Multiplatform UI (thay senior-frontend)
│   ├── kmp-ktor-backend.md         # Ktor server (thay senior-backend)
│   ├── kmp-iot.md                  # IoT/MQTT/ESP8266 (THÊM MỚI)
│   ├── kmp-devops.md               # Docker/CI-CD cho KMP (thay senior-devops)
│   ├── kmp-qa.md                   # Kotlin Test/Kotest/MockK (thay senior-qa)
│   └── kmp-security.md             # Security + JWT + MQTT security (thay senior-security)
│
├── skills/                         # Automation tooling
│   └── kmp-tools/
│       ├── SKILL.md                # Skill documentation
│       └── scripts/
│           ├── architecture/       # Module dependency analysis
│           ├── shared/             # KMP shared module scaffolding
│           ├── compose/            # Compose component generation
│           ├── ktor/               # Ktor route scaffolding
│           ├── iot/                # IoT/MQTT integration tools
│           ├── devops/             # Docker/Gradle build tools
│           └── qa/                 # Test generation (Kotest, MockK)
│
├── commands/                       # Slash command workflows (Antigravity)
│   ├── new-kmp-feature.md          # Feature mới trên KMP stack
│   ├── new-iot-device.md           # Tích hợp thiết bị IoT mới (THÊM MỚI)
│   ├── kmp-deploy.md               # Deploy Ktor server + Docker
│   └── kmp-security-audit.md      # Security review cho IoT/KMP
│
└── context/                        # Inter-agent communication
    ├── README.md                   # Context protocol (KMP version)
    └── kmp-context.json            # Master context template
```

---

## Chi tiết thay đổi từng file

### Agents

#### [MODIFY] `engineering-orchestrator.md` → `kmp-orchestrator.md`
- Thay danh sách specialists từ Web sang KMP agents
- Cập nhật workflow patterns: `kmp-architect` → `kmp-shared` → `kmp-ktor-backend` → `kmp-compose` → `kmp-qa` → `kmp-devops`
- Thêm flow đặc biệt cho **IoT feature**: `kmp-iot` → `kmp-shared` → `kmp-compose`
- Thay script references từ `engineering-tools` sang `kmp-tools`

#### [MODIFY] `senior-architect.md` → `kmp-architect.md`
- Tech stack: Thay React/Node.js → KMP + Compose Multiplatform + Ktor
- Architecture patterns: Clean Architecture + KMP Module Structure
- Thay C4 diagrams bằng KMP module dependency diagrams
- Thay Prisma/PostgreSQL → SQLDelight + Exposed (Ktor)
- Thêm KMP platform target design (Android, iOS, Desktop, Server)

#### [NEW] `kmp-shared.md`
- Chuyên gia về `shared/` module của KMP (business logic, domain, data)
- Expect/actual pattern, SKIE, Koin DI
- Coroutines + StateFlow + Repository pattern
- Platform-specific implementations

#### [MODIFY] `senior-frontend.md` → `kmp-compose.md`
- Thay React/Next.js → Compose Multiplatform
- MaterialDesign 3, Navigation Compose
- Thay Playwright → Compose UI Testing
- Adaptive layouts (Phone / Tablet / Desktop)
- Platform-specific UI (iOS SwiftUI bridging)

#### [MODIFY] `senior-backend.md` → `kmp-ktor-backend.md`
- Thay Node.js/Express → Ktor (Kotlin)
- Thay Prisma → Exposed ORM + SQLDelight
- JWT auth với Ktor Authentication plugin
- WebSocket support natively (Ktor)
- MQTT broker integration

#### [NEW] `kmp-iot.md`
- ESP8266/ESP32 integration patterns
- MQTT protocol (Mosquitto, HiveMQ)
- RS485/Modbus protocols
- WebSocket real-time communication
- Sensor data schemas + validation

#### [MODIFY] `senior-devops.md` → `kmp-devops.md`
- Giữ Docker/docker-compose nhưng cho Ktor server
- Thay K8s → đơn giản hơn (Raspberry Pi deployment)
- Thêm Gradle multi-module build optimization
- Android APK/iOS IPA distribution
- GitHub Actions cho KMP (Android + iOS + Desktop)

#### [MODIFY] `senior-qa.md` → `kmp-qa.md`
- Thay Playwright/Vitest → Kotest + MockK
- Kotlin Coroutine testing (runTest)
- Compose UI Testing
- Shared module unit testing pattern
- MQTT/WebSocket integration testing

#### [MODIFY] `senior-security.md` → `kmp-security.md`
- JWT/Auth0 với Ktor
- MQTT broker security (TLS, ACL)
- IoT device security (firmware, communication)
- Kotlin Coroutine-safe patterns

---

### Commands (Antigravity Workflows)

#### [MODIFY] `new-feature.md` → `new-kmp-feature.md`
- Workflow: `kmp-architect` → `kmp-shared` → `kmp-ktor-backend` → `kmp-compose` → `kmp-qa` → `kmp-devops`

#### [NEW] `new-iot-device.md`
- Workflow tích hợp thiết bị IoT mới
- `kmp-iot` → `kmp-shared` → `kmp-compose` → `kmp-qa`

#### [MODIFY] `deploy.md` → `kmp-deploy.md`
- Pre-deployment: Gradle build + unit tests
- Deploy Ktor server via Docker
- Android APK build + distribution

#### [MODIFY] `security-audit.md` → `kmp-security-audit.md`
- IoT-specific security audit flow

---

### Context Files

#### [MODIFY] `engineering-context.json` → `kmp-context.json`
- Cập nhật schema cho KMP tech stack
- Thêm IoT device configuration
- Thêm platform targets (android, ios, desktop, server)

#### [MODIFY] `context/README.md`
- Cập nhật agent dependencies cho KMP

---

## Lưu ý về cấu trúc `.claude/`

> [!IMPORTANT]
> Antigravity (AI này) đọc commands từ `.claude/commands/` và skills từ `.claude/skills/`. Structures này cần đúng format để hệ thống nhận ra.

> [!NOTE]
> Các file `skills/kmp-tools/scripts/` sẽ là Python scripts đơn giản (stubs), không cần đầy đủ như file gốc vì đây là KMP/Kotlin project.

---

## Thứ tự thực thi

1. Tạo cấu trúc thư mục `.claude/` đầy đủ
2. Tạo 9 agent files (KMP-adapted)
3. Tạo SKILL.md cho `kmp-tools`
4. Tạo 4 command files (workflow)
5. Cập nhật context files
6. Xóa hoặc archive thư mục `claude-code/` gốc

---

## Verification Plan

- Kiểm tra tất cả agent files có đúng YAML frontmatter
- Kiểm tra commands files có đúng format Antigravity
- Kiểm tra `.claude/` không conflict với existing skills folder
