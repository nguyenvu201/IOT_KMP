docker compose up -d

# Tự động tìm và tắt tiến trình đang chiếm cổng 8085 (nếu có)
echo "Đang dọn dẹp cổng 8085..."
lsof -t -i :8085 | xargs kill -9 2>/dev/null || true

# Chạy lại server Ktor
./gradlew :server:run
