# Spring Boot REST bridge (Java 17, Maven)

Aplikacja udostępnia prosty REST API, które korzysta z wygenerowanego klienta OpenAPI do komunikacji z Fakturownią.

## Wymagania
- Java 17+
- Maven 3.9+

## Konfiguracja
Zmienne środowiskowe:
- `FAKTUROWNIA_BASE_URL` (np. `https://twojaDomena.fakturownia.pl`)
- `FAKTUROWNIA_API_TOKEN`

## Uruchomienie
```bash
mvn spring-boot:run
```


## Swagger / OpenAPI UI
Po uruchomieniu aplikacji:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Dokumentacja zawiera opisy endpointów, request/response i pola DTO.

## Endpointy

### 1) Utworzenie faktury
`POST /api/invoices`

Przykładowe body:
```json
{
  "sellerName": "Wystawca Sp. z o.o.",
  "sellerTaxNo": "6272616681",
  "buyerName": "Klient Sp. z o.o.",
  "buyerTaxNo": "6272616681",
  "positions": [
    {
      "name": "Usługa",
      "quantity": 1,
      "tax": "23",
      "totalPriceGross": 123.00
    }
  ]
}
```

### 2) Status faktury + status KSeF
`GET /api/invoices/{invoiceId}/status`

### 3) Pobranie faktury PDF
`GET /api/invoices/{invoiceId}/pdf`

## Wspólny format odpowiedzi
```json
{
  "fakturowniaInvoiceId": 123,
  "ksefInvoiceId": "5213704420-20260215-ABC123DEF456",
  "invoiceStatus": "issued",
  "ksefStatus": "ok"
}
```

> `ksefInvoiceId` i `ksefStatus` mogą być `null`, jeśli faktura nie została jeszcze wysłana/przetworzona w KSeF.


## Testy (Spock + Groovy)
Testy kontrolera znajdują się w:
- `src/test/groovy/pl/fakturownia/bridge/api/InvoiceControllerIntegrationSpec.groovy`

Uruchamianie:
```bash
mvn test
```
