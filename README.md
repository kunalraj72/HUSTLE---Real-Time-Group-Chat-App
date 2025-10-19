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

## 🛠️ Technology Stack

- **Backend**: Java 8, Spring Boot 2.7.18
- **Microservices**: Spring Cloud, Netflix Eureka
#- **API Gateway**: Spring Cloud Gateway
- **Real-time Communication**: WebSocket (Classical Implementation)
- **Message Broker**: Apache Kafka
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: JWT, Spring Security
- **Build Tool**: Maven
- **Service Discovery**: Netflix Eureka Server

## 📋 Prerequisites

- Java 8
- Maven 3.6+
- PostgreSQL 12+
- Apache Kafka 2.8+
- Zookeeper (for Kafka)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/real-time-chat-app.git
cd real-time-chat-app
