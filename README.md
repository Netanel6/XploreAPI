# 🧠 XploreAPI

XploreAPI is a backend service designed to power a dynamic and customizable quiz app. It provides endpoints to fetch quiz quiz, manage scoring, and interact with MongoDB for data storage. Built with Kotlin and Ktor, it offers lightweight and scalable solutions for quiz-based applications.

## 🚀 Features

- **Dynamic Question Management**: Fetch quiz categorized by difficulty and type.
- **MongoDB Integration**: Secure and efficient data storage for quiz content.
- **Ktor Framework**: Lightweight, fast, and modern server-side framework.
- **Custom Scoring Logic**: Tailored scoring system based on question difficulty.
- **Environment Configurable**: Easy setup with environment variables for flexibility.

## 🛠️ Requirements

- **Kotlin**: Version 1.9 or later
- **MongoDB**: Database for storing quiz data
- **Java**: Version 17 or later
- **Gradle**: For project build and dependency management

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/XploreAPI.git
   cd XploreAPI
   ```
2.	Set up your MongoDB connection string in the environment variable:
  ```bash
  export MONGODB_URI="your-mongodb-connection-string"
  ```
3.	Build the project:
```bash
./gradlew build
```

## 🌐 API Endpoints

GET /quiz

- **Description**: Fetch a list of quiz quiz.
- **Response**: JSON array of quiz with metadata.

POST /score

- **Description**: Submit quiz answers for scoring.
- **Payload**: User’s responses in JSON format.
- **Response**: Total score and feedback.

## 📄 Environment Variables

The following environment variables should be configured:
	•	MONGODB_URI: MongoDB connection string for the database.

Example:
```bash
export MONGODB_URI="mongodb+srv://user:password@cluster.mongodb.net/database-name"
```

## 🛠️ Built With

- **Kotlin**: Modern, concise, and type-safe programming language.
- **Ktor**: Asynchronous framework for building server-side applications.
- **MongoDB**: Flexible NoSQL database for storing quiz data.
- **Gradle**: Build automation and dependency management tool.

## 🤝 Contributing

1.	Fork the repository.
2.	Create a new branch:
```bash
git checkout -b feature-name
```
3.	Make your changes and commit them:
```bash
git commit -m "Add feature-name"
```
4.	Push to the branch:
```bash
git push origin feature-name
```
5.	Create a pull request.

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.
    
