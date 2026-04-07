---
description: Bắt đầu luồng phát triển chức năng mới theo chuẩn FDA SDLC
---

// turbo-all

# New Feature Workflow (FDA-Aligned SDLC)

Quy trình này sẽ hướng dẫn agent (đóng vai trò Orchestrator) thực hiện đầu cuối một chức năng mới theo quy trình FDA-compliant.

## Bước 1: Workflow Init (Phase 0)
- **MANDATORY**: Tên feature (`<feature_name>`) và mô tả (`<description>`) **BẮT BUỘC phải viết bằng Tiếng Anh** (để tránh lỗi font khi sinh file markdown). Nếu người dùng dùng tiếng Việt, hãy tự dịch sang tiếng Anh.
- Mở Terminal và chạy lệnh sau để khởi tạo ngữ cảnh dự án:
```bash
python .claude/skills/kmp-tools/scripts/context_manager.py init <feature_name> "<description>"
```

## Bước 2: Architecture & Design (Phase 1)
- Đọc rule `.claude/agents/kmp-architect.md`.
- Sinh file `01_architect.json` với block `_fda` đầy đủ (`soups_introduced`, `risks`, `module_plan` với đường dẫn file cụ thể).
- Chạy lệnh sau để tạo Draft Docs:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --srs --sdd
```

## Bước 3: Implementation (Phase 2 - ACTUAL CODING)
Bạn PHẢI TRỰC TIẾP VIẾT CODE (.kt files) cho tất cả các layer được định nghĩa trong `01_architect.json`:
- Viết file Domain models, repo interfaces cho thư mục `shared/`.
- Viết file MQTT Subscriber cho thư mục `server/`.
- Viết file Backend Routes và DB cho thư mục `server/`.
- Viết file Compose UI Screen và ViewModel cho thư mục `composeApp/`.
- **Sau khi viết code**, Cập nhật lần lượt các file `02_shared.json`, `03_iot.json`, `04_ktor.json`, `05_compose.json` với `_fda.requirements_implemented`.

## Bước 4: Dual Code Review (Phase 3)
- Đọc rule `.antigravity/rules/reviewer_logic.md` và `.antigravity/rules/reviewer_style.md`.
- Kiểm tra lại toàn bộ code đã sinh ra, đặc biệt review các Checklist FDA: không dùng `GlobalScope`, không hardcode secret, validate MQTT, v.v.
- Fix lỗi nếu có.

## Bước 5: Verification & Validation (Phase 4)
- Viết test (unit test/integ test) và cập nhật Output `07_qa.json` và `08_security.json`.
- Chạy toàn bộ test suites của KMP:
```bash
./gradlew allTests
```
- Nếu test pass, sinh doc VVR, Risk, và TM:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --vvr --risk --tm
```

## Bước 6: DevOps Verification (Phase 5)
- Chạy kiểm tra chất lượng tổng thể của build:
```bash
./gradlew check
```

## Bước 7: Final FDA Documentation & Commit (Phase 6 & 7)
- Cập nhật tự động kho tài liệu chuẩn IEC-62304 / FDA:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --soup --cm --all
```
- Gom tất cả file và tiến hành commit bằng Git:
```bash
git add docs/ CHANGELOG.md .claude/context/*.json
git commit -m 'docs(<feature_name>): FDA documentation package [IEC-62304]'
git add .
git commit -m 'feat(<feature_name>): <description>'
```

**Hoàn tất Workflow!** Report lại với người dùng tình trạng của Feature.
