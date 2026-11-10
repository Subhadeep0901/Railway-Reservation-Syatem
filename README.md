# 🚆 Railway Reservation System

A web-based Railway Reservation System developed using **Java, Spring Boot, MySQL, Spring Data JPA, Spring Security, and Thymeleaf**.

The application allows passengers to search trains, book tickets, manage reservations, and download tickets. It also provides an admin panel to manage trains and bookings.

## ✨ Features

### 👤 Passenger Features
- Passenger registration and login
- Search trains by source, destination, and journey date
- Check available seats
- Book multiple seats
- Generate unique PNR numbers
- Search booking details using PNR
- Cancel train tickets
- Download tickets in PDF format
- Email booking confirmation

### 🛠️ Admin Features
- Admin login
- Admin dashboard
- Add new trains
- View and manage trains
- Edit train details
- Activate or deactivate trains
- View booking details
- Manage ticket cancellations

## 💻 Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot | Application framework |
| Spring MVC | Web request handling |
| Spring Data JPA | Database operations |
| Spring Security | Admin authentication |
| MySQL | Database |
| Thymeleaf | Dynamic HTML templates |
| HTML5 | Page structure |
| CSS3 | Styling |
| Maven | Dependency management |
| iText | PDF ticket generation |
| JavaMail | Email notifications |

## 📂 Project Structure

```text
railway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── config/
│   │   │   │   └── example/railway/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## ⚙️ Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Subhadeep0901/Railway-Reservation-Syatem.git
```

### 2. Open the Project

Open the cloned project in VS Code or IntelliJ IDEA.

### 3. Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE railway_db;
```

Configure your database connection in `src/main/resources/application.properties`.

Use your own database username and password. Configure email credentials separately if email notifications are enabled.

**Never upload real passwords or secret credentials to GitHub.**

### 4. Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or on Windows:

```bash
mvnw.cmd spring-boot:run
```

### 5. Open in Browser

```text
http://localhost:8080
```

## 📸 Screenshots

Screenshots of the application will be added here.

- Passenger Login
- Passenger Registration
- Train Search
- Ticket Booking
- Booking Confirmation
- Admin Dashboard

## 🚀 Future Improvements

- Online payment integration
- Live train status tracking
- Seat selection interface
- Improved role-based passenger access
- Deployment to a cloud platform

## 👨‍💻 Developer

**Subhadeep Das**

- GitHub: [Subhadeep0901](https://github.com/Subhadeep0901)
- LinkedIn: [Subhadeep Das](https://www.linkedin.com/in/subhadeep-das-b79b70343)

---

⭐ If you find this project interesting, feel free to explore the repository!
