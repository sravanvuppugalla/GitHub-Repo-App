# GitHub-Repo-App
## Overview
This Android application interacts with the GitHub API to fetch and display repositories for a specific GitHub user or organization. It is designed to provide a user-friendly interface for exploring repositories, filtering results, and viewing detailed information for each repository.


### Prerequisites
Make sure you have the following software installed:
- **Android Studio** (latest version recommended)
- **Java Development Kit (JDK)** 11 or higher
- **Git** (for cloning the repository)

# GitHub Repository Viewer

This Android application interacts with the GitHub API to fetch and display repositories for a specific GitHub user or organization. It is designed to provide a user-friendly interface for exploring repositories, filtering results, and viewing detailed information for each repository.

## Features
- **Fetch Repositories**: Retrieve and display a list of repositories for any GitHub user or organization.
- **Filter by Language**: Filter the list of repositories based on the programming language.
- **Repository Details**: View detailed information about a selected repository, including name, description, stars, forks, and more.
- **Pagination**: Load more repositories as the user scrolls, providing a seamless browsing experience.
- **Error Handling**: Gracefully handle errors such as network issues or GitHub API rate limits, providing informative feedback to the user.

## Technology Stack
- **Kotlin**: Used for building the application.
- **MVVM Architecture**: Followed to ensure separation of concerns and ease of testing.
- **Retrofit**: For making network requests to the GitHub API.
- **Coroutines**: To handle asynchronous operations efficiently.
- **RecyclerView**: For displaying lists with pagination support.
- **ViewModel and LiveData**: To manage UI-related data lifecycle-aware.
- **Navigation Component**: For handling fragment navigation and back-stack management.
- **Material Design Components**: Used for creating a modern, responsive UI.


## Setup Instructions
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the project.

## Screenshots

<img width="312" alt="Screenshot 2024-11-04 at 7 23 35 PM" src="https://github.com/user-attachments/assets/717e9623-cef6-4fd3-bbb8-3298bf83d5a5">

<img width="329" alt="Screenshot 2024-11-04 at 7 21 40 PM" src="https://github.com/user-attachments/assets/214712fe-b61e-4476-b8a4-93f1bf5f851e">

https://github.com/user-attachments/assets/337c81d8-fed0-494a-af9a-0713af54e9fb


Steps : 
1. Open the App.
2. Enter any GitHub Username.
3. Click on Submit.
4. Select the technology you want from drop down.
