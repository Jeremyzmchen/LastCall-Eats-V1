## Collaborators

Weisi Chen · Zhaoming Chen · Chanyuan Liu · Yun Xin · Xinhong Zheng

# LastCall Eats

A mobile marketplace that reduces food waste by connecting restaurants and bakeries with consumers at the end of the day. Merchants list their end-of-day surplus food at a discount; users reserve, pay, and pick up before closing time.

## Architecture

**Modular monolith** built with Spring Boot 3 on Java 21. Each business domain is an isolated Maven module under a single runnable application (`lastcall-api`).

| Module | Responsibility |
|---|---|
| `lastcall-common` | Shared utilities: `ApiResponse`, exceptions, provider interfaces, storage strategy |
| `lastcall-auth` | Registration, login, JWT issuance and validation |
| `lastcall-user` | User profiles, avatars, favourites |
| `lastcall-merchant` | Merchant profiles, dashboard |
| `lastcall-product` | Product templates (reusable definitions) and daily listings |
| `lastcall-order` | Order lifecycle, pickup code generation and verification |
| `lastcall-payment` | Stripe integration (payment intents + webhooks) |
| `lastcall-review` | Post-purchase ratings and community posts |
| `lastcall-api` | Runnable Spring Boot app that composes all modules |
| `lastcall-frontend` | React Native app (Expo) |

## Tech stack

- **Backend:** Java 21, Spring Boot 3.2.5, Spring Data JPA, Spring Security + JWT (JJWT 0.12.6), Stripe Java SDK, MinIO, Lombok, Springdoc OpenAPI
- **Frontend:** React Native, Expo
- **Database:** MySQL 8
- **Build:** Maven multi-module

## Getting started

### Prerequisites
- Java 21, Maven, MySQL 8, Node.js 18+, IntelliJ IDEA (recommended)

### Backend
1. Create the database and load seed data:
   ```bash
   mysql -u root -p < sql/schema.sql
   mysql -u root -p lastcall_eats < sql/seed.sql
   ```
2. Open the project in IntelliJ. Update the DB password in `lastcall-api/src/main/resources/application-dev.yml` if needed.
3. Run `lastcall-api/src/main/java/com/lastcalleats/LastCallEatsApplication.java`. The server starts on `http://localhost:8080`.
4. API docs: `http://localhost:8080/swagger-ui.html`

### Frontend
```bash
cd lastcall-frontend/lastcall-app
npm install
npm run dev        # auto-detects local IP; scan the QR with Expo Go
```
Phone and dev machine must be on the same Wi-Fi.

## Test accounts

All passwords: `111111`

| Role | Email | Name |
|---|---|---|
| User | alice@example.com | Alice |
| User | bob@example.com | Bob |
| Merchant | bakery@example.com | Golden Bakery |
| Merchant | sushi@example.com | Sakura Sushi |
| Merchant | cafe@example.com | Brew & Bite Cafe |

**Stripe test card token:** `pm_card_visa`

## Core workflow

1. Merchant defines a **product template** (name, description, original price) — one-time setup.
2. Merchant publishes a daily **listing** from a template (discount price, quantity, pickup window, date).
3. User browses the feed, favourites and reserves a listing.
4. User pays with Stripe test mode → order status transitions to `PAID`.
5. A 6-digit pickup code and QR code are generated for the order.
6. At pickup, the merchant verifies the code → order status transitions to `COMPLETED`.

## Key design patterns

- **Strategy** — `StorageStrategy` and `PaymentStrategy` make image storage and payment providers swappable.
- **Facade** — `DashboardFacade` aggregates data across order and product modules; `WebhookFacade` decouples controllers from Stripe SDK internals.
- **State** — `OrderState` manages the `PENDING_PAYMENT → PAID → COMPLETED` lifecycle, with a `CANCELLED` branch on payment failure.
- **Factory** — `PickupCodeFactory` selects between numeric and QR code generators.
- **Provider** — `ListingStatsProvider` / `OrderStatsProvider` expose cross-module data without leaking entities.

## Project structure

```
LastCall-Eats-V1/
├── lastcall-api/          # Runnable entry point
├── lastcall-common/       # Shared code
├── lastcall-auth/         # + 7 other business modules
├── ...
├── lastcall-frontend/
│   └── lastcall-app/      # Expo React Native app
├── sql/
│   ├── schema.sql
│   └── seed.sql
├── start.md               # Setup guide (Chinese)
├── test.md                # Functional test checklist
└── pom.xml                # Parent POM
```

