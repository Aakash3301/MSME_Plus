# FinPlus Architecture Guide

This document outlines the architectural patterns and folder structures used in the FinPlus project. It serves as a blueprint for bootstrapping new Kotlin Multiplatform (KMP) projects (such as MSME Plus) with the same robust, scalable, and maintainable foundation.

## 1. High-Level Architecture (Clean Architecture)
The project strictly adheres to Clean Architecture principles, ensuring a separation of concerns across three main layers: **Domain**, **Data**, and **Presentation**.

### Folder Structure
```text
sharedLogic/src/commonMain/kotlin/com/yourcompany/project/
├── core/                  # Core utilities (MVI Base, Network wrappers, DI setup)
├── data/                  # Data Layer (API, DTOs, Repositories Impl, Mock Data)
├── domain/                # Domain Layer (Pure Models, Repository Interfaces, UseCases)
└── features/              # Presentation Logic (MVI Stores specific to each feature)
```

## 2. Domain Layer
The Domain layer is the heart of the application. It contains pure business logic and knows **nothing** about network requests, serialization, or UI frameworks.

* **Models**: Data classes representing pure business objects (`FinancialHealth`, `Transaction`).
* **Repository Interfaces**: Defines the contracts for data operations (`FinancialRepository`).
* **UseCases**: Single-responsibility classes that execute specific business rules.
  * Example: `GetFinancialHealthUseCase` takes a repository and returns a `Flow<Resource<FinancialHealth>>`.

## 3. Data Layer
The Data layer is responsible for fetching and mapping data from external sources (Network, Local Database) into Domain models.

* **DTOs (Data Transfer Objects)**: Models annotated with `@Serializable` that directly mirror JSON payloads from the API (`FinancialHealthDto`).
* **Mappers**: Extension functions to convert DTOs to Domain models safely (e.g., `FinancialHealthDto.toDomain()`).
* **Network Client**: Configured Ktor `HttpClient` targeting the base API URL (e.g., `https://www.sandboxidbi.com`).

### Mock Data as Real API Calls
During development (when backend APIs are not ready), we simulate network calls flawlessly without changing the architecture:
1. We define real JSON string responses in `MockJsonData.kt`.
2. Inside the Repository Implementation (`FinancialRepositoryImpl.kt`), instead of calling the Ktor client, we parse the mock JSON strings using `kotlinx.serialization.json.Json`.
3. We add a `delay()` to simulate network latency.
4. We map the decoded DTO to a Domain model and return it.
*Because the Repository Interface remains identical, the rest of the app thinks it is talking to a real server.*

### Safe API Call Wrapper
All data layer functions are wrapped in a `safeApiCall` block. This utility function catches exceptions (like timeouts or 400s) and automatically maps responses into standardized `Resource` states:
```kotlin
sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable, val message: String? = null) : Resource<Nothing>
    object Loading : Resource<Nothing>
}
```

## 4. Presentation Layer (MVI Redux)
The Presentation layer utilizes a Unidirectional Data Flow using the **Model-View-Intent (MVI)** pattern. This ensures predictable UI states and easy debugging.

### Core MVI Components
Located in `core/redux/MviBase.kt`, the base `Store` class handles the lifecycle of states and intents:

1. **State (`UiState`)**: A data class representing the entire state of a screen (e.g., `val isLoading: Boolean`, `val data: List<Item>`).
2. **Intent (`UiIntent`)**: Actions triggered by the user or system (e.g., `LoadData`, `OnItemClicked`).
3. **Action (`UiAction`)**: Internal tasks triggered by intents (e.g., fetching data from a UseCase).
4. **Effect (`UiEffect`)**: One-off events that shouldn't be persisted in state (e.g., `ShowToast`, `NavigateToHome`).

### How a Store Works (Example)
```kotlin
class DashboardStore(
    private val getHealthUseCase: GetFinancialHealthUseCase
) : Store<DashboardState, DashboardIntent, DashboardAction, DashboardEffect>(DashboardState()) {

    // 1. User sends an Intent from the UI
    override fun handleIntent(intent: DashboardIntent): DashboardAction {
        return when (intent) {
            is DashboardIntent.LoadDashboardData -> DashboardAction.FetchData
        }
    }

    // 2. Action is executed (often calling a UseCase)
    override suspend fun executeAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.FetchData -> {
                getHealthUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> updateState { it.copy(isLoading = true) }
                        is Resource.Success -> updateState { it.copy(isLoading = false, health = resource.data) }
                        is Resource.Error -> {
                            updateState { it.copy(isLoading = false) }
                            emitEffect(DashboardEffect.ShowError(resource.message))
                        }
                    }
                }
            }
        }
    }
}
```

## 5. Dependency Injection (Koin)
We use `Koin` for lightweight DI. The setup is organized in `core/di/SharedModule.kt`:
* **Singletons**: For Repositories and Network clients (`single { FinancialRepositoryImpl() }`).
* **Factories**: For UseCases and Stores (`factory { GetFinancialHealthUseCase(get()) }`).

## 6. Summary for the Next Project
When starting "MSME Plus", follow this exact pipeline to ensure the same clean structure:
1. Define **Domain Models** first.
2. Define **Repository Interfaces** for the data you need.
3. Build **UseCases** for each individual business operation.
4. Create **MVI States & Intents** for your UI screens.
5. Create **Stores** that consume the UseCases and emit States.
6. Build your **UI (Compose)** and observe the Store's `stateFlow`.
7. Finally, implement the **Data Layer** (first with Mock Data, then swap it for Real Network Calls later).
