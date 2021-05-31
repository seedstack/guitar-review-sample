# Guitar reviews

A full application for reviewing guitars based on an Angular frontend and SeedStack backend.

This sample shows an application used for reviewing guitar gear. Once accessing the application, you are connected as an admin and can add/update/delete gear, and as any other user add a review.

The application has two components :
- The backend component : accesses the data and exposes API endpoints for CRUD requests on domain items, the backend also validates user permissions ( not implemented yet )
- The frontend components : manages interraction with the user and requests the backend. In this sample the frontend is made with angular, but as all exchanges between the frontend and the backend are made through an API, any technology performing HTTP requests could have been used.


## Installation notice ##

### Database and images folder ###

Copy the `data/database` and `data/images` files in "database" and "images" (choose different names if wanted) folders on your file system. The path of these folders will have to be set in the backend configuration.

### Backend ###

Setup and run the backend server.

Please refer to the `./backend/README.md` for updating the configuration and running the backend server.

The backend server has to be running before starting the frontend server

### Frontend ###

If you didn't change api endpoint in the backend configuration, the frontend can be ran without changes.

Please refer to `./frontend/README.md` for running the front end server.