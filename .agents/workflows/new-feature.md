---
description: Bắt đầu luồng phát triển chức năng mới theo chuẩn FDA SDLC
---

---
description: Bắt đầu luồng phát triển chức năng mới theo chuẩn FDA SDLC
---

// turbo-all

# New Feature Workflow (FDA-Aligned SDLC)

Quy trình này sẽ hướng dẫn agent (đóng vai trò Orchestrator) thực hiện đầu cuối một chức năng mới theo quy trình FDA-compliant.

## Bước 1: Workflow Init (Phase 0)
- Hỏi người dùng về tên feature (`<feature_name>`) và mô tả (`<description>`) nếu chưa được cung cấp.
- Mở Terminal và chạy lệnh sau để khởi tạo ngữ cảnh dự án:
```bash
python .claude/skills/kmp-tools/scripts/context_manager.py init <feature_name> "<description>"
```

## Bước 2: Architecture & Design (Phase 1)
- Đọc rule `.claude/agents/kmp-architect.md`.
- Suy nghĩ hệ thống, phân rã yêu cầu thành các `REQ-IDs` và xuất file `01_architect.json` với block `_fda` đầy đủ (`soups_introduced`, `risks`, v.v.).
- Chạy lệnh sau để tạo Draft Docs (SRS và SDD):
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --srs --sdd
```

## Bước 3: Implementation (Phase 2)
Phân rã task xuống các layer và thực hiện code (áp dụng tuần tự hoặc theo rule tương ứng trong `.claude/agents/`):
- **kmp-shared**: Code file domain, models, repo interfaces. Output `02_shared.json`.
- **kmp-iot**: Code MQTT/thiết bị logic. Output `03_iot.json`.
- **kmp-ktor-backend**: Code server routes, database logic. Output `04_ktor.json`.
- **kmp-compose**: Code giao diện người dùng. Output `05_compose.json`.
> **MANDATORY**: Bạn PHẢI điền `_fda.requirements_implemented` vào tất cả các file JSON trên.

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
