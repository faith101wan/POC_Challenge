# Minimal Java POC - OMS/EMS Reconciliation

这是一个可运行的 Spring Boot 最小 POC 骨架，聚焦核心业务闭环：
- 接收 OMS 订单事件
- 接收 EMS 成交事件
- 输出对账结果：`MATCHED / MISSING_EXECUTION / DUPLICATE_EXECUTION`

## Tech Stack
- Java 21
- Spring Boot 3
- Maven
- In-memory state (`ConcurrentHashMap`)

## Run Locally
```bash
mvn spring-boot:run
```

## Run Tests
```bash
mvn test
```

## API
### 1) Submit OMS order
`POST /api/oms/events`

```json
{
  "orderId": "O-1001",
  "accountId": "A-001",
  "quantity": 100
}
```

### 2) Submit EMS execution
`POST /api/ems/events`

```json
{
  "execId": "E-9001",
  "orderId": "O-1001",
  "accountId": "A-001",
  "quantity": 100
}
```

### 3) Query results
`GET /api/reconciliation/results`

## Quick Verification
```bash
curl -X POST http://localhost:8080/api/oms/events \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"O-1001","accountId":"A-001","quantity":100}'

curl -X POST http://localhost:8080/api/ems/events \
  -H 'Content-Type: application/json' \
  -d '{"execId":"E-9001","orderId":"O-1001","accountId":"A-001","quantity":100}'

curl http://localhost:8080/api/reconciliation/results
```

## Docker
```bash
docker build -t poc-reconciliation .
docker run --rm -p 8080:8080 poc-reconciliation
```
