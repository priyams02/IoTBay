-- USERS (3 customers, 2 staff)
INSERT INTO USERS (EMAIL, PASSWORD, FIRSTNAME, LASTNAME, USERTYPE, PHONENUMBER) VALUES
                                                                                    ('alice@example.com', 'pass123', 'Alice', 'Anderson', 'CUSTOMER', '0412345678'),
                                                                                    ('bob@example.com',   'securepass', 'Bob', 'Brown', 'CUSTOMER', '0423456789'),
                                                                                    ('carol@example.com', 'hello123', 'Carol', 'Clark', 'CUSTOMER', '0434567890'),
                                                                                    ('dave@company.com',  'admin1', 'Dave', 'Davis', 'STAFF', NULL),
                                                                                    ('eve@company.com',   'admin2', 'Eve', 'Evans', 'STAFF', NULL);

-- ADDRESSES
INSERT INTO ADDRESSES (STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY) VALUES
                                                                             ('101', 'Main St', 'Suburbia', '2000', 'Sydney'),
                                                                             ('202', 'King St', 'Central', '3000', 'Melbourne'),
                                                                             ('303', 'Queen Rd', 'Northside', '4000', 'Brisbane'),
                                                                             ('404', 'George St', 'Westend', '5000', 'Adelaide'),
                                                                             ('505', 'Elizabeth St', 'Eastbay', '6000', 'Perth');

-- PAYMENT_INFO
INSERT INTO PAYMENT_INFO (CARDNUMBER, CVV, CARDHOLDER, EXPIRYDATE) VALUES
                                                                       ('4111111111111111', '123', 'Alice Anderson', '12/25'),
                                                                       ('4222222222222222', '456', 'Bob Brown', '11/24'),
                                                                       ('4333333333333333', '789', 'Carol Clark', '10/26'),
                                                                       ('4444444444444444', '111', 'Test Holder 1', '01/28'),
                                                                       ('4555555555555555', '222', 'Test Holder 2', '03/27');

-- CUSTOMER_PROFILES (linking to USERS by USERID)
-- Assuming USERIDs are auto-incremented from 1
INSERT INTO CUSTOMER_PROFILES (USERID, ADDRESS_ID, PAYMENT_ID) VALUES
                                                                   (1, 1, 1),
                                                                   (2, 2, 2),
                                                                   (3, 3, 3);

-- PRODUCTS
INSERT INTO PRODUCTS (PRODUCTID, PRODUCTNAME, CATEGORY, PRICE, STOCK) VALUES
                                                                          ('P001', 'Smart Watch', 'Electronics', 199.99, 25),
                                                                          ('P002', 'Wireless Mouse', 'Electronics', 49.95, 100),
                                                                          ('P003', 'Bluetooth Speaker', 'Electronics', 89.00, 60),
                                                                          ('P004', 'USB-C Cable', 'Accessories', 9.99, 200),
                                                                          ('P005', 'Gaming Keyboard', 'Gaming', 129.50, 40);

-- ORDERLINEITEM (simulate cart or order items)

-- ORDERS (assumes 3 customers with IDs 1–3)
INSERT INTO ORDERS (USERID, PRICE, STATUS, PRODUCTS, QUANTITY, ADDRESS_ID, PAYMENT_ID) VALUES
                                                                                           (1, 299.93, 'Completed', 'P001,P002', '1,2', 1, 1),
                                                                                           (2, 89.00, 'Shipped', 'P003', '1', 2, 2),
                                                                                           (3, 178.45, 'Processing', 'P004,P005', '5,1', 3, 3),
                                                                                           (1, 49.95, 'Pending', 'P002', '1', 1, 1),
                                                                                           (2, 129.50, 'Cancelled', 'P005', '1', 2, 2);

-- SHIPMENTS
INSERT INTO SHIPMENTS (ORDERID, ADDRESS_ID, STATUS, SHIPPINGOPTIONS) VALUES
                                                                         (1, 1, 'Delivered', 'Express'),
                                                                         (2, 2, 'In Transit', 'Standard'),
                                                                         (3, 3, 'Preparing', 'Express');

-- ACCESS LOG (Login/Logout entries for users)
INSERT INTO ACCESS_LOG (USERID, ACTION) VALUES
                                            (1, 'LOGIN'),
                                            (1, 'LOGOUT'),
                                            (2, 'LOGIN'),
                                            (3, 'LOGIN'),
                                            (3, 'LOGOUT');
