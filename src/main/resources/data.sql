DROP TABLE IF EXISTS BORROWED_BOOK;
DROP TABLE IF EXISTS WAITING_LIST;
DROP TABLE IF EXISTS USER CASCADE;
DROP TABLE IF EXISTS book CASCADE;

CREATE TABLE book (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  author VARCHAR(250) NOT NULL,
  title VARCHAR(250) NOT NULL,
  availablecopies INT NOT NULL,
  totalcopies INT NOT NULL,
  CREATED_DATE TIMESTAMP,
  UPDATED_DATE TIMESTAMP
);

INSERT INTO BOOK (Author,Title,AVAILABLECOPIES,TOTALCOPIES,CREATED_DATE,UPDATED_DATE  ) VALUES
  ('James Clear','Atomic Habits',3,3,'2020-10-12 14:01:58.752835','2020-10-12 14:01:58.753825'),
  ('Blake Masters and Peter Thiel','Zero to One',2,2,'2020-10-12 14:01:58.768786','2020-10-12 14:01:58.769784'),
  ('Paulo Coelho','The Alchemist',1,2,'2020-10-12 14:01:58.770781','2020-10-12 14:01:58.770781'),
  ('Albert Liebermann and Hector Garcia','Ikigai',4,4,'2020-10-12 14:01:58.772775','2020-10-12 14:01:58.772775'),
  ('Alex Lake','Seven Days',0,0,'2020-10-12 14:01:58.774769','2020-10-12 14:01:58.774769'),
  ('Robin Sharma','The 5AM Club',1,1,'2020-10-12 14:01:58.776764','2020-10-12 14:01:58.776764');



CREATE TABLE USER(
id Int AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(250) NOT NULL,
email VARCHAR(250) NOT NULL,
CREATED_DATE TIMESTAMP,
UPDATED_DATE TIMESTAMP
);

INSERT INTO USER (NAME,EMAIL,CREATED_DATE,UPDATED_DATE) VALUES
('Test1','test1@gmail.com','2020-10-12 14:01:58.752835','2020-10-12 14:01:58.753825'),
('Test2', 'test2@email.com','2020-10-12 14:01:58.768786','2020-10-12 14:01:58.769784'),
('Test3','test3@email.com','2020-10-12 14:01:58.770781','2020-10-12 14:01:58.770781'),
('Test4','test4@email.com','2020-10-12 14:01:58.772775','2020-10-12 14:01:58.772775');



CREATE TABLE BORROWED_BOOK(
bookId Int,
userId Int,
NO_OF_RENEWALS INT NOT NULL,
ISSUED_DATE TIMESTAMP NOT NULL,
DUE_DATE TIMESTAMP NOT NULL,
CREATED_DATE TIMESTAMP,
UPDATED_DATE TIMESTAMP,
CONSTRAINT BORROWED_BOOK_bookId_FK
    FOREIGN KEY(bookId)
    REFERENCES BOOK(ID),
CONSTRAINT BORROWED_BOOK_userId_FK
        FOREIGN KEY(userId)
        REFERENCES USER(ID),
   CONSTRAINT  BORROWED_BOOK_PK PRIMARY KEY(bookId,userId)
);
CREATE TABLE WAITING_LIST(
id Int AUTO_INCREMENT PRIMARY KEY,
BOOK_ID Int,
USER_ID Int,
CREATED_DATE TIMESTAMP,
 CONSTRAINT WAITING_LIST_bookId_FK
     FOREIGN KEY(BOOK_ID)
     REFERENCES BOOK(ID),
 CONSTRAINT WAITING_LIST_userId_FK
         FOREIGN KEY(USER_ID)
         REFERENCES USER(ID)
);
