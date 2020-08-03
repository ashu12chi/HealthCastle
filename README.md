# Health Castle
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)<br>
Official repository of team NP Devs for Prototype 2.0 (a 36 hour hackathon) held at IIIT- Allahabad.

**Webpage:** [https://ashu12chi.github.io/HealthCastle/](https://ashu12chi.github.io/HealthCastle/)


## Welcome to Swastha Bharat
- The app provides an easy platform to people for monitoring their and their friend's/ family member's health

## The Process Flow

1. Login/Sign Up,
2. View Optimal Calorie Limit for you,
3. Add the food that you ate to record calories consumed,
4. Add exercises done today,
5. App counts number of steps taken throughout the day automatically,
6. View total calories burnt today,
7. Check Heartbeat rate using Camera,
8. Record sugar level,
9. Record blood pressure level,
10. View graphs for past data related to these,
11. Add person in your family,
12. View past data graphs of your family members,
13. Get notifications if your family member is unhealthy or is depressed; and,
14. Contact doctors/ visit online pharmacy regarding your or your friend/family member health.

## Special Features

1. When person launchs the app, a photo of his/her face is captured and a Machine Learned Tensorflow model is used to detect the persons mood. This mood is further used to find the person is depressed.
2. If person feels depressed according to ML analysis, push notifications are sent to his friends and family members regarding this and a graph of mood fluctuations is shown.
3. App supports full voice control.
4. Food and exercise databases are extensible at personal level.

## APIs and Components used

- Firebase Realtime Database
- Firebase Authentication
- Tensorflow
- MPAndroidChart by PhilJay for interactive graphs
- AndroidX artifacts with Google Material Design components
- SQLite database

## Health Castle
**Team:** NPDevs

**Members:**

1. [Ashutosh Chitranshi](https://github.com/ashu12chi/)
2. [Nishchal Siddharth Pandey](https://github.com/nisiddharth/)
3. [Ankit Raj](https://github.com/rjankit/)

## To try hands on the project
Either just try installing the debug APK from [this link](https://drive.google.com/file/d/1aedgRhm36I5aONZsNLgUS3WSrRdAvzzQ/view?usp=sharing); OR:
1. Clone the project using link: https://github.com/ashu12chi/HealthCastle.git
2. Import the project in Android Studio
3. Deploy the app to your Android device and viola... you are good to go!

## Screenshots

|  |  |  |
|--|--|--|
|Main Activity ![Main Activity](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/main.jpg?raw=true)|Select Food eaten ![Select Food](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/food_select.jpg?raw=true)|Add amount of food eaten ![Food Measure](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/food_measure.jpg?raw=true)|
|Select exercise done ![Exercise Select](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/exercise_select.jpg?raw=true)|Enter time spent in exercise ![Exercise Measure](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/exercise_measure.jpg?raw=true)|Measure Heartrate ![Measure heartrate](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/hb_measure.jpg?raw=true)|
|Add new food item to database ![New Food](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/new_food.jpg?raw=true)|Add new exercise to database ![New Exercise](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/new_exercise.jpg?raw=true)|Record Blood Presure ![Record BP](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/bp.jpg?raw=true)|
|Add new person to family ![New Person](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/new_person.jpg?raw=true)|Family member details and services ![Member details](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/person.jpg?raw=true)| Family member problem notification ![Notification](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/notification.jpg?raw=true)|
|Historical graph of steps ![Steps graph](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/graph_steps.jpg?raw=true)|Historical graph of heartrate ![Heartrate graph](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/graph_hb.jpg?raw=true)|Historical graph of calorie intakes ![Calories graph](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/graph_calorie.jpg?raw=true)|
|Record Sugar level ![Record Sugar](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/sugar.jpg?raw=true)| Historical Sugar level graph ![Sugar graph](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/graph_sugar.jpg?raw=true)|Mood detected toast ![Mood detected](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/happy.jpg?raw=true)|
|App's Drawer ![Navigation drawer](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/drawer.jpg?raw=true)|App can be navigated via voice controls ![Voice Control](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/voice.jpg?raw=true)|Registration page (supports autofill) ![SignUp](https://github.com/nisiddharth/HealthCastle/raw/master/screenshots/register.jpg?raw=true)
