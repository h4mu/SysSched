# Minimalist System Scheduler for Android

A minimalist Android application written in **Java** for scheduling system operations such as **Wi-Fi, Bluetooth, mobile sync, power-saving mode, location/GPS, NFC, and device power actions**.

Designed for **Android 8.0 (API 26) and newer**, with a focus on simplicity, predictable scheduling, zero third-party cloud dependencies, and minimal background overhead.

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
* Restore schedules after reboot (`BOOT_COMPLETED`)

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

## Android Restrictions

Not every operation can be controlled directly by a standard Android application. The scheduler **never claims that an operation succeeded when Android rejected it**.

### Operation Capability

Each operation exposes its current capability:

```text
DIRECT
USER_ACTION_REQUIRED
PRIVILEGED
UNSUPPORTED
```

* **DIRECT**: Managed directly via standard Android APIs (e.g., Sync, Wi-Fi on older APIs).
* **USER_ACTION_REQUIRED**: Launches relevant Android system settings screen when direct toggling is restricted on modern Android releases.
* **PRIVILEGED**: Requires system app privileges or root (e.g., Reboot, Shutdown).
* **UNSUPPORTED**: Hardware or API not available on device.

## Scheduling Architecture

```text
                    ┌─────────────────┐
                    │       UI        │
                    │  (Activity/XML) │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Schedule Store  │
                    │ SharedPreferences│
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

## Technology Stack

* **Java** (Java 17 / Android API 26–34)
* Android SDK / AndroidX / Material 3
* `SharedPreferences` (Lightweight JSON Persistence, zero external DB dependencies)
* `AlarmManager` & `BroadcastReceiver`

## Building & Testing

To build the project:

```bash
gradle build
```

To run unit tests:

```bash
gradle test
```

## Persistence & Boot Recovery

Schedules survive application restarts and device reboots. Upon receiving `BOOT_COMPLETED`, `BootReceiver` restores all enabled schedules and registers their exact alarms with `AlarmManager`.
