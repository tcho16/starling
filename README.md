# Starling Interview Challenge - Tarikh Chouhan

This is the code based on the specification that Starling has provided.

There have been some assumptions made when coding this such as:
- The user may not be able to have more multiple goals with the same name.
- User cannot deposit 0 (zero) money to the goal if no transactions were made in the week
- It rounds up to the upper nearest pound (e.g. if user spends £2, it will deposit £1 to the goal. This logic was deduced from the specification).

## Structure of the codebase
- The structure of this code follows a domain driven approach. 
- We have a domain package in which the main logic resides in. It has ports injected into it which is known as the anti corruption layer.
- Any external entities (such as a controller, Starling's API etc) are known as Adapters (which are the implementation of the Ports) that interacts with the domain. Adapters resides in the integration package. 
- We have converters in place so that the integration and domain modules are looseley coupled. These converters are used to convert between domain/integration. 
- We also have an API package in which these are the data structure/objects the external entities uses. 
- We have a model package in the domain package which is exclusively used within the domain package.

## How to run this application:
- Need to have Java 11 installed and maven.
- Ensure you have a valid access Token and is put in the application.properties file.
- Ensure you have a valid X509 certificate  and is placed in the 'resources' folder with the name 'starling-sandbox-api-certificate.crt'. (Can be downloaded from the Starling docs website: https://developer.starlingbank.com/starling-sandbox-api-certificate.crt).

Steps:

Via terminal:

- 1 - Run `mvn clean install` on the main directory of this app (essentially in the same directory as pom.xml file).
- 2 - This will generate a target folder. Enter the target folder and run the following command `java -jar starling-0.0.1-SNAPSHOT.jar`

Via IntelliJ
- Import the project and then run the main class located in `src/main.java/com.tarikh/interview/starling/StarlingApplication`.

## How to use the application
One endpoint is exposed. It is a PUT method. The endpoint is:

`http://localhost:8080/account/{accountHolderUId}/saving-goals/transactions/roundup`

and takes in a JSON request body with the structure:

`{"timestamp":"2021-10-18 21:46:07","savingGoalName":"New Fitness Goal"}`

