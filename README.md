# Project Title

Asteroid Radar

## Dev Tasks

### Getting Started
[x] Clone the Repo.

[x] Open the project with Android Studio.

[x] Run the project and check that it compiles correctly.

### Main Screen
[x] Include Main screen with a list of clickable asteroids as seen in the provided design using a RecyclerView with its adapter.

[x] You could insert some fake manually created asteroids to try this before downloading any data.

[x] Remove fake manual asteroid data before continuing

### Details Screen

[x] Include a Details screen that displays the selected asteroid data once it’s clicked in the Main screen as seen in the provided design. 

[x] The images in the details screen are going to be provided with the starter code: an image for a potentially hazardous asteroid and another one for the non-hazardous ones, you have to display the correct image depending on the isPotentiallyHazardous asteroid parameter. 

[x] Navigation xml file is already included with starter code.


### Fetch Data - Asteroids

[x] Download and parse the data from NASA NeoWS (Near Earth Object Web Service) API. As this response cannot be parsed directly with Moshi, we are providing a method to parse the data “manually” for you, it’s called parseAsteroidsJsonResult inside NetworkUtils class, we recommend trying for yourself before using this method or at least take a close look at it as it is an extremely common problem in real-world apps. 

[x] For this response we need retrofit-converter-scalars instead of Moshi, you can check this dependency in build.gradle (app) file.

[x] When asteroids are downloaded, save them in the local database.

[x] Fetch and display the asteroids from the database and only fetch the asteroids from today onwards, ignoring asteroids before today. 

[x] Also, display the asteroids sorted by date (Check SQLite documentation to get sorted data using a query).

### Caching Data

[x] Be able to cache the data of the asteroid by using a worker, so it downloads and saves today's asteroids in background once a day when the device is charging and wifi is enabled.

### Fetch Data - Image

[ x] Download Picture of Day JSON, parse it using Moshi and display it at the top of Main screen using Picasso Library. (You can find Picasso documentation here: https://square.github.io/picasso/) You could use Glide if you are more comfortable with it, although be careful as we found some problems displaying NASA images with Glide.

### Accessibility

[x] Add content description to the views: 
[x] Picture of the day (Use the title dynamically for this), 
[x] details images and 
[x] dialog button. 

[x] Check if it works correctly with talk back.

[x] Make sure the entire app works without an internet connection.