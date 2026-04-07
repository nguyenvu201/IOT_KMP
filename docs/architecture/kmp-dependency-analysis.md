# KMP Module Dependency Analysis

**Generated**: 2026-04-07T21:28:51.011113

## Modules Found (3)

- **composeApp**: 18 Kotlin files, 0 cross-module imports
- **shared**: 25 Kotlin files, 0 cross-module imports
- **server**: 18 Kotlin files, 0 cross-module imports

## Module Dependency Diagram

```mermaid
graph TD
    composeApp["📱 composeApp<br/>Compose Multiplatform UI"]
    shared["🔷 shared<br/>KMP Shared Logic"]
    server["🖥️ server<br/>Ktor Backend"]
    composeApp --> shared
    server --> shared
```

## Circular Dependencies

✅ No circular dependencies detected