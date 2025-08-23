# Auth Service 🔐

Microservizio che gestisce l'autenticazione e la gestione dei ruoli degli utenti.  
Fornisce meccanismi di registrazione, login e validazione dei token JWT per garantire la sicurezza del sistema distribuito.

---

## ✨ Funzionalità principali

- Registrazione utente con **hashing della password** (es. BCrypt).
- Login con credenziali locali o provider esterni (OAuth2/OpenID Connect).
- Generazione e validazione di **JSON Web Token (JWT)**.
- Gestione dei ruoli e autorizzazioni (es. `ADMIN`, `VENDITORE`, `ACQUIRENTE`).

---

## 🔗 Integrazione con altri microservizi

- Il **Gateway** interroga `auth-service` per validare il token JWT.
- L’`id` dell’utente estratto dal token viene verificato nel database.
- Gli altri microservizi delegano ad `auth-service` la gestione di login, registrazione e refresh token.

---

## ⚙️ Stack Tecnologico

- **Linguaggio**: Java 17
- **Framework**: Spring Boot 3.** (Spring Security, Spring Data JPA)
- **Database**: MySQL
- **Autenticazione**: JWT, OAuth2
- **Build Tool**: Maven
- **Containerization**: Docker

---

## 📡 API Endpoints principali

| Metodo | Endpoint              | Descrizione                           | Autenticazione |
|--------|-----------------------|---------------------------------------|----------------|
| POST   | `/api/auth/register`  | Registra un nuovo utente              | ❌             |
| POST   | `/api/auth/login`     | Effettua login e genera JWT           | ❌             |


