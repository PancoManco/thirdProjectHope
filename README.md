# Currency exchanger

## 🧱Built with
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Java-Servlet](https://img.shields.io/badge/Java%20SERVLET-003545?style=for-the-badge&logo=openjdk&logoColor=white) ![JDBC](https://img.shields.io/badge/JDBC-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)  ![APACHE MAVEN](https://img.shields.io/badge/Apache%20Maven-blue?style=for-the-badge&logo=apachemaven&logoSize=auto&color=%23C71A36) ![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge&logo=postgresql&logoColor=white)

## 📋Application features 
* Add New Currencies: Easily add new currencies by specifying their code (e.g., USD), name (e.g., US Dollar), and symbol(e.g.,$).

* Manage Exchange Rates: View current exchange rates between currency pairs and update rates as market conditions change.

* Currency Conversion: Convert any amount from one currency to another using the latest exchange rates, allowing quick and accurate calculations.

# 🌐 REST API Endpoints
>`GET /currencies` - returns list of all currencies

```json
{
  "id": 0,
  "name": "United States dollar",
  "code": "USD",
  "sign": "$"
}
```

>`GET /currency/EUR` - returns a specific currency
```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```
>`POST /currencies` - adding a new currency to the database.
The data is sent in the request body as form fields (x-www-form-urlencoded). The form fields are **name, code, and sign**.
```json
{
  "id": 2,
  "name": "British Pound",
  "code": "GBP",
  "sign": "£"
}
```
>`GET /exchangeRates` - returns list of all exchange rates
```json
[
  {
    "id": 0,
    "baseCurrency": {
      "id": 0,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 1,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 0.99
  }
]
```
>`GET /exchangeRate/USDRUB` - - returns a specific exchange rate.
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```
>`POST /exchangeRates` - adding a new exchange rate to the database.
>The data is sent in the request body as form fields (x-www-form-urlencoded). The form fields are **baseCurrencyCode, targetCurrencyCode, and rate**.
>Example form fields:
>baseCurrencyCode - USD
>targetCurrencyCode - EUR
>rate - 0.99
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```
>`PATCH /exchangeRate/USDRUB` -  updating an existing exchange rate in the database.
>The currency pair is specified by consecutive currency codes in the request URL. The data is sent in the request body as form fields (x-www-form-urlencoded).
>The only form field is **rate**.
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```
>`GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT` - calculating the conversion of a specific amount of funds from one currency to another.
```json
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}
```
# 🚀 Running the Project on Local Tomcat
## ⚙️ Requirements
* Java JDK 11 or newer
* Apache Tomcat (e.g., version 9 or 10)
* Git
* Maven (if the project uses Maven)

## 🛠️ Steps to Run
1. **Clone the Repository**

Clone the project from GitHub to your local machine:
```bash
git clone https://github.com/your-username/your-project.git
cd your-project
```

2. **Build the Project**
If your project uses Maven, build the project with:
```bash
mvn clean package
```
This will generate a .war file inside the target/ directory

3. **Deploy the .war File to Tomcat**
* Copy the generated .war file from target/ into the webapps folder of your local Tomcat installation.
Example:
```bash
cp target/your-project.war /path/to/tomcat/webapps/
```
4. **Start Tomcat**
Start your Tomcat server:
On Linux/macOS:
```bash
/path/to/tomcat/bin/startup.sh
```
On Windows:
```bash
\path\to\tomcat\bin\startup.bat
```
5. **Access the Application**
Open your browser and navigate to:
```bash
http://localhost:8080/your-project/
```
Replace your-project with the name of your .war file (without the .war extension).

