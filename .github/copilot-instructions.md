# Android Development Instructions

## Architecture

Always use MVVM (Model-View-ViewModel) architecture for structuring all features in this Android project. This is a hard rule.

- **Models**: Handle data operations, API calls, and database interactions.
  - Use retrofit2 for API handling.
- **ViewModels**: Contain business logic, manage UI state, and communicate with Models.
  - declare contract for every ViewModel, which interface defines the methods that the ViewModel must implement.
  - expose uiState and event to View
   
- **Views**: Pure UI components that observe ViewModel state and trigger user actions.

## Language

Use Kotlin exclusively for all new code development. This is a hard rule.

- Avoid introducing new Java code unless absolutely necessary for legacy compatibility.

## UI Framework

Use Jetpack Compose for building all user interfaces. This is a hard rule.

- Do not create new XML layouts; migrate existing ones to Compose when possible.

## Asynchronous Programming

Use Kotlin Coroutines for all asynchronous operations. This is a hard rule.

- Prefer `Flow` for reactive data streams and state management.
- Avoid traditional callbacks or RxJava unless interfacing with legacy code.

## Testing

Follow Test-Driven Development (TDD) practices. This is a hard rule.

- Write unit tests for ViewModels, repositories, and utility classes before implementing the code.
- Use JUnit and Mockito for unit testing.
- Write integration tests for data layers.
- Use Compose Test framework for UI component testing.

## Code Quality

- Follow official Kotlin coding conventions.
- Use meaningful, descriptive names for variables, functions, and classes.
- Keep functions small and focused on a single responsibility.
- Use Hilt dependency injection for managing dependencies.
- Implement proper error handling and logging.
