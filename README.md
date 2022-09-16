### This is the backend application which enables users to perform CRUD operations on the notes. A note is a simple data structure which consists of a title, body and a few other fields. The app is built in the style of a RESTful API that enables devs to plug in multiple clients in their favorite language/framework/stack (browser, mobile, desktop etc..)
---
#### Below you will see the typical flow of the app usage:
1) User registration
   - User registers with email and password
   - User email must be unique
2) User login
   - User must log in to get back a JSON Web Token (JWT)
   - JWT will be used for accessing protected routes 
3) Display User notes
   - User can fetch all notes which he/she has ever created/updated
4) Create new User note
   - User can create a new note with specified title and body
5) Modify/Delete existing User notes
   - User can change the fields of the specific note, or delete it at all
6) Search/Sort User notes
   - User can apply exact/non-exact searching and ascending/desending sorting to his/her notes

#### Let's discuss step by step what each step does and how the HTTP Request/Response, API Endpoint and related things look like:
---
1) ### User registration
   | Endpoint                              | HTTP Method | Content Type     | HTTP Success (Statuscode)   | HTTP Failure (Statuscode)         |
   | ------------------------------------- | ----------- | ---------------- | --------------------------- | --------------------------------- |
   | http://localhost:8080/api/user/signup | POST        | application/json | User has been created (200) | User already exists (409)         | 
   
   #### Request Body: 
   ```json
   {
     "name": "Nika",
     "email": "nika@gmail.com",
     "password": "my-strong-pass"
   }
   ``` 
---

2) ### User login
   | Endpoint                              | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)         |
   | ------------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------- |
   | http://localhost:8080/api/user/login  | POST        | application/json | ```{"token": "JWT"}``` (200) | Auth failed (401)                 | 
   
   #### Request Body: 
   ```json
   {
     "email": "nika@gmail.com",
     "password": "my-strong-pass"
   }
   ``` 
3) ### Display User notes
   | Endpoint                        | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)         |
   | ------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------- |
   | http://localhost:8080/api/notes | GET         | application/json | Notes in JSON format (200)   | Auth failed (401)                 | 
   
   ##### hint: user the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
   #### Typical response: 
   ```json
    [
      {
          "id": 7159997665991673534,
          "title": "I love Scala",
          "body": "Scala rocks (most definitely)",
          "createdAt": "09-16-2022"
      },
      {
          "id": 5746959445480553359,
          "title": "I kinda like Java",
          "body": "Java rocks (kinda)",
          "createdAt": "09-16-2022"
      },
      {
          "id": 3672367746746389626,
          "title": "I completely hate Python",
          "body": "Python sucks (yep yep)",
          "createdAt": "09-16-2022"
      }
   ]
   ```
