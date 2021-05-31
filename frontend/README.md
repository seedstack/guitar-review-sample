# Guitar review Sample - Frontend #

This project is the frontend component of the seedstack's guitar review sample project.

This is an angular based project requesting the seedstack backend.

## Setup ##

Download required packages with NPM : `npm install`

If the seedstack backend doesn't run locally or doesn't use default web port or API sub-path, update the `src/app/app.module.ts` file and set the BASE_BACKEND_URL constant value with an URL related to the running backend.

Ensure the seedstack backend is running.

Just like any Angular application you can run the dev server with the angular client : `ng serve --open`

You can also build the sample with `ng build` and find in the `dist/` directory the production build to run from a web server.

## Project structure ##

- Domain classes : In the folder src/app, you'll find `category.ts` / `product.ts` / `review.ts` / `user.ts` classes related to the backend's representation classes. For example, `category.ts` corresponds to the backend's class `CategoryRepresentation` this sample has only one json format for each endpoint, of course, in more complex projects, a single endpoint can have multiple responses objects format, each one, must also be defined on the frontend side.
- Components : This project uses angular routing. In the src/app directory `dashboard`, `product-detail`, `products`, `user-reviews` are folders containing the target components (class / template /css )of angular routing.
- The `src/app/services` contains the services executing requests to the backend, these services are injected in the application components
- `srs/assets` folder contains fonts and images used by the GUI.