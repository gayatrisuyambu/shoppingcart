# Shopping Cart Assessment

This repository implements the **Shopping Cart coding challenge** in three parts:

1. **Java Core (Spring Boot 3.5.5)** – core business logic & pricing rules  
2. **JS API (Node + Express)** – frontend-friendly REST API that wraps the Java backend  
3. **Python Utility** – reporting script for analyzing shopping carts from CSV  

---

## 📖 Features
- Pricing rules:
  - Apple → 35p each  
  - Banana → 20p each  
  - Melon → 50p each (**Buy One Get One Free**)  
  - Lime → 15p each (**3 for 2**)  
- REST APIs in Java & Node  
- Input validation & error handling  
- Python script for batch reports  
- Unit + integration tests (JUnit, MockMvc)  

---

## 📂 Repository Structure
```
shopping-cart/
  java-core/        # Spring Boot project
  js-api/           # Node/Express API
  tools-python/     # Python reporting utility
  README.md
```

---

## ⚙️ Setup & Run

### 1. Java Core (Spring Boot)
```bash
cd java-core
mvn clean package
mvn spring-boot:run
```

Runs at [http://localhost:8080](http://localhost:8080).

**Endpoints**
- Health: `GET /api/health` → `OK`
- Pricing: `POST /api/price`  
  Request:
  ```json
  {"items":["Apple","Melon","Melon","Lime","Lime","Lime"]}
  ```
  Response:
  ```json
  {
    "itemQty": {"Apple":1,"Melon":2,"Lime":3},
    "totalPerItem": {"Apple":35,"Melon":50,"Lime":30},
    "totalPrice": 115
  }
  ```

**Tests**
```bash
mvn test
```

---

### 2. JS API (Node.js + Express)
```bash
cd js-api
npm install
npm run start
```

Runs at [http://localhost:3000](http://localhost:3000).

**Endpoints**
- `POST /api/cart/items` – add item  
  ```bash
  curl -X POST :3000/api/cart/items -H 'Content-Type: application/json' -d '{"item":"Apple"}'
  ```
- `GET /api/cart` – view cart
- `GET /api/cart/total` – calculate total (calls Java backend)  
  ```json
  {
    "items":["Apple","Melon","Melon"],
    "breakdown": {...},
    "totalPence": 85,
    "totalGBP": "0.85"
  }
  ```

**Security**
- API key required in header:  
  ```
  X-API-Key: secret123
  ```
- Set your key in `.env`:
  ```
  API_KEY=secret123
  ```

---

### 3. Python Reporting Tool
```bash
cd tools-python
pip3 install -r requirements.txt
```

**Run locally (no Java call):**
```bash
python3 report.py --input sample_carts.csv
```

**Run with Java backend:**
```bash
python3 report.py --input sample_carts.csv --java-base http://localhost:8080/api
```

**Sample CSV**
```csv
client_id,item
alice,Apple
alice,Melon
bob,Lime
carol,Dragonfruit
```

**Sample Output**
```json
{
  "clients": {
    "alice": { "totalPrice": 85, "totalGBP": "0.85" },
    "bob":   { "totalPrice": 30, "totalGBP": "0.30" },
    "carol": { "invalidItems": ["Dragonfruit"], "totalPrice": 20 }
  },
  "totals": {
    "grandPence": 135,
    "grandGBP": "1.35"
  }
}
```

---

## 🧪 Tests
- Java: `mvn test` (unit + controller tests)  
- Node: use `curl` or Postman for manual validation  
- Python: verify JSON output matches expected totals  

---

## 📌 Notes
- Java is the **source of truth** for pricing (Python & JS can proxy to it).  
- All totals use **pence** internally → no floating-point errors.  
- Both Java & Node layers validate input; invalid items produce `400 Bad Request`.  

---
