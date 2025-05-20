PRAGMA foreign_keys = ON;

-- 1) Drop old tables if they exist locally in your IntelliJ
DROP TABLE IF EXISTS CUSTOMERS;
DROP TABLE IF EXISTS SHIPMENTS;
DROP TABLE IF EXISTS ORDERS;
DROP TABLE IF EXISTS ORDERLINEITEM;

-- 1) Create CUSTOMERS
CREATE TABLE CUSTOMERS (
                           ID            INTEGER PRIMARY KEY AUTOINCREMENT,
                           FIRSTNAME     TEXT    NOT NULL,
                           LASTNAME      TEXT    NOT NULL,
                           PASSWORD      TEXT    NOT NULL,
                           EMAIL         TEXT    UNIQUE NOT NULL,
                           STREETNUMBER  TEXT,
                           STREETNAME    TEXT,
                           SUBURB        TEXT,
                           POSTCODE      TEXT,
                           CITY          TEXT,
                           PHONENUMBER   TEXT,
                           CARDNUMBER    TEXT,
                           CVV           TEXT,
                           CARDHOLDER    TEXT,
                           EXPIRYDATE    TEXT
);

-- 3) Create ORDERS
CREATE TABLE ORDERS (
                        ORDERID       INTEGER PRIMARY KEY AUTOINCREMENT,
                        OWNER         TEXT    NOT NULL,
                        PRICE         REAL    NOT NULL,
                        STATUS        TEXT    NOT NULL,
                        PRODUCTS      TEXT,
                        QUANTITY      TEXT,
                        PURCHASEDATE  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        STREETNUMBER  TEXT,
                        STREETNAME    TEXT,
                        SUBURB        TEXT,
                        POSTCODE      TEXT,
                        CITY          TEXT,
                        CARDNUMBER    TEXT,
                        CVV           TEXT,
                        CARDHOLDER    TEXT
);

-- 4) Create SHIPMENTS
CREATE TABLE SHIPMENTS (
                           ID              INTEGER PRIMARY KEY AUTOINCREMENT,
                           ORDERID         INTEGER NOT NULL,
                           STREETNUMBER    TEXT,
                           STREETNAME      TEXT,
                           SUBURB          TEXT,
                           POSTCODE        TEXT,
                           CITY            TEXT,
                           STATUS          TEXT,
                           SHIPPINGOPTIONS TEXT,
                           FOREIGN KEY(ORDERID)
                               REFERENCES ORDERS(ORDERID)
                               ON DELETE CASCADE
);

-- 5) Insert one test order — it will get ORDERID = 1
INSERT INTO ORDERS (
    OWNER, PRICE, STATUS, PRODUCTS, QUANTITY,
    STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY,
    CARDNUMBER, CVV, CARDHOLDER
) VALUES (
             'test@domain.com',   -- OWNER
             0.00,                -- PRICE
             'Confirmed',         -- STATUS
             '',                  -- PRODUCTS
             '',                  -- QUANTITY
             '1',                 -- STREETNUMBER
             'Test Street',       -- STREETNAME
             'Test Suburb',       -- SUBURB
             '2000',              -- POSTCODE
             'Sydney',            -- CITY
             '0000111122223333',  -- CARDNUMBER
             '123',               -- CVV
             'Test User'          -- CARDHOLDER
         );
INSERT INTO ORDERS (OWNER, PRICE, STATUS, PRODUCTS, QUANTITY, PURCHASEDATE, STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY, CARDNUMBER, CVV, CARDHOLDER)
VALUES
    ('alice@example.com', 99.95, 'Saved',    '1:', '2:', '2025/05/01 10:00:00', '12', 'King St',    'Sydney', '2000', 'Sydney', '4111222233334444', '123', 'Alice Doe'),
    ('bob@example.com',   120.00, 'Submitted','2:', '3:', '2025/05/02 11:30:00', '88', 'Queen Ave',  'Melbourne', '3000', 'Melbourne', '5555666677778888', '321', 'Bob Smith'),
    ('carol@example.com', 40.00, 'Saved',    '3:', '1:', '2025/05/03 12:00:00', '7',  'George Rd',  'Brisbane', '4000', 'Brisbane', '4444333322221111', '456', 'Carol Lee'),
    ('dave@example.com',  150.50, 'Cancelled','1:2:', '1:1:', '2025/05/04 09:20:00', '34', 'Prince Ln', 'Perth', '6000', 'Perth', '3333444455556666', '789', 'Dave Chan'),
    ('eve@example.com',   200.00, 'Submitted','2:3:', '2:2:', '2025/05/05 08:15:00', '21', 'Market St','Sydney', '2000', 'Sydney', '1111222233334444', '321', 'Eve Wang'),
    ('frank@example.com', 75.00, 'Saved',    '4:', '3:', '2025/05/06 13:30:00', '56', 'Park Ave',   'Melbourne', '3000', 'Melbourne', '6666555544443333', '654', 'Frank Zhang'),
    ('gina@example.com',  60.00, 'Saved',    '1:', '2:', '2025/05/07 14:00:00', '17', 'Hill Rd',    'Brisbane', '4000', 'Brisbane', '7777888899990000', '987', 'Gina Park'),
    ('henry@example.com', 250.99, 'Submitted','2:4:', '1:3:', '2025/05/08 17:00:00', '2',  'Luna Pl',    'Perth', '6000', 'Perth', '2222333344445555', '852', 'Henry Ford'),
    ('irene@example.com', 85.60,  'Cancelled','3:', '4:', '2025/05/09 11:40:00', '19', 'Lake Dr',   'Sydney', '2000', 'Sydney', '8888999900001111', '753', 'Irene Kim'),
    ('jack@example.com',  190.20, 'Submitted','4:', '2:', '2025/05/10 10:10:00', '8',  'Main St',   'Melbourne', '3000', 'Melbourne', '9999000011112222', '159', 'Jack Black'),
    ('kate@example.com',  79.95,  'Saved',    '1:', '1:', '2025/05/11 12:45:00', '11', 'Short Rd',   'Brisbane', '4000', 'Brisbane', '1111000099998888', '753', 'Kate Moss'),
    ('luke@example.com',  130.00, 'Saved',    '2:', '3:', '2025/05/12 13:30:00', '90', 'Ring Rd',    'Perth', '6000', 'Perth', '2222111122223333', '951', 'Luke Bryan'),
    ('maya@example.com',  44.50,  'Saved',    '3:', '2:', '2025/05/13 14:25:00', '15', 'Pine St',    'Sydney', '2000', 'Sydney', '3333444411110000', '159', 'Maya Lin'),
    ('nick@example.com',  110.00, 'Cancelled','4:2:', '1:2:', '2025/05/14 15:10:00', '33', 'Elm St',   'Melbourne', '3000', 'Melbourne', '4444333311119999', '357', 'Nick Jonas'),
    ('olga@example.com',  170.75, 'Saved',    '2:3:', '2:1:', '2025/05/15 09:50:00', '6',  'Bay Rd',    'Brisbane', '4000', 'Brisbane', '5555666699998888', '456', 'Olga Roman'),
    ('paul@example.com',  200.00, 'Submitted','1:', '3:', '2025/05/16 16:00:00', '20', 'Sunset Blvd','Perth', '6000', 'Perth', '6666555544441111', '852', 'Paul Brown'),
    ('qi@example.com',    61.50,  'Saved',    '4:', '2:', '2025/05/17 12:05:00', '13', 'Beach Rd',   'Sydney', '2000', 'Sydney', '7777888800001111', '159', 'Qi Wen'),
    ('ruby@example.com',  88.80,  'Submitted','3:2:', '1:1:', '2025/05/18 13:25:00', '4',  'Stone Ave',  'Melbourne', '3000', 'Melbourne', '8888999911112222', '159', 'Ruby Singh'),
    ('sam@example.com',   134.00, 'Saved',    '1:4:', '2:2:', '2025/05/19 14:15:00', '27', 'Forest Dr',  'Brisbane', '4000', 'Brisbane', '9999000011112222', '159', 'Sam Lim'),
    ('tina@example.com',  112.00, 'Saved',    '2:', '2:', '2025/05/20 10:35:00', '25', 'Lake St',    'Perth', '6000', 'Perth', '1111222233334444', '123', 'Tina Hope');

-- Insert test customers for all emails used in ORDERS table
INSERT INTO CUSTOMERS (
    FIRSTNAME, LASTNAME, PASSWORD, EMAIL,
    STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY,
    PHONENUMBER, CARDNUMBER, CVV, CARDHOLDER, EXPIRYDATE
)
VALUES
    ('Alice', 'Doe', 'alice123', 'alice@example.com', '12', 'King St', 'Sydney', '2000', 'Sydney', '0411000001', '4111222233334444', '123', 'Alice Doe', '12/27'),
    ('Bob', 'Smith', 'bob123', 'bob@example.com', '88', 'Queen Ave', 'Melbourne', '3000', 'Melbourne', '0411000002', '5555666677778888', '321', 'Bob Smith', '10/26'),
    ('Carol', 'Lee', 'carol123', 'carol@example.com', '7', 'George Rd', 'Brisbane', '4000', 'Brisbane', '0411000003', '4444333322221111', '456', 'Carol Lee', '03/28'),
    ('Dave', 'Chan', 'dave123', 'dave@example.com', '34', 'Prince Ln', 'Perth', '6000', 'Perth', '0411000004', '3333444455556666', '789', 'Dave Chan', '06/25'),
    ('Eve', 'Wang', 'eve123', 'eve@example.com', '21', 'Market St', 'Sydney', '2000', 'Sydney', '0411000005', '1111222233334444', '321', 'Eve Wang', '01/26'),
    ('Frank', 'Zhang', 'frank123', 'frank@example.com', '56', 'Park Ave', 'Melbourne', '3000', 'Melbourne', '0411000006', '6666555544443333', '654', 'Frank Zhang', '11/29'),
    ('Gina', 'Park', 'gina123', 'gina@example.com', '17', 'Hill Rd', 'Brisbane', '4000', 'Brisbane', '0411000007', '7777888899990000', '987', 'Gina Park', '09/27'),
    ('Henry', 'Ford', 'henry123', 'henry@example.com', '2', 'Luna Pl', 'Perth', '6000', 'Perth', '0411000008', '2222333344445555', '852', 'Henry Ford', '02/28'),
    ('Irene', 'Kim', 'irene123', 'irene@example.com', '19', 'Lake Dr', 'Sydney', '2000', 'Sydney', '0411000009', '8888999900001111', '753', 'Irene Kim', '05/26'),
    ('Jack', 'Black', 'jack123', 'jack@example.com', '8', 'Main St', 'Melbourne', '3000', 'Melbourne', '0411000010', '9999000011112222', '159', 'Jack Black', '08/27'),
    ('Kate', 'Moss', 'kate123', 'kate@example.com', '11', 'Short Rd', 'Brisbane', '4000', 'Brisbane', '0411000011', '1111000099998888', '753', 'Kate Moss', '03/27'),
    ('Luke', 'Bryan', 'luke123', 'luke@example.com', '90', 'Ring Rd', 'Perth', '6000', 'Perth', '0411000012', '2222111122223333', '951', 'Luke Bryan', '10/27'),
    ('Maya', 'Lin', 'maya123', 'maya@example.com', '15', 'Pine St', 'Sydney', '2000', 'Sydney', '0411000013', '3333444411110000', '159', 'Maya Lin', '01/29'),
    ('Nick', 'Jonas', 'nick123', 'nick@example.com', '33', 'Elm St', 'Melbourne', '3000', 'Melbourne', '0411000014', '4444333311119999', '357', 'Nick Jonas', '04/28'),
    ('Olga', 'Roman', 'olga123', 'olga@example.com', '6', 'Bay Rd', 'Brisbane', '4000', 'Brisbane', '0411000015', '5555666699998888', '456', 'Olga Roman', '07/28'),
    ('Paul', 'Brown', 'paul123', 'paul@example.com', '20', 'Sunset Blvd', 'Perth', '6000', 'Perth', '0411000016', '6666555544441111', '852', 'Paul Brown', '02/27'),
    ('Qi', 'Wen', 'qi123', 'qi@example.com', '13', 'Beach Rd', 'Sydney', '2000', 'Sydney', '0411000017', '7777888800001111', '159', 'Qi Wen', '05/28'),
    ('Ruby', 'Singh', 'ruby123', 'ruby@example.com', '4', 'Stone Ave', 'Melbourne', '3000', 'Melbourne', '0411000018', '8888999911112222', '159', 'Ruby Singh', '09/29'),
    ('Sam', 'Lim', 'sam123', 'sam@example.com', '27', 'Forest Dr', 'Brisbane', '4000', 'Brisbane', '0411000019', '9999000011112222', '159', 'Sam Lim', '12/28'),
    ('Tina', 'Hope', 'tina123', 'tina@example.com', '25', 'Lake St', 'Perth', '6000', 'Perth', '0411000020', '1111222233334444', '123', 'Tina Hope', '10/26'),
    ('Test', 'User', 'test123', 'test@domain.com', '1', 'Test Street', 'Test Suburb', '2000', 'Sydney', '0411000000', '0000111122223333', '123', 'Test User', '01/27');

INSERT INTO ORDERS (
    OWNER, PRICE, STATUS, PRODUCTS, QUANTITY, PURCHASEDATE,
    STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY,
    CARDNUMBER, CVV, CARDHOLDER
) VALUES (
             'test@gmail.com', 55.00, 'Confirmed', '1:', '2:', '2025/05/21 10:00:00',
             '99', 'Testers Ave', 'Testville', '9999', 'Testcity',
             '1234567890123456', '999', 'Test User'
         );
INSERT INTO CUSTOMERS (
    FIRSTNAME, LASTNAME, PASSWORD, EMAIL,
    STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY,
    PHONENUMBER, CARDNUMBER, CVV, CARDHOLDER, EXPIRYDATE
) VALUES (
             'Test', 'User', 'test', 'test@gmail.com',
             '99', 'Testers Ave', 'Testville', '9999', 'Testcity',
             '0411999999', '1234567890123456', '999', 'Test User', '12/30'
         );
CREATE TABLE ORDERLINEITEM (
                               ID INTEGER PRIMARY KEY AUTOINCREMENT,
                               ORDERID INTEGER NOT NULL,
                               OWNER TEXT NOT NULL,
                               QUANTITY INTEGER NOT NULL,
                               TOTALCOST REAL NOT NULL
);
INSERT INTO ORDERLINEITEM (
    ORDERID, OWNER, QUANTITY, TOTALCOST
) VALUES (
             1, 'test@gmail.com', 2, 55.00
         );
-- Drop old STAFF table if exists
DROP TABLE IF EXISTS STAFF;

-- Create STAFF table
CREATE TABLE STAFF (
                       STAFFID      INTEGER PRIMARY KEY AUTOINCREMENT,
                       FIRSTNAME    TEXT NOT NULL,
                       LASTNAME     TEXT NOT NULL,
                       EMAIL        TEXT UNIQUE NOT NULL,
                       PASSWORD     TEXT NOT NULL,
                       ROLE         TEXT NOT NULL,
                       PHONENUMBER  TEXT,
                       STREETNUMBER TEXT,
                       STREETNAME   TEXT,
                       SUBURB       TEXT,
                       POSTCODE     TEXT,
                       CITY         TEXT
);

-- Insert dummy staff data
INSERT INTO STAFF (
    FIRSTNAME, LASTNAME, EMAIL, PASSWORD, ROLE, PHONENUMBER,
    STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY
) VALUES
      ('John', 'Doe', 'john.doe@example.com', 'pass123', 'Admin', '0411000001', '12', 'King St', 'Sydney', '2000', 'Sydney'),
      ('Jane', 'Smith', 'jane.smith@example.com', 'pass456', 'Support', '0411000002', '88', 'Queen Ave', 'Melbourne', '3000', 'Melbourne'),
      ('Mark', 'Taylor', 'mark.taylor@example.com', 'pass789', 'Manager', '0411000003', '7', 'George Rd', 'Brisbane', '4000', 'Brisbane'),
      ('Lucy', 'Brown', 'lucy.brown@example.com', 'passabc', 'Support', '0411000004', '34', 'Prince Ln', 'Perth', '6000', 'Perth');
