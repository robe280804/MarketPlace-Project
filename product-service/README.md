# Product-service

Microservizio che gestisce i prodotti presenti, la loro quantità, il prezzo ... Funge come magazzino.

---

## ✨ Funzionalità principali

- Creazione dei prodotti (Solo se si è ADMIN o VENDITORI)
- Visualizzazione di tutti i prodotti
- Aggiornamento della quantità di un prodotto
- Rimozione di un prodotto (Il VENDITORE può rimuovere solo i prodotti creati, l' ADMIN qualunque)
- Comprare un prodotto (Ottieni un prodotto con la quantità desiderata)
- Prodotti dell'utente (Il VENDITORE o l' ADMIN possono visualizzare i prodotti creati)

---

## 🔗 Integrazione con altri microservizi

- Il **Gateway** invia nell' header i dati dell'utente estratti dal Token, il **product-Service** crea l' Authentication e lo salva
    nel SecurityContextHolder
- Il **cart-service** richiede i prodotti dal **product-service**

---

## ⚙️ Stack Tecnologico

- **Linguaggio**: Java 17
- **Framework**: Spring Boot 3.** (Spring Security, Spring Data JPA)
- **Database**: MySQL
- **Build Tool**: Maven
- **Containerization**: Docker

---


