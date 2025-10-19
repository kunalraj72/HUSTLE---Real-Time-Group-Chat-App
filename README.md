# Real-Time Chat Application

A scalable, microservices-based real-time chat application built with Spring Boot, WebSocket, Kafka, and PostgreSQL.

## 🚀 Features

- **Real-time Messaging**: Instant message delivery using WebSocket connections
- **Microservices Architecture**: Independently deployable services with Spring Cloud
- **JWT Authentication**: Secure user authentication and authorization
- **Multiple Chat Rooms**: Support for different conversation spaces
- **Message Persistence**: All messages stored in PostgreSQL database
- **Scalable Design**: Load balancing across multiple chat service instances
- **Kafka Integration**: Reliable message pub-sub for distributed systems

## 🏗️ Architecture

![HUSTLE-Architecture](./HUSTLE-Architecture.png.png)

## 🛠️ Technology Stack

- **Backend**: Java 8, Spring Boot 2.7.18
- **Microservices**: Spring Cloud, Netflix Eureka
- **Real-time Communication**: WebSocket (Classical Implementation)
- **Message Broker**: Apache Kafka
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: JWT, Spring Security
- **Build Tool**: Maven
- **Service Discovery**: Netflix Eureka Server
