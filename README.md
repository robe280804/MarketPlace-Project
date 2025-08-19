# IN FASE DI SVILUPPO, SONO POSSIBILI MODIFICHE

# 🛒 Marketplace con Ruoli Avanzati

> Piattaforma e-commerce ispirata a Etsy/Amazon, per la vendita e l’acquisto di prodotti artigianali o digitali.  
> Realizzata in **Java + Spring Boot + Spring Cloud**, con **architettura a microservizi**, gestione ruoli avanzata e sicurezza.

---

## 📋 Descrizione
Il sistema consente ad acquirenti e venditori di interagire in un ambiente sicuro e scalabile.  
Include gestione prodotti, carrelli, ordini, messaggistica e un centro assistenza.

---

## ✨ Funzionalità Principali

### 👤 Gestione Ruoli
- **👑 Admin** → Gestisce utenti, prodotti, ordini e supporto clienti.
- **🏪 Venditore** → Può vendere prodotti, accedere alle statistiche e comunicare con i clienti.
- **🛍️ Acquirente** → Può acquistare prodotti, usare filtri, aggiungere preferiti e gestire il carrello.

---

### 🛍 Funzioni Acquirente
- 🔍 Visualizzare tutti i prodotti.
- 📊 Filtrare per **prezzo**, **tipologia**, **condizione** (nuovo, usato).
- ❤️ Aggiungere articoli ai **preferiti**.
- 🛒 Inserire articoli nel **carrello** ed effettuare ordini.

---

### 🏪 Funzioni Venditore
- 📦 Inserire nuovi prodotti con descrizione, immagini e prezzo.
- 📈 Dashboard vendite: ordini recenti, ricavi totali, prodotti più venduti.
- 💬 Comunicazione diretta con i clienti.

---

### 📢 Comunicazioni e Supporto
- 🔔 **Notifiche** in tempo reale per ordini e messaggi.
- 💌 **Messaggistica** tra cliente e venditore.
- 🎧 **Centro assistenza** con possibilità di contatto con l’Admin.

---

## ☁ Architettura Spring Cloud
- ⚖ **Load Balancing** → Distribuzione del carico tra microservizi.
- 🚪 **API Gateway** → Accesso centralizzato con routing e sicurezza.
- 📍 **Service Discovery (Eureka)** → Registrazione e individuazione dei servizi.
- ⚙ **Config Server** → Configurazioni centralizzate.

---

## 🏗 Struttura a Microservizi
📦 marketplace
- ┣ 📂 auth-service → Autenticazione, autorizzazione, gestione ruoli
- ┣ 📂 product-service → Crud prodotti
- ┣ 📂 cart-service → Gestione carrello, wishlist e ordini
- ┣ 📂 gateway → API Gateway per instradamento richieste
- ┣ 📂 eureka → Service Discovery
- ┗ 📂 config → Config Server centralizzato

## Gateway
- Estrazione dati dell'utente dal token, invio di questi negli header

Ogni microservice dovrà avere un filtro per validare i dati dell'utente inviati nell'header dal gateway


##  ️Auth-Service
- Autenticazione sia centralizzata per tutti i microservizi.

Gestire solo login, registrazione, refresh token, ruoli.
Nessun dato sensibile non legato all’accesso.
Associare un UUID univoco a ogni utente per referenziarlo negli altri servizi.

## User-Service e gestione preferiti
- Gestisce i dati dell'utente e la sezione preferiti

Gestisce dati personali (nome, cognome, dati residenziali ...)
Gestisce la sezione preferiti dei prodotti

## Product-Service
- CRUD prodotti, filtri ...

Creazione dei prodotti, gestione quantità ...
Metodi in base al ruolo dell'utente

## Cart-Service
- Gestione carrello dell'utente

Aggiungere / Rimuovere prodotti, calcolo totale ...

---

## 🚀 Possibili Estensioni Future
- 💳 Pagamenti integrati (Stripe / PayPal).
