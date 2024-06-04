
# Hello Shoes Pvt. Ltd - Shoe Shop Management System

This project is a part of the Advanced API Development course (ITS1114) for the Graduate Diploma in Software Engineering. It aims to develop an enterprise-level application using Spring Framework, specializing in Spring Boot and MySQL, for Hello Shoes Pvt. Ltd.

## Table of Contents

1. [Introduction](#introduction)
2. [System Overview](#system-overview)
3. [Project Structure](#project-structure)
4. [System Requirements](#system-requirements)
5. [Tech Stack](#tech-stack)
6. [Installation](#installation)
7. [Usage](#usage)
8. [ER Diagram](#er-diagram)
9. [Class Diagram](#class-diagram)
10. [Client Directory](#client-directory)
11. [Server Directory](#server-directory)
12. [Contributing](#contributing)
13. [License](#license)

## Introduction

Hello Shoes Pvt. Ltd is a shoe retail company with multiple branches. This project aims to systemize various aspects of their operations including sales, inventory management, customer management, employee management, and admin panel functionalities.

## System Overview

The system consists of several microservices to handle different aspects of the business process:
- Sales Service
- Supplier Service
- Customer Service
- Employee Service
- Inventory Service
- Admin Panel Service
- User Service

## Project Structure

The project follows a modular structure with each microservice housed in its own module. The structure is as follows:
- `sales-service`
- `supplier-service`
- `customer-service`
- `employee-service`
- `inventory-service`
- `admin-panel-service`
- `user-service`

## System Requirements

Please refer to the [Course Work document](https://drive.google.com/file/d/1tnRGZzz6KGGZ_EPuyhSvLPqsGE65M04r/view?usp=sharing) for detailed system requirements and instructions.

## Tech Stack

The project utilizes the following technologies:
- Front End:
  - HTML, CSS, JavaScript, jQuery, AJAX
- Back End:
  - Spring Boot, Spring Data, Spring Web MVC, Spring Validation, Spring Security, Lombok, Model Mapper, Jackson
- Database:
  - MySQL
- Authentication:
  - JWT
- Documentation:
  - Swagger

## Installation

1. Clone the repository: `git clone https://github.com/your_username/HelloShoesPVT.git`
2. Navigate to the project directory: `cd HelloShoesPVT`
3. Build the project: `mvn clean install`

## Usage

1. Start each microservice individually using `mvn spring-boot:run` command.
2. Access the different functionalities using the provided endpoints.

## ER Diagram

You can find the ER diagram [here](https://drive.google.com/file/d/1tya1W3eI-0px4PC9Qmklt_eTIBlnQT5d/view?usp=sharing).

## Class Diagram

You can find the class diagram [here](https://drive.google.com/file/d/1oZZ2gyZpuUnSQ7YAvlJSdEqF5IwoYoTJ/view?usp=sharing).

## Client Directory

The client-side code is located in the `client` directory.

## Server Directory

The server-side code is located in the `server` directory.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your suggestions or improvements.

## License

This project is licensed under the [MIT License](LICENSE).
