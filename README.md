# MSME Plus (FinPlus)

**MSME Plus** is an AI-powered MVP built for the **IDBI Bank Innovate Hackathon**. 

The core objective of this project is to provide an AI-driven **MSME Financial Health Card** that helps IDBI Bank evaluate New-to-Credit (NTC) and New-to-Bank (NTB) MSMEs using **alternate financial data** rather than relying solely on traditional financial statements.

This application demonstrates how AI can improve financial inclusion and accelerate MSME credit assessment.

## Key Functionalities

- **AI-Powered Health Score**: Generates a comprehensive health score based on alternate data.
- **Alternate Data Integration**: Connects with modern Indian financial infrastructure:
  - **GST**: Goods and Services Tax data for business revenue tracking.
  - **UPI**: Real-time transaction data.
  - **Account Aggregator (AA)**: Secure and consented financial data sharing.
  - **EPFO**: Employee provident fund data for workforce strength validation.
  - **Bank Statements**: Direct integration for cash flow analysis.
- **AI Advisor / Insights**: Leverages Gemini API to provide intelligent insights, financial health analysis, and tailored loan recommendations to MSMEs.
- **Dashboard & Analytics**: Intuitive visual representation of financial health.
- **Mock & Live Data**: Capable of seamless switching between mock JSON data (for MVP demo) and real APIs without altering core architecture.

## Tech Stack

The project leverages **Kotlin Multiplatform (KMP)** to share business logic across iOS and Android while maintaining native UI experiences.

### Shared & Core
- **Language**: Kotlin
- **Framework**: Kotlin Multiplatform (KMP)
- **Architecture**: Clean Architecture (Domain, Data, Presentation) + **MVI (Model-View-Intent)**
- **Networking**: Ktor Client
- **Dependency Injection**: Koin
- **Serialization**: kotlinx.serialization
- **Concurrency**: Kotlin Coroutines & Flow
- **AI Engine**: Google Gemini API

### Android
- **UI Framework**: Jetpack Compose (Material 3)
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose

### iOS
- **UI Framework**: SwiftUI

## Project Structure

- [`shared/`](./shared) - Contains all the shared KMP code (Domain, Data, Presentation logic, Ktor networking, Koin DI).
- [`androidApp/`](./androidApp) - Android native application using Jetpack Compose that observes states from the shared MVI stores.
- [`iosApp/`](./iosApp) - iOS native application using SwiftUI.

## Architecture Highlights

We follow a strict **Clean Architecture** pattern to ensure a robust, scalable, and testable foundation:
1. **Domain Layer**: Pure business logic, UseCases, and Models.
2. **Data Layer**: Repositories, DTOs, API calls, and local data persistence. Includes safe API wrappers and mock capabilities.
3. **Presentation Layer**: UI Stores using the **MVI Redux** pattern for predictable and unidirectional state management.

For detailed architecture guidelines, please refer to [finPlus_architecture.md](./finPlus_architecture.md).

## Getting Started

### Prerequisites
- **Android Studio** or **IntelliJ IDEA** (with KMP plugins).
- **Xcode** (for iOS development and execution).
- A valid **Gemini API Key**.

### Configuration
1. Create a `local.properties` file in the project's root directory if it doesn't exist.
2. Add your Gemini API key:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
   *(This key is securely injected into the shared code via `BuildConfig` during the build process).*

### Running the Apps
- **Android**: Select the `androidApp` run configuration in Android Studio and hit Run, or use the terminal:
  ```bash
  ./gradlew :androidApp:assembleDebug
  ```
- **iOS**: Open the `iosApp/iosApp.xcodeproj` (or `.xcworkspace`) in Xcode and run it on a simulator or device.

---
*Built for the IDBI Bank Innovate Hackathon*