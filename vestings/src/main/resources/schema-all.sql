DROP TABLE VESTING_SCHEDULE IF EXISTS;

CREATE TABLE VESTING_SCHEDULE  (
	ID INTEGER  auto_increment NOT NULL PRIMARY KEY,
	EVENT_TYPE VARCHAR(20),
	EMPLOYEE_ID VARCHAR(20),
	EMPLOYEE_NAME VARCHAR(100),
	AWARD_ID VARCHAR(20),
	AWARD_DATE INTEGER,
	QUANTITY INTEGER
);
