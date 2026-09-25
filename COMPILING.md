# Compiling & Building System Scheduler

This document provides step-by-step instructions for setting up the environment, building, testing, and packaging the **System Scheduler** Android application.

---

## 1. Prerequisites

Before building System Scheduler, ensure your development machine has the following installed:

* **Java Development Kit (JDK):** JDK 17 or higher is required.
* **Android SDK:**
  * Target SDK: API Level 34 (Android 14)
  * Minimum SDK: API Level 26 (Android 8.0)
* **Gradle:** The project includes the Gradle Wrapper (`./gradlew`), so a standalone Gradle installation is optional.
* **Android Studio (Optional):** Recommended for IDE-based development and debugging (Android Studio Iguana or newer recommended).

---

## 2. Environment Variables

Ensure the following environment variables are properly configured on your system:

```bash
# Example for Linux / macOS
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=/path/to/android/sdk
export PATH=$PATH:$JAVA_HOME/bin:$ANDROID_HOME/platform-tools
```

On Windows (Command Prompt / PowerShell):
```cmd
set JAVA_HOME=C:\Path\To\JDK17
set ANDROID_HOME=C:\Users\<Username>\AppData\Local\Android\Sdk
```

---

## 3. Building from the Command Line

All build and test tasks can be performed using the Gradle Wrapper included in the repository root.

### Grant Execution Permission (Linux / macOS)

```bash
chmod +x gradlew
```

### Run Unit Tests

Execute unit tests across all test suites:

```bash
./gradlew test
```

### Build Debug APK

Compile the source code and generate a debug APK:

```bash
./gradlew assembleDebug
```

The output APK will be placed in:
`app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK

To build an unsigned release APK:

```bash
./gradlew assembleRelease
```

The output APK will be placed in:
`app/build/outputs/apk/release/app-release-unsigned.apk`

### Clean Build Artifacts

To remove all build files and start a fresh compilation:

```bash
./gradlew clean
```

---

## 4. Building with Android Studio

1. Open **Android Studio**.
2. Select **Open** and choose the repository root folder.
3. Allow Android Studio to complete the Gradle sync.
4. To run unit tests:
   * Right-click on `app/src/test/java` in the Project Explorer and select **Run 'Tests in ...'**.
5. To build and run on a device or emulator:
   * Select a connected Android device or Virtual Device (API 26+).
   * Click **Run 'app'** (or press `Shift + F10`).

---

## 5. Localization Support

System Scheduler supports multiple languages:

| Language | Locale Code | Resource Directory |
| -------- | ----------- | ------------------ |
| English  | Default (`en`) | `app/src/main/res/values/` |
| German   | `de` | `app/src/main/res/values-de/` |
| Italian  | `it` | `app/src/main/res/values-it/` |
| Czech    | `cs` | `app/src/main/res/values-cs/` |
| Hungarian| `hu` | `app/src/main/res/values-hu/` |

---

## 6. Continuous Integration (GitHub Actions)

The repository contains a GitHub Actions CI/CD workflow defined at `.github/workflows/build.yml`.

* **Triggers:** Automatically runs on `push` and `pull_request` to `main` or `master` branches, as well as version tags (e.g., `v1.0.0`).
* **Tasks Executed:**
  1. Sets up JDK 17 environment.
  2. Runs `./gradlew test` unit tests.
  3. Builds debug APK via `./gradlew assembleDebug`.
  4. Uploads build artifacts for version tag pushes and branch builds.
