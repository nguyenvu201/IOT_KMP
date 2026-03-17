import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        compilerOptions {
            freeCompilerArgs.add("-Xklib-duplicated-unique-name-strategy=allow-all-with-warning")
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.json)
            api(libs.ktor.clientCore)
            api(libs.ktor.clientWebsockets)
            // put your Multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "caonguyen.vu.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

buildkonfig {
    packageName = "caonguyen.vu.shared.buildconfig"
    // Do not use exposeObjectWithName here to avoid @JsExport crash on WasmJS
    objectName = "BuildKonfigInternal"
    defaultConfigs {
        // Here we define the global constants
        // To run easily on Desktop/Simulator, use "127.0.0.1". 
        // For Android Emulator, use "10.0.2.2".
        // For physical device, use Wi-Fi IP (e.g., "192.168.1.104")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "MQTT_BROKER_HOST", "127.0.0.1")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "WEBSOCKET_HOST", "192.168.1.104")
        
        // MQTT Topic defaults
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "MQTT_CMD_TOPIC", "iot/esp8266/node-1/cmd")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "MQTT_STATUS_TOPIC_FILTER", "iot/esp8266/+/status")
    }
}
