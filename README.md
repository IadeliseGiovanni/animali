# 🚀 PetFlow: Adozioni Animali API

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203-brightgreen?style=for-the-badge&logo=springboot)
![Angular](https://img.shields.io/badge/Frontend-Angular%2017-red?style=for-the-badge&logo=angular)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?style=for-the-badge&logo=postgresql)
![JWT](https://img.shields.io/badge/Security-JWT-black?style=for-the-badge&logo=jsonwebtokens)

## 🐾 Progetto

**PetFlow** è una piattaforma Full Stack progettata per digitalizzare e ottimizzare il processo di adozione degli animali. Il sistema permette di gestire l'intero ciclo di vita dell'adozione: dall'inserimento dell'animale nel database fino alla gestione degli eventi e degli adottanti, garantendo sicurezza e integrità dei dati tramite autenticazione **JWT**.

### 🛠️ Architettura e Meccanismo
Il progetto segue un approccio professionale basato sulla separazione delle responsabilità (**Separation of Concerns**):

*   **Core Backend:** Sviluppato con **IntelliJ IDEA**, gestisce la logica di business, la persistenza e la sicurezza.
*   **User Interface:** Sviluppata con **VS Code**, offre un'esperienza utente reattiva consumando i servizi REST in modo asincrono.

---

## ⚙️ Configurazione Rapida

### 🗄️ Database Setup
Il sistema è configurato per essere **"Plug & Play"**. 
1. Crea un DB vuoto chiamato `adozioni` in PostgreSQL.
2. Grazie alla configurazione `ddl-auto=update`, le tabelle (Animali, Eventi, Adottanti) vengono modellate automaticamente al primo avvio.

> **Configurazione nel file application.properties:**
> `spring.datasource.url=jdbc:postgresql://localhost:5432/adozioni`

### 🚀 Avvio Applicazione

| Layer | IDE consigliata | Comando / Azione | Porta Default |
| :--- | :--- | :--- | :--- |
| **Backend** | IntelliJ IDEA | `Run AnimaliApplication` | `8080` |
| **Frontend** | VS Code | `ng serve` | `4200` |

---

## 💎 Caratteristiche Tecniche

Ho implementato i seguenti standard industriali per garantire la qualità del software:

*   **Design Pattern DTO:** Astrazione dei dati tramite *Data Transfer Objects* per proteggere le entità del database.
*   **Sincronizzazione Asincrona:** Gestione dei flussi dati tramite **RxJS Observable** per un'interfaccia sempre fluida.
*   **Security Layer:** Implementazione di un **JwtAuthenticationFilter** per la validazione dei token di accesso.
*   **CORS Policy:** Configurazione specifica per permettere la comunicazione sicura tra i diversi domini delle IDE.

---

### 👨‍💻 Sviluppo e Manutenzione
Il progetto è strutturato per essere scalabile. La scelta di utilizzare **PostgreSQL** garantisce l'affidabilità dei dati necessari per un contesto delicato come quello delle adozioni animali.

---
*Progetto creato con passione per aiutare i nostri amici a quattro zampe.*
