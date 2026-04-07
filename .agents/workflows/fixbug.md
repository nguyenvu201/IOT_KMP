---
description: Quy trình sửa lỗi cơ bản theo chuẩn FDA SDLC
---

// turbo-all

# Fix Bug Workflow (FDA-Aligned SDLC)

Quy trình này sẽ hướng dẫn agent (đóng vai trò Orchestrator) thực hiện sửa một lỗi phần mềm (bug) theo quy trình FDA-compliant.

## Bước 1: Bug Analysis & Context Init (Phase 0)
- Hỏi người dùng về thông tin lỗi phần mềm (`<bug_name>`) và nguyên nhân hoặc mô tả lỗi (`<description>`) nếu chưa được cung cấp.
- Sử dụng các công cụ đọc log, codebase (như `gitnexus_query`, `grep_search`, `view_file` v.v...) để cô lập lỗi một cách chắc chắn.
- Mở Terminal và chạy lệnh sau để khởi tạo ngữ cảnh dự án:
```bash
python .claude/skills/kmp-tools/scripts/context_manager.py init <bug_name> "<description>"
```

## Bước 2: Impact Analysis & Design (Phase 1)
- Xác định nguyên nhân gốc rễ (Root Cause) và lập kế hoạch sửa lỗi. Nghĩ về các rủi ro (Risks) mới có thể sinh ra do đoạn mã sửa đổi (để chuẩn bị ghi vào `<layer>.json`).
- Sinh (hoặc cập nhật) Draft Docs nếu phần Design thay đổi:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --sdd
```

## Bước 3: Bug Fix Implementation (Phase 2)
Cập nhật các module tương ứng để sửa lỗi và theo dõi bằng `.json` FDA Context:
- **kmp-shared**: Code file domain, models, repo interfaces. Output `02_shared.json`.
- **kmp-iot**: Code MQTT/thiết bị logic. Output `03_iot.json`.
- **kmp-ktor-backend**: Code server routes, database logic. Output `04_ktor.json`.
- **kmp-compose**: Code giao diện người dùng. Output `05_compose.json`.
> **MANDATORY**: Điền `_fda.risks` hoặc `_fda.requirements_implemented` nếu bản sửa lỗi liên quan tới các REQ-ID cần thiết.

## Bước 4: Dual Code Review (Phase 3)
- Đọc rule `.antigravity/rules/reviewer_logic.md` và `.antigravity/rules/reviewer_style.md`.
- Rà soát đặc biệt vào phần mã vừa thay đổi để đảm bảo Bug vừa được cô lập hoàn toàn và không tạo thêm Bug mới về Non-blocking / Concurrency.

## Bước 5: Regression & Verification (Phase 4)
- Viết / cập nhật Regression Test để đảm bảo lỗi tương tự sẽ không lặp lại và cập nhật Output `07_qa.json`.
- Chạy lại các tests để xác minh:
```bash
./gradlew allTests
```
- Nếu mọi thứ ổn định, cập nhật hồ sơ FDA VVR, Risk, và TM:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --vvr --risk --tm
```

## Bước 6: DevOps Verification (Phase 5)
- Chạy kiểm tra chất lượng tổng thể của build:
```bash
./gradlew check
```

## Bước 7: Final FDA Documentation & Commit (Phase 6 & 7)
- Cập nhật tự động toàn bộ kho tài liệu chuẩn IEC-62304 / FDA:
```bash
python .claude/skills/kmp-tools/scripts/doc_generator.py --soup --cm --all
```
- Gom tất cả file và tiến hành commit bằng Git:
```bash
git add docs/ CHANGELOG.md .claude/context/*.json
git commit -m 'docs(<bug_name>): FDA documentation package update [IEC-62304]'
git add .
git commit -m 'fix(<bug_name>): <description>'
```

**Hoàn tất Workflow Fix Bug!** Báo cáo root cause và phương án đã áp dụng để sửa thành công cho người dùng.
