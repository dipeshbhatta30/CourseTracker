# CourseTracker
Android application that helps students track their academic progress toward graduation

##  Architecture Overview

- **UI Layer (View)**
  - Activities & Fragments
  - Handles user interaction and displays data

- **ViewModel Layer**
  - Manages UI-related data
  - Survives configuration changes (e.g., screen rotation)

- **Repository Layer**
  - Acts as a bridge between ViewModel and data sources
  - Ensures a single source of truth

- **Data Layer (Room Database)**
  - Uses SQLite via Room Persistence Library
  - Stores Courses and Assignments data

## 🔄 Data Flow

