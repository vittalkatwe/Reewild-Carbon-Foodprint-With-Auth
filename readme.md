# Reewild Carbon FoodPrint Estimator

A full stack web application that estimates the carbon footprint (added water print as well) of dishes using Google AI Studio’s API.
The system also provides authentication with email verification and add the user details as well as the last login time in the database.

The carbon footprint with the dish is not added in the database (as mentioned in the assignment document).

---

## Features

### User Authentication

*   **login & signup:** authentication system to protect user accounts.
*   **Email verification:** Ensures that users provide a valid email address during registration.
*   **Last login time:** The timestamp of the last successful login is recorded and stored in the database.

### Carbon Footprint Estimation
*   **ingredient inference:** Uses the Google AI Studio API to infer the ingredients of a dish from its name.
*   **footprint analysis:** Estimates the carbon footprint of each individual ingredient and calculates the total for the entire dish.

---

## Tech Stack

*   **Backend:** Spring Boot, Spring Security, JWT
*   **Frontend:** React (TypeScript + Vite)
*   **Database:** MongoDB
*   **AI Integration:** Google AI Studio API
*   **Deployment:** Docker & Docker Hub

---

## Run Application


### 1. Run Locally
**Backend (Spring Boot)**
```bash
git clone https://github.com/vittalkatwe/Reewild-Carbon-Foodprint-With-Auth.git
cd Reewild-Carbon-Foodprint-With-Auth
mvn spring-boot:run
```


**Frontend (React)**
```bash
cd frontend
npm install
npm run dev
```

### 2. Run Using Docker

```bash
docker run -d -p 8080:8080 vittalkat/carbon-backend
docker run -d -p 5173:5173 vittalkat/frontend-app:latest
```


### Application Properties
**Backend (resources/application.properties)**
```bash
#to connect to mongo db
spring.data.mongodb.uri=add a mongodb uri

#AI API's
google.api.key=add google ai studio api
google.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent (needs to change if the model is no longer available)
google.api.vision.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent (needs to change if the model is no longer available)


#To allow CORS
spring.web.cors.allowed-origin-patterns=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*


#For Mail Verification (to send verification mails)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your email
spring.mail.password=your email app password (not email password)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```


---



| Column          | Type      | Constraints        |
| --------------- | --------- | ------------------ |
| id              | UUID      | Primary Key        |
| email           | String    | Unique             |
| password        | String    | Hashed             |
| verified        | Boolean   | Default: false     |
| last\_login\_at | Timestamp | Auto-updated login |
| created\_at     | Timestamp | Current TimeStamp  |
| verified\_at    | Timestamp | Verification time  |


---


## 🌟 Future Enhancements

**Add OAuth2 login (Google, GitHub)**

**Role-based access (Admin, User)**

**A dashboard where admin can see the insights**

**User can also see historical footprint comparisons**

**Multiple language support**

**Try Microservices, where we scale the auth service and carbon estimation services individually as per the traffic**


