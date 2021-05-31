## Guitar review sample - Backend ##

This is the backend component of the application.

The backend provides API endpoints called by the frontend.

### Setup ###

This sample application provides a HSQLDb database file and some sample images, access to these components need to be set up in seedstack configuration.

( Please see the root project Readme file for getting the database files and images required to run this sample. )


Edit the seedstack configuration file /src/main/resources/application.yaml and set the following :

- application.databaseFile : Path to the directory containing the database files
- application.imageFolder : Path to the directory containing the sample images files

This sample uses seedstack default web port ( 8080 ) and exposes its endpoints on the /api sub path. Remember to update frontend configuration ( src/app/app.module.ts file) when updating these parameters.

### Running ###

To run this backend :

- From your IDE : Updates the project dependencies from maven and start a java application runner with the main class org.seedstack.seed.core.SeedMain
- From the command line : Build a capsule jar with maven : `mvn clean package` then run the backend server from the java command : `java -jar guitar-reviews-sample-capsule.jar`

### structure ###

The project root package is org.seedstack.samples.guitar, containing the sub packages :
- domain.model : 
    - Define models ( Aggregates roots ) for Product / Category / Review / User
    - Define repositories interfaces for each model
    
- infrastructure.jpa : JPA Implementation for each Aggregate repository, in this sample project implementations are sub classes of BaseJpaRepository
- interfaces : API endpoints for each domain item, also define responses representations