# ClearBreath Service - README

## Overview

The ClearBreath API provides real-time access to air quality and weather forecast data, enabling users to monitor environmental conditions in their area. It offers endpoints for user authentication, marker management, and data retrieval, making it ideal for developers building health and environmental awareness applications. With comprehensive coverage and precise data, the ClearBreath API is your go-to solution for integrating environmental data into your applications.

## Prerequisites

Ensure the following are installed on your system:

- **Java JDK 17** or later
- **Maven** 3.6.3 or later
- **MySQL Database** (MySQL 8 recommended)

## Setup Instructions

### Step 1: Clone the Repository

Begin by cloning the ClearBreath repository to your local machine:

```bash
git clone https://github.com/P4r1nc3/ClearBreath
```

Then, navigate to the project directory:

```bash
cd ClearBreath
```

### Step 2: Configure Environment Variables

FlashDash requires the configuration of certain environment variables related to database connectivity and JWT authentication. These are defined in the `application.properties` file and must be set prior to running the application:

- `DB_HOST`: Database URL (e.g., `jdbc:mysql://localhost:3306/clear-breath`)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_KEY`: Secret key for JWT token signing

### Step 3: Configuring Environment Variables in IntelliJ IDEA

To configure environment variables in IntelliJ IDEA:

1. Open the project in IntelliJ IDEA.
2. Navigate to the `Run` menu and select `Edit Configurations...`
<img width="475" alt="1" src="https://github.com/P4r1nc3/ClearBreath/assets/51295585/2a473cdb-8046-4bbd-9013-0689599b2d54">

3. In the `Run/Debug Configurations` window, select your Spring Boot application.
<img width="1152" alt="2" src="https://github.com/P4r1nc3/ClearBreath/assets/51295585/d39cd51a-a24e-4b64-a1c2-33d4c96a461d">

4. Click `Modify options`.
<img width="1152" alt="3" src="https://github.com/P4r1nc3/ClearBreath/assets/51295585/6b07d880-a82c-4c3a-9478-9c7bf167b8a7">

5. From the `Operating System` section, select `Environment variables.`
<img width="503" alt="4" src="https://github.com/P4r1nc3/ClearBreath/assets/51295585/ff070ce6-a523-434c-ab48-b3dff55f295f">

6. In the `Environment variables` input field, enter your variables as key-value pairs, separated by semicolons:
```
DB_HOST=jdbc:mysql://localhost:3306/clear-breath;DB_USERNAME=root;DB_PASSWORD=admin12345;JWT_KEY=sQF1C+qQJpmV9A/M8/LPd5esayq2dpG/cD0MCEwJZjM=;TOMORROW_API_KEY=DVnNicp9JzPpqTXYtKsDlSI83jcWTvAp;WAQI_API_KEY=c5da53c6a0c6265f04b72dbd409a3c2eaae0b574
```
<img width="1152" alt="5" src="https://github.com/P4r1nc3/ClearBreath/assets/51295585/a6c7d883-caef-47f2-a648-a61f34159b9a">

7. Click `Apply` and then `OK` to save the configuration.

### Step 4: Building the Application

To build the FlashDash application, use the following Maven command:

```bash
mvn clean install
```

This command compiles the project and runs any tests, ensuring the application is correctly set up.

### Step 5: Running the Application

After successfully building the project, run it with Maven by executing:

```bash
mvn spring-boot:run
```

This starts the FlashDash service, making it accessible at `http://localhost:8080`.

## Conclusion

Following these instructions, you have successfully set up and launched the ClearBreath service on your local machine. The service is now ready to facilitate the creation, management, and utilization of markers.
