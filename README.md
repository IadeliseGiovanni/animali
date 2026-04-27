Per far girare il progetto, la configurazione non è altro che un passaggio rapidissimo: ho scelto PostgreSQL per la sua affidabilità. Basta creare un database vuoto chiamato adozioni e, grazie alla proprietà ddl-auto=update di Hibernate, tutte le tabelle (Animali, Eventi, Adottanti) vengono generate automaticamente al primo avvio del backend. Questo rende l'installazione del software estremamente fluida e scalabile."

Ricordati: Prima di avviare Spring Boot dopo aver creato il DB, controlla nel file application.properties che la riga dell'URL sia esattamente questa:
spring.datasource.url=jdbc:postgresql://localhost:5432/adozioni

Create Database sul db.