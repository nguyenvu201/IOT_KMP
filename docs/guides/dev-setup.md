# Developer Setup Guide

> **Updated**: 2026-04-07 13:04 UTC

## Prerequisites

| Tool | Version | Install |
|------|---------|---------|
| JDK | 17+ | `brew install temurin@17` |
| Android Studio | Hedgehog+ | [developer.android.com](https://developer.android.com/studio) |
| Docker | 24.0+ | [docker.com](https://docker.com) |
| Python | 3.11+ | `brew install python` |
| IntelliJ IDEA | 2024.1+ | For server development |

## Quick Start

```bash
# 1. Clone project
git clone <repo-url>
cd KMPRoadMap

# 2. Start backend services (PostgreSQL + MQTT + Ktor)
docker-compose up -d

# 3. Run Android app
./gradlew :composeApp:installDebug

# 4. Run Desktop app
./gradlew :composeApp:run

# 5. Run Server locally (without Docker)
./gradlew :server:run
```

## Environment Variables

Copy `.env.example` to `.env` and fill in:

```env
JWT_SECRET=<min-32-char-random-string>
DB_PASSWORD=<postgres-password>
MQTT_USERNAME=ktor-server
MQTT_PASSWORD=<mqtt-password>
DATABASE_URL=jdbc:postgresql://localhost:5432/iotdb
```

Generate JWT secret:
```bash
python3 -c "import secrets; print(secrets.token_hex(32))"
```

## Project Structure

```
KMPRoadMap/
├── shared/          # KMP shared module (domain logic)
├── composeApp/      # Android + Desktop UI
├── server/          # Ktor backend
├── iosApp/          # iOS wrapper
├── docs/            # Project documentation ← you are here
│   ├── features/    # Feature-level docs
│   ├── api/         # API reference
│   ├── architecture/ # Architecture overview
│   ├── adrs/        # Architecture Decision Records
│   └── guides/      # Developer guides
└── .claude/         # AI agent configuration
    ├── agents/      # Specialized agents
    ├── commands/    # Workflow commands
    └── skills/      # Automation scripts
```

## AI Agent Workflow

```bash
# Khởi tạo workflow mới
python .claude/skills/kmp-tools/scripts/context_manager.py init <feature-name> "<description>"

# Xem trạng thái
python .claude/skills/kmp-tools/scripts/context_manager.py status

# Generate docs sau khi xong
python .claude/skills/kmp-tools/scripts/doc_generator.py --all
```

## Running Tests

```bash
# Tất cả tests
./gradlew allTests

# Chỉ shared module (nhanh hơn)
./gradlew :shared:jvmTest

# Server tests (cần PostgreSQL running)
./gradlew :server:test
```

## Code Style

- Kotlin: follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Package: `caonguyen.vu.*`
- SharedMain first: implement logic trong `commonMain` trước, platform-specific sau
