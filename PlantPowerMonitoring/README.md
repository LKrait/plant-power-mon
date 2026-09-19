# Plant Power Monitoring – Android APK

Portrait Android dashboard for monitoring a plant supplied by **Generator + Utility**, with **16 feeders**.

## Dashboard layout
1. Total Plant Load
2. Power Sources
3. Load Distribution
4. Feeders (16)
5. Load Trend
6. Energy Consumption

The current version is a visual/sample dashboard using fixed sample values. It is intentionally dependency-light and uses a custom Android View so it can be built without Android Studio.

## Build with GitHub
1. Create a new GitHub repository.
2. Upload the contents of this folder, including `.github/workflows/build-apk.yml`.
3. Open **Actions** in GitHub.
4. Run **Build Android APK** manually, or push to `main`/`master`.
5. Open the completed workflow run and download the artifact named **PlantPowerMonitoring-debug-apk**.

## Next development stage
The sample values can be replaced with live data from your power meters/PLC/network gateway. The feeder cards can also be changed to show feeder names, kW, kWh, A, V, PF, frequency and alarm status.
