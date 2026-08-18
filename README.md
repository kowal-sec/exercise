# Journey Cost Calculator

Spring Boot REST API calculating the cost of a vehicle journey, with a
tiered pricing rule and a customer-specific discount.

## Stack

- Java 21
- Spring Boot 4.1
- Maven
- JUnit 5, Mockito, MockMvc

## Running

```
./mvnw spring-boot:run
```

## Testing

```
./mvnw clean test
```

## API

`POST /api/journeys/cost`

Request:

```json
{
  "distanceKm": 200,
  "costPerKm": 0.25,
  "customerId": "TransX"
}
```

`customerId` is optional. Any value other than `"TransX"` (exact match,
case-sensitive) is treated as a regular customer.

Response:

```json
{
  "totalCost": 45.13
}
```

## Pricing rules

1. **Tiered distance rate** (`JourneyPricingService`): the first 100 km are
   charged at `costPerKm`. Any distance beyond 100 km is charged at a 10%
   discount. The two tiers are computed independently and summed, so the
   rule holds regardless of total distance (no cliff effect at the
   threshold).

2. **TransX customer discount** (`JourneyDiscountService`): a further 5%
   discount is applied to the *final* cost from step 1, only when
   `customerId` equals `"TransX"` exactly. This is a separate service
   because it operates on total cost regardless of distance structure —
   a different concern from the tiered rate, and a natural seam for future
   customer-specific or promotional discounts.

Both discounts compound: a TransX journey over 100 km receives the tiered
discount on the excess distance, then the flat 5% on the resulting total.

## Design decisions and assumptions

- **Money as `BigDecimal`**, rounded to 2 decimal places with `HALF_UP`,
  applied once per service after all arithmetic. `double` was avoided
  deliberately to prevent floating-point rounding errors on currency.
- **No currency field.** The exercise doesn't specify one; the API assumes
  a single implicit currency.
- **Negative or missing `distanceKm`/`costPerKm` return `400`** via Bean
  Validation on the request DTO. This validates the HTTP boundary.
- **Services validate their own inputs independently of the controller**
  (negative/null checks throw `IllegalArgumentException`), on the
  assumption they may be called from contexts other than this controller
  in the future (batch jobs, other endpoints). This is intentionally
  separate from — and duplicates — the DTO validation, since DTOs only
  protect the HTTP entry point, not direct method calls.
- **No custom `@ExceptionHandler`.** Validation failures currently return
  Spring's default 400 body. A structured error response
  (`@ControllerAdvice`) would be a natural next step but is out of scope
  for the stated requirements.
- **`customerId` matching is exact and case-sensitive.** `"transx"` or
  `"TRANSX"` do not qualify. Covered explicitly by a test rather than
  left implicit.

## Test coverage

- `JourneyPricingServiceTest` — tiered pricing rule, boundaries around the
  100 km threshold, rounding, negative-input guards.
- `JourneyDiscountServiceTest` — TransX discount, non-TransX/null/blank
  customer IDs, case sensitivity, negative/null-input guards.
- `JourneyControllerTest` — HTTP layer in isolation (mocked services):
  request validation, response shape.
- `JourneyEndToEndTest` — full context, real services, confirms the worked
  example from the spec (200 km @ €0.25/km → €47.50, and €45.13 with the
  TransX discount applied).
