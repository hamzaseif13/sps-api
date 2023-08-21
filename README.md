
# SPS Smart Parking System

SPS stands for Smart Parking System, Mobile application and admin dashboard (web), Mobile application Where drivers can discover nearby zones and book available barking spaces, Officers are assigned schedules and can report barking violations, Web application Admins manage the state of the application such as zone info, officers schedules, and get an overview about the application's state. 

<strong style="text-decoration:underline;">*This repositroy is for the backend (API)*.</strong>

[Live Admin Dashboard](https://sps-just-admin.netlify.app/) (admin@admin.com 123456789),

 [Mobile application Repository](https://github.com/hamzaseif13/sps-mobile)
 
  [Admin dashboard Repository](https://github.com/hamzaseif13/sps-admin)

**our infrastructure consists of 4 main components** 
 1. Backend API, a spring boot application hosted on AWS EC2 virtual machine, is protected by Cloudflare and provides a secure connection with clients and against DDoS attacks.
 2. Mysql database for data persistence, hosted on AWS RDS managed database.
 3. Web application for admin dashboard, Built with React hosted on Netlify.
 4. Mobile application for the user and officer, it's currently an APK for Android with plans to publish it to the Google app store.
  
![enter image description here](https://sps-violations.s3.eu-west-3.amazonaws.com/Screenshot%202023-06-14%20211414.png)

**Starting the application:**
prerequisites: having docker installed on your computer and running.

Step 1: cloning the repository :

    git clone https://github.com/hamzaseif13/sps-api.git
  
  Step 2: Open the installed repo and  create a .env file on the root, and add these variables this is for aws bucket credentials :
  

 

    AWS_REGION=YOUR_REGION
    AWS_S3_BUCKET=BUCKET_NAME
    AWS_ACCESS_KEY=YOUR_KEY
    AWS_SECRET=SECRET

   Step 3: Run docker compose to run the backend, frontend, and database : 
   if you are on Linux/mac run the following :
   
  ` docker compose --env-file ./.env up --build`
  
  if you are on windows  
` docker compose --env-file .\.env up --build`

Wait a few minutes for the application to build and install dependencies, this may take some time.
after the build process is finished the frontend should be running on [http://localhost:3000](http://localhost:3000) and the backend should be running on [http://localhost:8080](http://localhost:8080)
default admin logins : email: admin@admin.com,        password: 123456789

Backend API documentation [Postman](https://documenter.getpostman.com/view/17428762/2s93sgXqjD)

<hr/>

**Mobile application**
to run the mobile application locally you should have node js v17+ installed on your machine and install Expo go application on your phone (or use a simulator ).
You can also download the prebuild APK for Android from [here](https://expo.dev/accounts/hamzaseif/projects/sps-mobile/builds/e4ec5850-c6ab-48af-a9cc-8d593ad0d77f)

Step 1: clone the repository 

    git clone https://github.com/hamzaseif13/sps-mobile.git
Step 2: Install dependencies

    npm install
Step 3: starting the application:

    npm start
 Step 4: Open the expo go application on your phone and scan the QR code prompted in the console
 
 default user logins: email : customer@customer.com,        password: 123456789
 
  default officer logins : email: officer@officer.com,        password: 123456789



