# Reewild Carbon FoodPrint Estimator

A full stack web application that estimates the carbon footprint (added water print as well) of dishes using Google AI Studio’s API.
The system also provides authentication with email verification and add the user details as well as the last login time in the database.

The carbon footprint with the dish is not added in the database (as mentioned in the assignment document).

### Note - The Registration takes around 6-15 seconds (as the JavaMailSender bean is being initialized)

### Frontend Link - https://github.com/vittalkatwe/Reewild-Carbon-Foodprint-With-Auth-Frontend.git

## You can follow this readme file, it has steps to run both frontend and backend.


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
**Backend (Spring Boot)** (Before running the backend please make sure that the **application.properties** is updated accordingly)
```bash
git clone https://github.com/vittalkatwe/Reewild-Carbon-Foodprint-With-Auth-Backend.git
cd Reewild-Carbon-Foodprint-With-Auth-Backend
mvn spring-boot:run
```


**Frontend (React)**
```bash
git clone https://github.com/vittalkatwe/Reewild-Carbon-Foodprint-With-Auth-Frontend.git
cd Reewild-Carbon-Foodprint-With-Auth-Frontend
npm install
npm run dev
```

### 2. Run Using Docker

```bash
docker run -d -p 8080:8080 vittalkat/carbon-backend
docker run -d -p 5173:5173 vittalkat/frontend-app:latest
```
## the frontend will be running on **http://localhost:5173**
## the backend will be running on **http://localhost:8080**


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

## Assumptions and Limitations
- **The system does not store user preferences or history**

- **Carbon emission values for ingredients are inferred from AI and may not be precise**

- **Currently I'm using Map Data Structure as Cache to reduce the AI calls for frequent dishes**

- **may not capture exact ingredient quantities or certain ingredients**

- **Currently it is designed for one dish at a time, does not handle full meals, combos, or meal plans. if an image is upload with two or more meals it may not be able to identify**

- **Certain common misspellings or variations of dish names are automatically corrected (e.g., “chiken biryani” → “Chicken Biryani”) to improve AI prediction accuracy**

- **AI may combine similar ingredients (e.g., “spices”) into a single category.**

---


## My key Design decisions and reasoning
1. **Spring Boot for Backend**  

   I chose spring boot for its simplicity, security support (Spring Security + JWT), faster development, and easy integration with MongoDB.

2. **React + TypeScript Frontend**  
 
   Provides a fast, modern, type safe frontend. Vite ensures fast page loads.

3. **Database - MongoDB**  

   Flexible schema for storing user data (email, password, verification, last login, createed at, verified at). easy to scale and integrate with Spring Boot.

4. **JWT Authentication**  

   Stateless token-based authentication for secure and scalable login sessions.

5. **Email Verification**  

   Ensures valid users and prevents spam registrations.

6. **Google AI Studio Integration**  

   - LLM for ingredient inference from dish names.  
   - Vision API for identifying dishes from images.  
   Allows estimation without hardcoding dishes or ingredients.

7. **Cache for Frequent Dishes**
   
   A Map data structure stores previously requested dishes and their carbon scores to reduce API calls and speed up responses. I know this is not efficient, we can replace it with redis in future.

8. **Dish Name Autocorrection**
   
   Corrects common misspellings to improve AI prediction accuracy.

9. **Future-ready Structure**
      
    Modular code structure allows easy addition of features like dashboards, user favorites, gamification, and multi-language support.

10. **Data Model**
    
    I have taken these (email, password, verification, last login, createed at, verified at), as in future we might add some new features, where we show users when their acccount was create or when did they last login.
    

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
- **Allow users to save their favorite dishes and track their personal carbon footprint and also suggest lower carbon alternatives for ingredients to promote sustainable choices**

- **A dashboard where admin can see the insights**

- **Implement caching for frequently requested dishes to reduce API calls**

- **User can also see historical footprint comparisons**

- **Reward users for choosing lower carbon dishes with points, badges or rank them in leaderboard**

- **Multiple language support**

- **Try Microservices, where we scale the auth service and carbon estimation services individually as per the traffic**

- **Add OAuth2 login (Google, GitHub)**

- **Role-based access (Admin, User)**



