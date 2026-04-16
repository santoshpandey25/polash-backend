# 🏦 KMS Banking Backend

A complete Spring Boot banking workflow engine. It handles customer KYC processing, Liveness image storage, and Bank Portal employee access out of the box.

---

## 🛠️ Prerequisites

To run this project, make sure your machine has:
1. **Docker Desktop** installed and actively running.
2. **Postman** installed to test the application's APIs.

---

## 🚀 How to Run the Project

Since we do not upload the heavy 74 MB application file to GitHub, you need to generate it on your machine first. Follow these steps:

1. **Download & Extract:** Clone or download this project on your PC.
2. **Build the JAR File:** Open a terminal in the project folder and run:
   ```powershell
   .\mvnw.cmd clean package -DskipTests

   Fire up Docker: Once the build is successful and you see the target/ folder created, run:
   ```powershell
   docker-compose up --build

