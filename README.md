# CineBook
Full-stack movie ticket booking starter: Java 21 + Spring Boot 3 + PostgreSQL + React/Vite.

## Run locally
1. Install Java 21, Maven, Node 20 and Docker.
2. `docker compose up -d db` from project root.
3. `cd backend && mvn spring-boot:run`
4. In another terminal: `cd frontend && cp .env.example .env && npm install && npm run dev`
5. Open http://localhost:5173. API docs: http://localhost:8080/swagger-ui.html

Demo admin: `admin@cinebook.local` / `Admin@12345` (created on first run).
Demo customer: register through the UI.

## Deploy
**Render:** Create PostgreSQL. Create a Web Service with root `backend`, build `mvn clean package -DskipTests`, start `java -jar target/cinebook-1.0.0.jar`. Set `SPRING_DATASOURCE_URL` (JDBC URL), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET` (32+ random bytes), `APP_CORS_ORIGINS` (your Vercel URL), `APP_ADMIN_EMAIL`, `APP_ADMIN_PASSWORD`. Render injects PORT.
**Vercel:** Import repo, root directory `frontend`, build `npm run build`, output `dist`; set `VITE_API_URL=https://YOUR-RENDER-URL.onrender.com/api`. `vercel.json` handles SPA routing.

## Included
Customer registration/login, movie catalog/search, movie detail, shows, seat map, booking and booking history. Admin dashboard, movie creation, show creation, theatre screens and booking overview. PostgreSQL schema and demo seed.

## Important production work
The included checkout uses a DEMO payment record and immediate confirmation. Do not accept real money until Razorpay order creation, server-side signature verification and webhook reconciliation are implemented. For multi-instance production, add Redis TTL seat locks, booking idempotency, expiry worker, refresh-token rotation, email verification, rate limits, audit logs, tests, backups and monitoring. Configure cancellation/refund policy and use licensed movie metadata/poster sources.
