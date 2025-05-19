--Script to set up database

-- Enable foreign key support
PRAGMA foreign_keys = ON;

-- USERS TABLE (Unified for Customers and Staff)
CREATE TABLE IF NOT EXISTS USERS (
                                     ID         INTEGER PRIMARY KEY AUTOINCREMENT,
                                     EMAIL      TEXT UNIQUE NOT NULL,
                                     PASSWORD   TEXT NOT NULL,
                                     FIRSTNAME  TEXT NOT NULL,
                                     LASTNAME   TEXT NOT NULL,
                                     USERTYPE   TEXT CHECK (USERTYPE IN ('CUSTOMER', 'STAFF')) NOT NULL,
                                     PHONENUMBER TEXT
);

-- ADDRESSES TABLE
CREATE TABLE IF NOT EXISTS ADDRESSES (
                                         ID            INTEGER PRIMARY KEY AUTOINCREMENT,
                                         STREETNUMBER  TEXT,
                                         STREETNAME    TEXT,
                                         SUBURB        TEXT,
                                         POSTCODE      TEXT,
                                         CITY          TEXT
);

-- PAYMENT INFORMATION TABLE
CREATE TABLE IF NOT EXISTS PAYMENT_INFO (
                                            ID          INTEGER PRIMARY KEY AUTOINCREMENT,
                                            CARDNUMBER  TEXT,
                                            CVV         TEXT,
                                            CARDHOLDER  TEXT,
                                            EXPIRYDATE  TEXT
);

-- CUSTOMER PROFILES (Linked to USER accounts)
CREATE TABLE IF NOT EXISTS CUSTOMER_PROFILES (
                                                 USERID         INTEGER PRIMARY KEY,
                                                 ADDRESS_ID     INTEGER,
                                                 PAYMENT_ID     INTEGER,
                                                 FOREIGN KEY (USERID) REFERENCES USERS(ID) ON DELETE CASCADE,
                                                 FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESSES(ID),
                                                 FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENT_INFO(ID)
);

-- PRODUCTS
CREATE TABLE IF NOT EXISTS PRODUCTS (
                                        PRODUCTID   TEXT PRIMARY KEY,
                                        PRODUCTNAME TEXT NOT NULL,
                                        CATEGORY    TEXT,
                                        PRICE       REAL NOT NULL,
                                        STOCK       INTEGER NOT NULL
);

-- ORDERLINEITEM (Shopping Cart)
CREATE TABLE IF NOT EXISTS ORDERLINEITEM (
                                             USERID     INTEGER NOT NULL,
                                             PRODUCTID  TEXT    NOT NULL,
                                             QUANTITY   INTEGER NOT NULL,
                                             PRIMARY KEY (USERID, PRODUCTID),
                                             FOREIGN KEY (USERID) REFERENCES USERS(ID) ON DELETE CASCADE,
                                             FOREIGN KEY (PRODUCTID) REFERENCES PRODUCTS(PRODUCTID)
);

-- ORDERS
CREATE TABLE IF NOT EXISTS ORDERS (
                                      ORDERID       INTEGER PRIMARY KEY AUTOINCREMENT,
                                      USERID        INTEGER NOT NULL,
                                      PRICE         REAL    NOT NULL,
                                      STATUS        TEXT    NOT NULL,
                                      PRODUCTS      TEXT,
                                      QUANTITY      TEXT,
                                      PURCHASEDATE  DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      ADDRESS_ID    INTEGER,
                                      PAYMENT_ID    INTEGER,
                                      FOREIGN KEY (USERID)     REFERENCES USERS(ID),
                                      FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESSES(ID),
                                      FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENT_INFO(ID)
);

-- SHIPMENTS
CREATE TABLE IF NOT EXISTS SHIPMENTS (
                                         ID              INTEGER PRIMARY KEY AUTOINCREMENT,
                                         ORDERID         INTEGER NOT NULL,
                                         ADDRESS_ID      INTEGER,
                                         STATUS          TEXT,
                                         SHIPPINGOPTIONS TEXT,
                                         FOREIGN KEY (ORDERID)    REFERENCES ORDERS(ORDERID) ON DELETE CASCADE,
                                         FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESSES(ID)
);

-- ACCESS LOG (Login/Logout Auditing)
CREATE TABLE IF NOT EXISTS ACCESS_LOG (
                                          LOGID     INTEGER PRIMARY KEY AUTOINCREMENT,
                                          USERID    INTEGER NOT NULL,
                                          ACTION    TEXT NOT NULL, -- LOGIN / LOGOUT
                                          TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          FOREIGN KEY (USERID) REFERENCES USERS(ID)
);
