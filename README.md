# MetroTube

A minimal Android (API 29+ / Android 10+) YouTube browsing client styled after
the old Windows Phone official YouTube client: black background, thin
"youtube" wordmark, pivot-style tabs ("what to watch" / "browse"), full-width
thumbnails, and the round-icon bottom app bar.

It talks directly to the **YouTube Data API v3** using a key you provide —
there's no bundled key, no scraping, and no attempt to spoof an official
client. On first launch it asks for a key; you can change it later from the
overflow (•••) button → Settings.

## Why you have to build this yourself

This project was written and assembled in a sandboxed environment that has
no Android SDK, no Gradle, and no network access to Google's Maven
repository (`dl.google.com`), which AndroidX/Material/etc. are hosted on.
So the source is complete, but it was never actually compiled here — you'll
need to build it locally.

## Building the APK

**Easiest: Android Studio**
1. Open Android Studio → `Open` → select this `MetroTube` folder.
2. Let it sync Gradle (first sync downloads the SDK platform/build tools if
   you don't have them yet).
3. `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`.
4. The APK lands in `app/build/outputs/apk/debug/app-debug.apk`.
5. Install it: drag onto an emulator, or
   `adb install app/build/outputs/apk/debug/app-debug.apk` with a device
   connected over USB debugging.

**From a terminal, if you already have the Android SDK + `ANDROID_HOME` set:**
```bash
cd MetroTube
./gradlew assembleDebug
# (if the wrapper jar is missing, run `gradle wrapper` once with a local
# Gradle install to regenerate gradle/wrapper/gradle-wrapper.jar)
adb install app/build/outputs/apk/debug/app-debug.apk
```

If you only have a Debian terminal and nothing else, the practical path is:
install `sdkmanager` (comes with `cmdline-tools`) and accept the licenses,
`sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"`,
export `ANDROID_HOME`, then run the Gradle command above. This needs a real
network path to `dl.google.com` and `repo.maven.apache.org`, which this
sandbox's proxy doesn't allow — so do this step on your own machine.

## Getting an API key

Settings → api key → paste a key with the **YouTube Data API v3** enabled,
created in the [Google Cloud Console](https://console.cloud.google.com/apis/library/youtube.googleapis.com).
Free tier is normally enough for casual browsing; heavy use can hit daily
quota limits.

## What's implemented vs. stubbed

- ✅ First-run setup screen, settings screen, key storage (plain
  `SharedPreferences` — swap in `EncryptedSharedPreferences` if you want it
  hardened).
- ✅ "What to watch" tab pulls `videos?chart=mostPopular`.
- ✅ "Browse" tab currently runs a fixed placeholder search query
  (`technology`) — wire the search button to an actual query dialog/bar to
  make it useful.
- ✅ Tapping a video hands off to the YouTube app/browser via an intent
  rather than embedding a player, to keep this scoped to the browsing UI.
- ⛔ No offline caching, no account/OAuth login, no comments/likes — those
  need OAuth scopes beyond a simple API key and are left out.
