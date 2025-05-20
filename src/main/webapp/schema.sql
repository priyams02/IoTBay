PRAGMA foreign_keys = ON;

-- 1) Drop old tables if they exist locally in your IntelliJ
DROP TABLE IF EXISTS CUSTOMERS;
DROP TABLE IF EXISTS SHIPMENTS;
DROP TABLE IF EXISTS ORDERS;

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