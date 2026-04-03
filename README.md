# AmarDokan - E-Commerce Application

<p align="center">
<img src="src/main/resources/static/img/AmarDokan.png" width="400" height="200">
</p>

## Project Description

AmarDokan is a comprehensive e-commerce web application built as a group project for Software Engineering and Project Management course. The application provides a platform for users to browse products, manage shopping carts, place orders, and for administrators to manage products, categories, and user accounts. The system includes user authentication, product catalog management, order processing, and email notifications.

### Authors : A.K.M Samioul Islam(2107051) & Pritom Banik(2107052)

## Technology Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.5.11** - Framework for building the application
- **Spring Data JPA** - Data access layer
- **Spring Security** - Authentication and authorization
- **Spring Mail** - Email functionality
- **PostgreSQL** - Primary database
- **H2 Database** - Test database

### Frontend
- **Thymeleaf** - Server-side templating engine
- **HTML5/CSS3/JavaScript** - Frontend technologies
- **Bootstrap** - CSS framework (via custom styles)

### DevOps & Tools
- **Maven** - Build automation and dependency management
- **Docker & Docker Compose** - Containerization
- **GitHub Actions** - CI/CD pipeline
- **Lombok** - Code generation library

##  Architecture Overview

The application follows a **layered, three-tier architecture** to maintain clean separation of concerns, improve maintainability, and enable comprehensive testing.

### Core Layers

| Layer | Responsibility |
|-------|----------------|
| **Controller** | HTTP request handling, input validation via Spring MVC, route management for Admin/User roles, and view/response rendering |
| **Service** | Business logic implementation including user management, product catalog operations, cart management, order processing, and email notifications |
| **Repository** | Data persistence layer using Spring Data JPA for CRUD operations and custom queries |
| **Model** | Domain entities (User, Product, Category, Cart, ProductOrder, OrderAddress) representing persistent data |
| **DTO** | Data Transfer Objects for request validation and response formatting between layers and frontend |
| **Config** | Security configuration, authentication handlers, and application-wide beans |

### Request Flow

```
User Browser → Thymeleaf Template → Controller → Service Layer → Repository → Database (PostgreSQL)
     ↓
 Spring Security (RBAC) → Authentication/Authorization checks
     ↓
 File Upload Handler → Local File System (images, documents)
```

### Core Components

| Component | Location | Purpose |
|-----------|----------|---------|
| **Controllers** | `controller/` | HomeController, AdminController, UserController handle HTTP requests and route management |
| **Services** | `service/` | UserService, ProductService, CategoryService, CartService, OrderService provide business logic |
| **Repositories** | `repository/` | Spring Data JPA interfaces for database operations |
| **Models** | `models/` | JPA entities (User, Product, Category, Cart, ProductOrder, OrderAddress) |
| **DTOs** | `dto/` | Request and response data transfer objects |
| **Security Config** | `config/` | SecurityConfig, UserDetailsServiceImpl, AuthSucessHandlerImpl, AuthFailureHandlerImpl |
| **Utilities** | `util/` | CommonUtil, AppConstant, OrderStatus enums and helper functions |

###  Design Patterns Used

| Pattern | Where Used | Purpose |
|---------|-----------|---------|
| **Service Layer** | UserService, ProductService, OrderService, CartService, CategoryService | Encapsulates business logic and separates it from controllers |
| **Repository Pattern** | UserRepository, ProductRepository, CategoryRepository, CartRepository, ProductOrderRepository | Abstracts database access and enables data persistence without coupling to Spring Data directly |
| **Data Transfer Object (DTO)** | `dto/request/` and `dto/response/` folders | Transfers structured data between layers while protecting entity internals |
| **MVC** | Controllers + Thymeleaf templates + JPA models | Clean separation between presentation, business logic, and data layers |
| **Singleton** | All Spring @Bean components and @Service classes | Ensures single instance of services, repositories, and configurations |
| **Strategy** | AuthSucessHandlerImpl, AuthFailureHandlerImpl | Different authentication strategies for success/failure scenarios |
| **Factory** | Spring's @Bean methods in SecurityConfig | Centralized creation of complex objects like PasswordEncoder and AuthenticationProvider |

###  Database Design

#### Main Tables

| Table | Purpose |
|-------|---------|
| **users** | Stores user profile (name, email, mobile, address), authentication credentials (password, resetToken), account state (enabled, locked) |
| **product** | Stores product metadata (title, description, price, stock, discount) and references category |
| **category** | Stores category information (name, image, status) for product classification |
| **cart** | Stores shopping cart items for users before checkout |
| **product_order** | Stores individual product orders with quantity, price, payment type, status, and delivery address |
| **order_address** | Stores delivery addresses for orders (street, city, state, pincode, phone) |

#### Entity Relationships

```
User (1) ──→ (Many) Cart
User (1) ──→ (Many) ProductOrder
User (1) ──→ (Many) Product (seller relationship implicitly handled)

Category (1) ──→ (Many) Product
Product (1) ──→ (Many) ProductOrder

OrderAddress (1) ──→ (Many) ProductOrder
```

**Relationship Description:**
- User can have multiple Cart items and ProductOrders
- Category has multiple Products
- Each Product belongs to one Category
- Each ProductOrder is linked to one User, one Product, and one OrderAddress
- OrderAddress stores delivery information for orders

**ER diagram :**
<p align="center">
<img src="src\main\resources\static\ERdiagram.png" width="500" height="300">
</p>



###  Authentication & Security

| Feature | Implementation |
|---------|-------------------|
| **Spring Security** | Provides route protection, role-based access control (RBAC) for /admin/** (ROLE_ADMIN) and /user/** (ROLE_USER) paths |
| **Password Encoding** | BCryptPasswordEncoder hashes passwords with salt to provide strong cryptographic security |
| **Role-Based Access Control (RBAC)** | Strict route mapping: `/admin/**` requires ROLE_ADMIN, `/user/**` requires ROLE_USER, public routes permit all |
| **Custom Success Handler** | AuthSucessHandlerImpl automatically redirects authenticated users to roles dashboards (admin or user) |
| **Custom Failure Handler** | AuthFailureHandlerImpl tracks failed login attempts, locks accounts after threshold, and provides feedback |
| **Account Locking** | After repeated failed login attempts, accounts are locked for security; tracks `failedAttempt` and `lockTime` |
| **Password Reset** | Reset tokens are generated and stored in `resetToken` field for secure password recovery |
| **Lazy Initialization** | Spring Lazy loading prevents circular dependency issues in security chain initialization |



## API Endpoints

### Products API (`/api/products`)
- `GET /api/products` - Retrieve all products
- `GET /api/products/{id}` - Retrieve product by ID
- `POST /api/products` - Create new product (with image upload)
- `PUT /api/products/{id}` - Update existing product
- `DELETE /api/products/{id}` - Delete product

### Categories API (`/api/categories`)
- `GET /api/categories` - Retrieve all categories
- `GET /api/categories/{id}` - Retrieve category by ID
- `POST /api/categories` - Create new category (with image upload)
- `PUT /api/categories/{id}` - Update existing category
- `DELETE /api/categories/{id}` - Delete category

### Users API (`/api/users`)
- `GET /api/users` - Retrieve all users (optional role filter)
- `GET /api/users/{email}` - Retrieve user by email
- `POST /api/users` - Create new user
- `PUT /api/users/{id}/status` - Update user account status

## Run Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL (if running locally without Docker)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd amarDokan
   ```

2. **Configure Environment Variables**
   
   Create a `.env` file in the root directory:
   ```env
   DATABASE_URL=jdbc:postgresql://localhost:5432/amardokan
   DATABASE_USER=your_db_user
   DATABASE_PASSWORD=your_db_password
   MAIL_USERNAME=your_gmail@gmail.com
   MAIL_PASSWORD=your_gmail_app_password
   IMAGE_UPLOAD_PATH=uploads/img
   PORT=8081
   ```

3. **Using Docker Compose (Recommended)**
   ```bash
   # Start PostgreSQL database
   docker-compose up postgres -d
   
   # Run the application
   ./mvnw spring-boot:run
   ```

4. **Using Docker Compose (Full Stack)**
   ```bash
   docker-compose up --build
   ```
   The application will be available at `http://localhost:8081`

5. **Manual Setup**
   ```bash
   # Install dependencies
   ./mvnw clean install
   
   # Run the application
   ./mvnw spring-boot:run
   ```

### Default Admin Credentials
- **Username**: admin
- **Password**: admin123

## CI/CD Pipeline

The project uses GitHub Actions for continuous integration and deployment. The CI/CD pipeline is configured in `.github/workflows/maven.yml` and includes:

### Triggers
- Push to `main`, `develop`, or any `feature/**` branch
- Pull requests targeting `main` or `develop` branches

### Pipeline Steps
1. **Checkout Code**: Retrieves the latest code from the repository
2. **Setup JDK 21**: Configures Java 21 with Eclipse Temurin distribution
3. **Cache Maven Dependencies**: Speeds up builds by caching dependencies
4. **Make Maven Wrapper Executable**: Ensures the Maven wrapper script is executable
5. **Build and Test**: Runs `mvn test` to compile and execute unit tests
6. **Upload Test Reports**: Archives JUnit test results as artifacts

### Benefits
- **Automated Testing**: Ensures code quality with every push
- **Fast Feedback**: Quick identification of build failures
- **Consistent Environment**: Standardized build environment across all runs
- **Artifact Storage**: Test reports available for download and analysis

## Project Structure

```
amarDokan/
├── src/
│   ├── main/
│   │   ├── java/com/amarDokan/amarDokan/
│   │   │   ├── controller/          # MVC and REST controllers
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── exception/           # Custom exceptions
│   │   │   ├── mapper/              # Object mappers
│   │   │   ├── models/              # JPA entities
│   │   │   ├── repository/          # Data repositories
│   │   │   ├── service/             # Business logic services
│   │   │   └── util/                # Utility classes
│   │   └── resources/
│   │       ├── static/              # CSS, JS, images
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.properties
│   └── test/                        # Unit and integration tests
├── .github/workflows/               # CI/CD configuration
├── uploads/                         # File upload directory
├── Dockerfile                       # Docker image configuration
├── compose.yaml                     # Docker Compose setup
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

---
---
---