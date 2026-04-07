# KMP Module Dependency Analysis

**Generated**: 2026-04-07T20:31:44.216232

## Modules Found (3)

- **composeApp**: 14 Kotlin files, 0 cross-module imports
- **shared**: 24 Kotlin files, 0 cross-module imports
- **server**: 12 Kotlin files, 0 cross-module imports

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