A) Running the program from command line 
B) Setting up project IDE
C) Project Design
==============A) Running the program from command line =========================

1) Unzip the file , you will see EK_CARTA dir.
2) Open the command line interface ( windows or Unix) 
3) go to the dir EK_CART
4) depending on windows or unix env execute vesting_program.bat or vesting_program.bash with two parameters CSV file and from date. 
e.g : 

./vesting_program example2.csv 2021-01-01
NOTE: port 8080 is needed for application to run.

5) To exit the program press Ctrl+C

====================B Setting up project IDE ======================

Project is maven based therefore import the project as maven in your ide. 
you can run the project from your IDE by passing parameters below parameters
--inputFile=vesting_events.csv --fromDate=20200401

=====================C Project Design============================
Project is written as a spring batch where CSV is loaded into the H2 in-memory database table : VESTING_SCHEDULE

Phase 1 of the project 
In order to get all the users even if they don't fulfill the criteria of 'on or before date' I had two choice 
First choice
1.1) get all users and their awards from db 
1.2) get all the records which satisfy date criteria 
1.3) then process these records and display them as result 
Second choice
2) do a self join operation at the database to avoid two trips to database.

I chose second option 

Improvement ( not implemented ) 
------------
There is no Index defined on the table yet fo

Phase  2 of the project : 
-------------------------
If the record is of type "Cancel" insert the quanity is negative value 
Also I noticed for such record no user name is present .

Therefore I got all users and their awards from db -  Employee ID and Employee Name map
corelated above map with the result obtained from query written for phase 1
The corelated information is the final answer


Improvement ( not implemented ) 
Before querying the database we can fix the table by adding employee name to rows where it is missing.
This way the query from phase 1 will be sufficient and no multiple trips to database is needed.






