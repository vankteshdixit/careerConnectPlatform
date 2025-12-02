Welcome to the Career Connect Platform! This project allow users to build professional networks, similar to LinkedIn. It is built using a microservices architecture to ensure scalability and maintainability. The platform handles user management, connections between professionals, creating posts, and sending notifications.

Features
User Authentication: Secure signup and login functionality using JWT.

Professional Connections: Users can send, accept, and reject connection requests. We use a graph database to efficiently manage first-degree connections.

Posts & Interaction: Users can create posts with images and like other people's content.

Notifications: Real-time alerts when someone connects with you or interacts with your posts.

Media Upload: Support for uploading images via Cloudinary or Google Cloud Storage.

Architecture Overview
The system is composed of several independent microservices that communicates with each other. We use Netflix Eureka for service discovery and an API Gateway to route requests.

API Gateway: The entry point that handles routing and security.

User Service: Manages user registration and authentication details stored in PostgreSQL.

Connections Service: Uses Neo4j to manage the complex graph of user relationships.

Posts Service: Handles content creation and likes, utilizing Kafka for asynchronous events.

Notification Service: Listens to Kafka topics to generate user notifications.

Uploader Service: A separate service responsible for handle file uploads.

Tech Stack
Language: Java 21

Framework: Spring Boot 3 & Spring Cloud

Databases: PostgreSQL (User, Posts, Notification), Neo4j (Connections)

Messaging: Apache Kafka

Containerization: Docker & Kubernetes

Build Tool: Maven

Getting Started
Prerequisites
Before you start, make sure you have the following installed on your machine:

Java Development Kit (JDK) 21

Docker Desktop

Maven

Installation
Clone the repository: Download the code to your local machine.

Start Infrastructure: We use Docker to spin up the necessary databases and message brokers. You won't face no issues if you use the provided Kubernetes configurations in the k8s folder.

Build the Services: You need to build each service individually. Navigate to each service directory and run:

Bash

./mvnw clean package
Run the Application: You can run the services locally or deploy them to a Kubernetes cluster. To running this project locally, ensure your Eureka Discovery Server is up first.

Configuration
The services use YAML and properties files for configuration. For example, the ConnectionsService connects to a Neo4j datbase running on port 7687. Ensure your environment variables for DB_USERNAME and DB_PASSWORD is set correctly in your deployment files.

API Endpoints
Here are some key endpoints you can test:

POST /auth/signup: Register a new user.

POST /core/post: Create a new post with an image.

GET /core/{userId}/first-degree: Get a list of your connection.

Contributions
We welcome contributions! If you finds a bug, please open an issue. Its a great tool for learning microservices, so feel free to fork the repo and experiment.
