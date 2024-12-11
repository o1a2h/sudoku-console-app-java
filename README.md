# Sudoku Console Program

## Overview
This is a console-based Sudoku game implemented in Java. The program supports two languages: English (EN) and Kazakh (KZ), which can be selected at the start and changed later through the menu. The program also features an accounting system with local storage for user login and sign-in.

## Features
- **Multi-language Support:** Choose between English and Kazakh.

- **User Authentication:**
  - **Login:** Enter username and password.
  - **Sign in:** Enter username, public username, password, and confirm password.

- **Main Menu:**
  - **Start:** Begin a new Sudoku game.
  - **Tutorial:** Brief description of rules and tips.
  - **Statistics:** View user scores.
  - **Leaderboard:** View leaderboard of users by score.
  - **Difficulty:** Change the difficulty level of the game.
  - **Language:** Switch between supported languages.
  - **Log out:** Log out from the current account.
  - **Exit:** Exit the application.

## Security
- **Data Encryption:** User data is stored locally using SHA-256 and AES encryption.

## Configuration
- **ConfigManager:** Handles `.cfg` files for configuration settings.

## User Class
- **User:** Class to manage user data, including username, public username, and password.

## Getting Started
1. **Clone the repository:**
   ```sh
   git clone <repository_url>