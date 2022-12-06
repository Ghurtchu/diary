### This is the backend application which enables users to perform CRUD operations on the notes. A note is a simple data structure which consists of a title, body and a few other fields. The app is built in the style of a RESTful API that enables devs to plug in multiple clients in their favorite language/framework/stack (browser, mobile, desktop etc..)

### The app has no integration tests because I have a full time job.. LOL (might add them later though)
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
---   
3) ### Display User notes
   | Endpoint                        | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)         |
   | ------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------- |
   | http://localhost:8080/api/notes | GET         | application/json | Notes in JSON format (200)   | Auth failed (401)                 | 
   
   ##### hint: use the real JWT returend upon the successful login
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
---
4) ### Create new Note
   | Endpoint                        | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)         |
   | ------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------- |
   | http://localhost:8080/api/notes | POST        | application/json | Note has been created (200)  | Auth failed (401)                 | 
   
   ##### hint: use the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
   #### Request Body: 
   ```json
   {
       "title": "why should I learn ZIO?",
       "body": "cuz [insert 5 million intelligent words here]",
       "createdAt": "17-09-2022"
   }
   ```
---
5) ### Get Note by ID
   | Endpoint                            | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)               |
   | ----------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------------- |
   | http://localhost:8080/api/notes/:id | GET         | application/json | Note in JSON format (200)    | Auth failed (401) / Note does not exist | 
   
   ##### hint: use the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
   #### Typical Response: 
   ```json
   {
       "id": 5321604607032827422,
       "title": "why should I learn ZIO?",
       "body": "cuz [insert 5 million intelligent words here]",
       "createdAt": "17-09-2022"
   }
   ```
---
6) ### Delete Note by ID
   | Endpoint                            | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)               |
   | ----------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------------- |
   | http://localhost:8080/api/notes/:id | DELETE      | application/json | Note has been deleted (200)  | Auth failed (401) / Note does not exist | 
   
   ##### hint: use the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
---
7) ### Update Note fully
   | Endpoint                            | HTTP Method | Content Type     | HTTP Success (Statuscode)    | HTTP Failure (Statuscode)               |
   | ----------------------------------- | ----------- | ---------------- | ---------------------------- | --------------------------------------- |
   | http://localhost:8080/api/notes/:id | PUT         | application/json | Note has been updated (200)  | Auth failed (401) / Note does not exist | 
   
   ##### hint: use the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
   #### Request Body: 
   ```json
   {
      "id": 7043231874327471104,
      "title": "why should I learn ZIO?!?!?!?!?!",
      "body": "cuz [insert 5 million intelligent words here]",
      "createdAt": "17-09-2022"
   }
   ```
---
8) ### Search for a specific Note 
   | Endpoint                                             | HTTP Method | Content Type     | HTTP Success (Statuscode)        | HTTP Failure (Statuscode) |
   | ---------------------------------------------------- | ----------- | ---------------- | -------------------------------- | ------------------------- |
   | http://localhost:8080/api/notes/search?title={title} | GET         | application/json | Note array in JSON format (200)  | Auth failed (401)         | 
   
   ##### hint: use the real JWT returend upon the successful login
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
---
9) ### Sort notes by title 
   | Endpoint                                       | HTTP Method | Content Type     | HTTP Success (Statuscode)        | HTTP Failure (Statuscode) |
   | ---------------------------------------------- | ----------- | ---------------- | -------------------------------- | ------------------------- |
   | http://localhost:8080/api/notes/sort?order=asc | GET         | application/json | Note array in JSON format (200)  | Auth failed (401)         | 
   
   ##### hint: use the real JWT returend upon the successful login
   #### Headers:
   ```
   Authorization: Bearer JWT
   ```
   #### Typical response: 
   ```json
   [
     {
       "id" : 3672367746746389626,
       "title" : "I completely hate Python",
       "body" : "Python sucks (yep yep)",
       "createdAt" : "09-16-2022"
     },
     {
       "id" : 5746959445480553359,
       "title" : "I kinda like Java",
       "body" : "Java rocks (kinda)",
       "createdAt" : "09-16-2022"
     },
     {
       "id" : 7159997665991673534,
       "title" : "I love Scala",
       "body" : "Scala rocks",
       "createdAt" : "09-16-2022"
     },
     {
       "id" : 7043231874327471104,
       "title" : "why should I learn ZIO?",
       "body" : "cuz [insert 5 million intelligent words here]",
       "createdAt" : "17-09-2022"
     }
   ]
   ```
---



