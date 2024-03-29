# Widget Service
# Overview
Widget Service is a RESTful API developed with Spring Boot, designed to manage widgets. It provides operations for creating, retrieving, updating, and deleting widgets.

# Features
Create a Widget: Add new widgets with unique names.
List Widgets: Retrieve a list of all available widgets.
Get Widget Details: Fetch details of a specific widget by name.
Update Widget: Modify the details of an existing widget.
Delete Widget: Remove a widget from the system.

# Prerequisites
Before you begin, ensure you have met the following requirements:
Java JDK 17 Java can be acquired using [SDKMAN!](https://sdkman.io/)
Gradle 7.x (for building the project) [Running your Application with Gradle](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#running-your-application) for more information.
Setting Up
To set up the Widget Service, follow these steps:
Clone the repository:

git clone https://github.com/yourusername/widget-service.git
Navigate to the project directory:

cd widget-service
Build the project:

./gradlew build
Run the application:

./gradlew bootRun
The service will start running on http://localhost:8080.

# Using the API
# Create a Widget
URL: /v1/widgets
Method: POST
Body:
{
  "name": "Sample Widget",
  "description": "This is a sample widget.",
  "price": 19.99
}
# Get All Widgets
URL: /v1/widgets
Method: GET
Get Widget by Name
URL: /v1/widgets/{name}
Method: GET
# Update a Widget
URL: /v1/widgets/{name}
Method: PUT
Body:
{
  "description": "Updated description.",
  "price": 29.99
}
# Delete a Widget
URL: /v1/widgets/{name}
Method: DELETE
# Documentation
For detailed API documentation, visit http://localhost:8080/swagger-ui.html after starting the application.

# Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.

# License
Distributed under the MIT License. See LICENSE for more information.

# Contact
Jose de Jesus Ascencio Martinez pepejoe99@hotmail.com

Project Link: https://github.com/AscencioPEPE/WidgetBackend
