## INTRODUCTION TO SOFTWARE DEVELOPMENT R1 (41025)

DEVELOPED BY:

	BRYCE KOH ZHENG RONG | 14516126
	RICHARD HALIM |   
	PRIYAM |  
	DELBERT CHARLIE |   
	DAVID |   	
	 |   

## IoTBAY E-COMMERCE WEBSITE
This project was developed in accordance with the specifications set by the University of Technology Sydney for the subject Introduction to Software Development (41025).

IoTBay is an Internet of Things (IoT) store that offers a variety of devices such as sensors, actuators, and gateways to customers across Australia. Previously, IoTBay lacked an online platform for customers to browse and purchase these products.

To address this, a web-based application was developed to enable customers to conveniently access product information, manage orders and deliveries, register accounts, and make secure online payments. The system also includes administrative functionality, allowing staff members to manage and maintain the IoTBay platform.

## ✅ HOW TO RUN

1. **Download or clone this repository.**
2. **Unzip the source code (if downloaded as a ZIP).**
3. **Open IntelliJ IDEA.**
4. Go to **File → Open**, then navigate to the unzipped project folder and open it.
5. Wait for IntelliJ to load all Maven dependencies (if applicable).
6. **Configure Tomcat Server:**
    - Go to **Run → Edit Configurations...**
    - Click the **`+`** button and choose **Tomcat Server → Local**.
    - Name the configuration (e.g., `IoTBay`).
    - Set the **Tomcat Home** to your installed Apache Tomcat directory.
    - Set the **Deployment** to `artifact: IoTBay:war exploded` (or similar, depending on your module setup).
7. **Set the project SDK:**
    - Go to **File → Project Structure → Project**.
    - Set the correct **Project SDK** (e.g., Java 11 or 17).
    - Under **Modules**, ensure the language level and output paths are set correctly.
8. **Ensure `web.xml` is present** under `src/main/webapp/WEB-INF/`.
9. **Ensure the SQLite JDBC driver is added:**
    - Add the dependency to your `pom.xml`:
      ```xml
      <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.42.0.0</version>
      </dependency>
      ```
    - Or manually add the JAR to your `lib` folder and mark it as a library.
10. **Run the project:**
    - Select the configured Tomcat server.
    - Click **Run** ▶ or press **Shift + F10**.
11. Visit `http://localhost:8080/IoTBay` (or your configured context path) in your browser.