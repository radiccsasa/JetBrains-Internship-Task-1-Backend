# JetBrains-Internship-Task-1-Backend
Hello my name is Saša Radić! 
Welcome to my project for JetBrains Intership : Backend for main analytics dashboard

************************************************************************

**Project Description**

This project is a backend application developed as part of a JetBrains internship task. The goal is to create a REST API service using Spring Boot that allows:
-storing SQL queries defined by users
-executing those queries on a preloaded dataset
-returning query results
-additional features such as asynchronous execution and result caching

**Features**

-REST endpoint for adding a new SQL query
-REST endpoint for retrieving all saved queries
-REST endpoint for executing a query by ID (synchronously)
-Optional REST endpoint for executing a query asynchronously
-Uses an in-memory H2 database with data loaded from a CSV file (passengers table) on startup
-Only SELECT queries are allowed to prevent data modification
-Result caching to avoid re-executing the same query multiple times
-Layered architecture: Controller → Service → Repository → Model (Entities)

**Technologies and Tools**

-Java (version 21 or 24 recommended)
-Spring Boot (starter-web, starter-data-jpa, starter-test)
-H2 in-memory database
-JDBC/Template for dynamic SQL execution
-Maven as build tool
-Lombok (optional) to reduce boilerplate code
-JUnit + Spring Boot Test for testing

Project Structure
queryapp/
 ├─ pom.xml
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/
 │   │   │   ├─ com/example/queryapp/
 │   │   │   │   ├─ QueryApplication.java        # main class
 │   │   │   │   ├─ controller/                   # REST controllers
 │   │   │   │   ├─ service/                      # business logic
 │   │   │   │   ├─ repository/                   # JPA and JDBC repositories
 │   │   │   │   ├─ entity/                       # entities (Query, Passenger)
 │   │   │   │   ├─ loader/                       # CSV loader component (DataLoader)
 │   │   ├─ resources/
 │       ├─ application.properties               # configuration
 │       └─ titanic.csv                           # dataset
 └─ src/
     └─ test/                                   # tests

**How to Run**

Clone the repository:

git clone https://github.com/radiccsasa/JetBrains-Internship-Task-1-Backend.git

-Open the project in an IDE (e.g., IntelliJ IDEA).
-Build the project with Maven (mvn clean install or from IDE).
-Run the main class QueryApplication.java. The application will start and load the dataset into the H2 database.

Run QueryServiceTest.java which is located in Queries\queryapp\src\test\java\com\example\queryapp\service.
Run QueryControllerTest.java which is located in Queries\queryapp\src\test\java\com\example\queryapp\controller.
You can also use Postman or any other tool to test request -> http://localhost:8080/queries

************************************************************************


**NOW I WILL DESCRIBE MY SOLUTION**


(1)
My job was to develop a Spring Boot Application with REST API
Firstly I started with endpoints, thinking what would be the results of running my Application.
As said service must have 3 endpoints:

-Endpoint for adding query

-Endpoint for geting all saved queries

-Endpoint for executing queries by ID

Started with REST Controller, figured out that it will be based on Queries so ->
I've made a Query Entity, which will be used to store Queries in a Table.
For that I used annotation @Entity which makes a Table with name queries.
I gave it 2 attributes: a generated ID(Long) and a text(String) for the query.
Then I've made a QueryRepository so I can use basic query method like save and listAll.

(2)
Then I had to load the database from csv file into h2 memory, for that I used the DataLoader class.
Then I made a passenger entity and repository so I can save it into h2 memory and later execute queries over that database.
The DataLoader class use BufferReader to read text from csv file, it reads file line by line, whit each line being a separate passenger, then split the line so I can get every part to be a attribute of the new entity.

Once all passengers are loaded into a list, the list is saved into the PassengerRepository.

(3)

Now I have a database on which I can execute queries.

The given queries will be added with requests so I need a service which will be used in controller for get and post requests.

I made a QueryService with 3 methods: saveQuery, getAll, executeQuery.

Save and getAll were basic JPA methods but execute was the tricky one, firstly I though about a separate passenger service which would be used for executing queries over passengers, but I wanted to make it simpler and tried it in QueryService. As I use H2 memory, only tables in there are queries and passengers, so If I use a JDBCTemplate to execute queries, it would be easier -> JDBCTemplate will recognize table passengers from query Select * from passengers. But then I figured there might be a problem which i will talk later about.



(4) So now All that left for me to test my app was to finnish RestController

I made 3 endpoint, 1 post for adding Query and 2 get requests so I can get all queries from database or execute one.

I tested it on PostMan and after minor changes my app works. Firstly Data got Loaded into h2, and then I could add request and execute them over passanger database, as I load data form csv every time when I start app, deleting and insert didnt have function. And as queries should not modify the data, I just added simple If clause so only queries which start with SELECT can be executed.

(5) To save some time for executing queries over large database I have used an Async method and an Endpoint for its execution.
It will be executed in different thread, not in main one. With this server won't be blocked for other requests, altho you'll have to wait for query to execute and give the response. So no query will block other query. Then I added a in-memory cashe, so now the result of executed Queries will be saved in cashe memory, and if needed would be resend again so app doesn't need to execute same queries again. After that I wrote some basic tests so I can test my app without PostMan. After some test I figured that I forgot to save queries in cashe memory -> fixed.

(Problems)

As I used JDBCTemplate, if u write query "SELECT * from queries", it will execute, as it is not precise that only queries over passangers will executed, I left that "mistake" but it can be easy to fix in later development.
As of now project is small I cannot think of its use, so its limited for now, but altho can be upgraded into big projects, as You can save and execute queries over any database you want.
