# Repro: Instrumentation test class filtering causes runtime crashes with R8

## Problem

When building an instrumentation test with starlark `android_binary` + `instruments`,
`filter_zip_exclude` in `_process_deploy_jar` removes **all** `.class` files from
the test APK that also exist in the app's `pre_dex_jar`. At runtime, Android's
instrumentation classloader shares classes between the app and test APKs. Missing
classes in the test APK cause the classloader to either:

1. Fail with `ClassNotFoundException` — if R8 repackaged/removed the class entirely
2. Load R8-processed versions from the app APK — where methods may be finalized,
   companions stripped, or abstract flags removed.

## Project structure

```
app/
├── BUILD.bazel              # android_library + android_binary with proguard
├── AndroidManifest.xml
├── proguard.cfg
├── res/values/strings.xml
└── src/com/example/app/
    ├── Helper.java
    ├── MainActivity.java
    └── MyApp.java
test/
├── BUILD.bazel              # android_library + android_binary with instruments
├── AndroidTestManifest.xml
└── src/com/example/test/
    └── MyAppTest.java
```

## Build

```bash
bazel build //app:app //test:test_app
```

## Reproduce the crash

```bash
# Start an emulator (or connect a device)
emulator -avd <your_avd> &

# Install both APKs
adb install -r $(bazel cquery //app:app --output=files 2>/dev/null | grep 'app\.apk$')
adb install -r $(bazel cquery //test:test_app --output=files 2>/dev/null | grep 'test_app\.apk$')

# Run the instrumentation test
adb shell am instrument -w \
  -e class com.example.test.MyAppTest \
  com.example.test/androidx.test.runner.AndroidJUnitRunner

# Check crash logs
adb logcat -d -s AndroidRuntime:E | tail -30
```

### Actual output (crash)

```
INSTRUMENTATION_RESULT: shortMsg=Process crashed.
INSTRUMENTATION_CODE: 0
```

## Environment

- rules_android v0.7.1
- Bazel 7.7.1
- macOS 26.3.1
