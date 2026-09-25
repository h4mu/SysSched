# Minimalist System Scheduler for Android

A minimalist Android application for scheduling system operations such as **Wi-Fi, Bluetooth, mobile sync, power-saving mode, location/GPS, NFC, and device power actions**.

Designed for **Android 8.0 (API 26) and newer**, with a focus on simplicity, predictable scheduling, and minimal background overhead.

> **Important:** Android deliberately restricts third-party applications from directly changing several system settings. The application therefore distinguishes between operations it can perform directly, operations requiring special privileges, and operations that can only open the corresponding system settings.

## Features

Schedule system operations for a specific time or on a recurring basis.

### Connectivity

* 📶 Wi-Fi — ON / OFF
* 🟦 Bluetooth — ON / OFF
* 📡 Mobile data — ON / OFF where permitted
* 🔄 Automatic synchronization — ON / OFF
* 📍 Location / GPS — ON / OFF where permitted
* 📳 NFC — ON / OFF where permitted

### Power

* 🔋 Battery Saver / Power Saving Mode — ON / OFF
* 🔌 Device shutdown
* 🔄 Device reboot
* ⚡ Device power-on where supported by the hardware/device configuration

### Scheduling

* One-time schedules
* Daily schedules
* Weekly schedules
* Enable/disable individual schedules
* Edit existing schedules
* Delete schedules
* Restore schedules after reboot

## Example

A typical schedule might look like:

| Time  | Operation    | Action |
| ----- | ------------ | ------ |
| 07:00 | Wi-Fi        | ON     |
| 07:00 | Bluetooth    | ON     |
| 07:00 | Location     | ON     |
| 08:00 | Sync         | ON     |
| 22:00 | Sync         | OFF    |
| 22:30 | Bluetooth    | OFF    |
| 23:00 | Wi-Fi        | OFF    |
| 23:00 | Location     | OFF    |
| 23:00 | NFC          | OFF    |
| 23:30 | Power Saving | ON     |

## Supported Operations

The application represents operations using a common model:

```text
Operation
├── Wi-Fi
├── Bluetooth
├── Sync
├── Location
├── NFC
├── Power Saving
├── Mobile Data
├── Reboot
└── Shutdown
```

Each operation has an action:

```text
ON
OFF
```

Additional actions can be introduced later without changing the scheduling system.

## Android Restrictions

Not every operation can be controlled by a normal Android application.

The scheduler must **never claim that an operation succeeded when Android rejected it**.

### Wi-Fi

Directly changing Wi-Fi state is restricted on modern Android versions.

The application should:

1. Use an API when direct control is permitted.
2. Otherwise open the appropriate system settings or panel.
3. Report that direct automation is unavailable.

### Bluetooth

Bluetooth state management has become increasingly restricted across Android releases.

The application should use the appropriate version-specific Bluetooth APIs and permissions.

Where Android requires user interaction, the scheduled action should open the appropriate system UI rather than attempting to bypass the restriction.

### Synchronization

The application can provide scheduling around synchronization-related settings where Android permits it.

The implementation should distinguish between:

* Global automatic synchronization
* Individual account synchronization
* Triggering an immediate sync

These are different operations and should not be represented as the same action.

### Location / GPS

Modern Android versions restrict applications from silently changing the global Location setting.

The application can detect the current location state and, where necessary, direct the user to Android's location controls.

The scheduler should therefore support a state such as:

```text
Location → ON
Location → OFF
```

while determining at runtime whether the requested action is executable.

### NFC

NFC state control is restricted on modern Android versions.

Where direct control is unavailable, the scheduler should open the appropriate NFC/system settings rather than attempting to modify the setting through unsupported APIs.

### Power Saving Mode

Battery Saver / Power Saving Mode is controlled by the Android system and is subject to API and privilege restrictions.

The app should distinguish between:

```text
Power Saving → ON
Power Saving → OFF
```

and:

```text
Open Battery Saver settings
```

The latter is a fallback when direct control is unavailable.

### Shutdown / Reboot

Ordinary third-party applications cannot generally shut down or reboot a consumer Android device.

These operations may be available on:

* Rooted devices
* System/privileged applications
* Managed enterprise devices
* Specialized OEM configurations
* Devices where the application has appropriate device-management privileges

The application should detect whether the operation is available before scheduling it.

### Power-On

Powering on a completely powered-off Android phone is fundamentally different from waking a sleeping device.

A normal application cannot generally turn a completely powered-off phone on at a scheduled time.

Hardware, firmware, bootloader, or OEM support is required.

## Operation Capability

Each operation should expose its current capability:

```text
DIRECT
USER_ACTION_REQUIRED
PRIVILEGED
UNSUPPORTED
```

For example:

```text
Wi-Fi
✓ Can schedule

NFC
⚠ Requires user action

Shutdown
🔒 Requires elevated privileges

Power-on
✕ Unsupported on this device
```

This prevents the UI from promising functionality that the Android platform does not provide.

## Scheduling Architecture

```text
                    ┌─────────────────┐
                    │       UI        │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Schedule Store  │
                    │      Room       │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │    Scheduler    │
                    │  AlarmManager   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ BroadcastReceiver│
                    └────────┬────────┘
                             │
                             ▼
                  ┌──────────────────────┐
                  │ Operation Dispatcher │
                  └──────────┬───────────┘
                             │
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
       Network            System             Power
       Manager            Settings           Manager
```

## Recommended Android Stack

* **Kotlin**
* Android SDK
* Jetpack
* Room
* `AlarmManager`
* `BroadcastReceiver`
* Material 3

`AlarmManager` should be preferred for operations that are intended to happen at a particular clock time.

`WorkManager` is more appropriate for deferrable background work and should not be treated as an exact timer.

## Schedule Model

A schedule can be represented conceptually as:

```text
Schedule
---------
id
operation
action
triggerAt
repeatType
enabled
createdAt
lastExecution
```

Example:

```text
id:          42
operation:   WIFI
action:      OFF
triggerAt:   23:00
repeatType:  DAILY
enabled:     true
```

## User Interface

The main screen should remain deliberately minimal.

```text
SYSTEM SCHEDULER

TODAY

07:00   Wi-Fi          ON
07:00   Bluetooth      ON
07:00   Location       ON
08:00   Sync           ON
22:00   Sync           OFF
23:00   Wi-Fi          OFF
23:00   Location       OFF
23:30   Power Saving   ON

                              +
```

Adding a schedule:

```text
NEW SCHEDULE

Operation
[ Wi-Fi             ▼ ]

Action
[ ON                ▼ ]

Time
[ 23:00               ]

Repeat
[ Every day         ▼ ]

                    SAVE
```

If an operation cannot be performed automatically:

```text
NFC

⚠ Direct control is unavailable
   on this Android version.

[ OPEN NFC SETTINGS ]
```

## Persistence & Reboot

Schedules must survive:

* Application restarts
* Device restarts
* Process termination

After receiving `BOOT_COMPLETED`, the application should restore enabled schedules and register their alarms again.

The implementation should also account for:

* Doze mode
* Battery optimization
* Time-zone changes
* Manual clock changes
* Missed alarms
* Android background restrictions

## Permissions

Only request permissions required by the actual implementation.

Potential permissions vary considerably by Android version and operation. Examples include:

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

<!-- Bluetooth permissions depend on Android version -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

<!-- Required only if the app actually performs location-related functions -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

> A permission does not automatically grant permission to change a protected global system setting. Android version, target SDK, device manufacturer, and application privileges all matter.

## Design Principles

The app should remain intentionally small:

* No account
* No cloud backend
* No unnecessary network access
* No advertisements
* No analytics by default
* Local schedule storage
* Minimal permissions
* Minimal UI
* No unnecessary services running continuously
* Clear error reporting
* Android-native behavior

The scheduler should focus on **when an operation happens**, rather than becoming a full automation framework.

## Compatibility

**Minimum SDK:** Android 8.0 / API 26

**Target SDK:** Current stable Android SDK

Because Android system-setting APIs have changed substantially between Android 8 and current releases, every operation should be implemented behind a version-aware abstraction.

For example:

```kotlin
interface SystemOperation {
    fun isSupported(): Boolean
    fun requiresUserAction(): Boolean
    fun execute(action: Action): Result
}
```

Implementations can then be isolated:

```text
SystemOperation
├── WifiOperation
├── BluetoothOperation
├── SyncOperation
├── LocationOperation
├── NfcOperation
├── PowerSavingOperation
├── RebootOperation
└── ShutdownOperation
```

This makes it possible to handle different Android releases and manufacturer-specific behavior without complicating the scheduler itself.

## Failure Handling

Every scheduled operation should produce a result:

```text
SUCCESS
USER_ACTION_REQUIRED
PERMISSION_DENIED
NOT_SUPPORTED
FAILED
```

For example:

```text
23:00  NFC OFF
       ⚠ User action required
```

rather than:

```text
23:00  NFC OFF
       ✓ Done
```

when the operation was actually rejected by Android.

## Privacy

All schedules can remain local to the device.

The app should not require:

* User accounts
* Cloud synchronization
* Location collection
* Usage tracking
* Remote servers

unless a future feature explicitly requires them.

## Project Status

This project is a minimalist scheduled system-operation controller for Android 8.0+.

The scheduling infrastructure is straightforward to implement, but **actual control of protected system settings depends on Android version, OEM behavior, permissions, and device privileges**.

The application should always prefer a truthful capability indicator over pretending that an unsupported system operation is possible.
