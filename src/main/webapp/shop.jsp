<%--
  Created by IntelliJ IDEA.
  User: DELBERT
  Date: 25/03/2025
  Time: 20:08
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IoTBay - Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        /* Basic Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            padding: 20px;
            color: #333;
        }

        header {
            background-color: #333;
            color: #fff;
            padding: 20px 10px;
            text-align: center;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 10px;
        }

        h2 {
            margin: 20px 0;
            text-align: center;
        }

        .product-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .product {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 5px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .product img {
            max-width: 100%;
            height: auto;
            border-bottom: 1px solid #eee;
            margin-bottom: 10px;
        }

        .product h3 {
            margin: 10px 0;
            font-size: 1.2em;
        }

        .product p {
            font-size: 0.95em;
            color: #555;
        }

        .product button {
            background-color: #28a745;
            color: #fff;
            border: none;
            padding: 10px 15px;
            margin-top: 10px;
            border-radius: 3px;
            cursor: pointer;
        }

        .product button:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>

<header>
    <h1>IoTBay</h1>
    <p>Your one-stop shop for IoT products</p>
</header>

<div class="container">
    <h2>Featured Products</h2>
    <div class="product-list">
        <!-- Product 1 -->
        <div class="product">
            <img src="product1.jpg" alt="Smart Sensor">
            <h3>Smart Sensor</h3>
            <p>High precision sensor for your IoT applications.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 2 -->
        <div class="product">
            <img src="product2.jpg" alt="IoT Hub">
            <h3>IoT Hub</h3>
            <p>Centralized device management and connectivity.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 3 -->
        <div class="product">
            <img src="product3.jpg" alt="Smart Light">
            <h3>Smart Light</h3>
            <p>Adjustable smart lighting for energy savings.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 4 -->
        <div class="product">
            <img src="product4.jpg" alt="Security Camera">
            <h3>Security Camera</h3>
            <p>High definition camera with remote monitoring.</p>
            <button>Add to Cart</button>
        </div>
    </div>
</div>

</body>
</html>

