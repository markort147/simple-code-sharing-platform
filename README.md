# Simple Code Sharing Platform

This project is a web application built with Java and Spring Framework that provides users with two primary ways to interact with code snippets:
- Web Interface
- Rest API

## Key Features
- Create and share code snippets
- View existing snippets submitted by others

## Technologies Used
- Java
- Spring Framework
- H2 (as in-memory database)
- Freemarker (for templating)

## Getting Started

### Prerequisites
- Java 21 or higher
- Gradle 8 or higher

### Clone the Repository

Clone the project repository using Git:

```bash
git clone https://github.com/markort147/simple-code-sharing-platform
```

### Launch the Application

**Windows**

Open a command prompt and navigate to the project directory.
Run the following command to build and run the application using the Gradle wrapper:

```batch
gradlew bootRun
```

**Linux**

Open a terminal and navigate to the project directory.
Run the following command to build and run the application using the Gradle wrapper:

```bash
./gradlew bootRun
```

### Access the Application

Once the application is running, you can access it using your web browser by navigating to:
```
http://localhost:8080
```
This will open the web interface of the application, where you can browse and interact with code snippets.

**Note**: The application uses Spring Boot's default embedded Tomcat server, which runs on port 8889. You can change this port by modifing _application.properties_.
  
## Usage

### Web Interface

This method uses a web browser to access the application. Here's what you can do:

- **View Code Snippets**: You can browse and view existing code snippets. There are functionalities to:
  - See the latest snippets (```/code/latest```)
  - Search for a specific snippet by its ID (```/code/{id}```)
- **Create New Snippets**: You can access a form to create and submit new code snippets (```/code/new```)

### API (Application Programming Interface)

This method interacts with the application programmatically, typically from another software program. Here's what the API allows:

- **Retrieve Code Snippets**: You can programmatically access code snippets using the same functionalities as the web interface:
  - Get the latest snippets (```/api/code/latest```).
  - Get a specific snippet by its ID (```/api/code/{id}```).
- **Create New Snippets**: You can submit new code snippets through the API (```/api/code/new```).
