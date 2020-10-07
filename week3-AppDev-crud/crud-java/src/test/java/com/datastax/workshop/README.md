## Bug Maven + JUnit5:

`mvn test` does not excute any test (Tests run: 0)

```shell
$ mvn test

... etc ...

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Results :

Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

- [Discusstion](https://community.datastax.com/questions/6846/running-mvn-test-locally-on-crud-java-project-week.html)

- [Explanation](https://dzone.com/articles/why-your-junit-5-tests-are-not-running-under-maven)
 
 
## Workaround:

Run each test individually

```shell
cd ./week3-AppDev-crud/crud-java/

mvn test -Dtest=com.datastax.workshop.Ex02_Connect_to_Cassandra
mvn test -Dtest=com.datastax.workshop.Ex03_Query5a_Insert_Journey
mvn test -Dtest=com.datastax.workshop.Ex04_Query5b_TakeOff
mvn test -Dtest=com.datastax.workshop.Ex05_Query5c_Travel
mvn test -Dtest=com.datastax.workshop.Ex06_Query5d_Landing
mvn test -Dtest=com.datastax.workshop.Ex07_Query4a_ListJourneys
mvn test -Dtest=com.datastax.workshop.Ex08_Query4b_Read_Journey_Details
mvn test -Dtest=com.datastax.workshop.Ex09_Query4c_ReadMetrics
mvn test -Dtest=com.datastax.workshop.Ex10_Query4c_ReadMetrics_Paging
```
