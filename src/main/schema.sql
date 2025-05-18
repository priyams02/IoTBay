-- Enable foreign‐key support
PRAGMA foreign_keys = ON;

-- CUSTOMERS
CREATE TABLE IF NOT EXISTS CUSTOMERS (
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

-- STAFF
CREATE TABLE IF NOT EXISTS STAFF (
                                     EMAIL     TEXT    PRIMARY KEY,
                                     FIRSTNAME TEXT    NOT NULL,
                                     LASTNAME  TEXT    NOT NULL,
                                     PASSWORD  TEXT    NOT NULL
);

-- PRODUCTS
CREATE TABLE IF NOT EXISTS PRODUCTS (
                                        PRODUCTID   TEXT    PRIMARY KEY,
                                        PRODUCTNAME TEXT    NOT NULL,
                                        CATEGORY    TEXT,
                                        PRICE       REAL    NOT NULL,
                                        STOCK       INTEGER NOT NULL
);

-- ORDERLINEITEM (Cart)
CREATE TABLE IF NOT EXISTS ORDERLINEITEM (
                                             OWNER     TEXT    NOT NULL,
                                             PRODUCTID TEXT    NOT NULL,
                                             QUANTITY  INTEGER NOT NULL,
                                             PRIMARY KEY (OWNER, PRODUCTID),
                                             FOREIGN KEY (OWNER)     REFERENCES CUSTOMERS(EMAIL)   ON DELETE CASCADE,
                                             FOREIGN KEY (PRODUCTID) REFERENCES PRODUCTS(PRODUCTID)
);
CREATE TABLE IF NOT EXISTS ORDERS (
                                      ORDERID       INTEGER  PRIMARY KEY AUTOINCREMENT,
                                      OWNER         TEXT     NOT NULL,
                                      PRICE         REAL     NOT NULL,
                                      STATUS        TEXT     NOT NULL,
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
                                      CARDHOLDER    TEXT,
                                      FOREIGN KEY (OWNER) REFERENCES CUSTOMERS(EMAIL)
);

-- SHIPMENTS
CREATE TABLE IF NOT EXISTS SHIPMENTS (
                                         ID              INTEGER PRIMARY KEY AUTOINCREMENT,
                                         ORDERID         INTEGER NOT NULL,
                                         STREETNUMBER    TEXT,
                                         STREETNAME      TEXT,
                                         SUBURB          TEXT,
                                         POSTCODE        TEXT,
                                         CITY            TEXT,
                                         STATUS          TEXT,
                                         SHIPPINGOPTIONS TEXT,
                                         FOREIGN KEY (ORDERID) REFERENCES ORDERS(ORDERID) ON DELETE CASCADE
);

-- ACCESS_LOG (Login/Logout auditing)
CREATE TABLE IF NOT EXISTS ACCESS_LOG (
                                          LOGID     INTEGER  PRIMARY KEY AUTOINCREMENT,
                                          EMAIL     TEXT     NOT NULL,
                                          ACTION    TEXT     NOT NULL,        -- e.g. 'LOGIN' or 'LOGOUT'
                                          TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          FOREIGN KEY (EMAIL) REFERENCES CUSTOMERS(EMAIL)
);
