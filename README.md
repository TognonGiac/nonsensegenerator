# NONSENSE-generator
gruppo composto da Girotto Giulia, Marcato Silvia, Smiderle Nicola, Tognon Giacomo

Per visualizzare la documentazione --> https://tognongiac.github.io/nonsensegenerator/

Per visualizzare le User Stories su Jira --> https://giuliagirotto.atlassian.net/jira/software/projects/NGE/boards/34/backlog?atlOrigin=eyJpIjoiMThlYTM0NTZmNzg4NDFhNmFiMGZlYTk4MmJjMWRkOGYiLCJwIjoiaiJ9


---


## DOWNLOAD

Per utilizzare NONSENSE Generator si raccomanda di:

  1) Clona il progetto da GitHub in una cartella a piacere:
   git clone https://github.com/TognonGiac/nonsensegenerator.git

  2) Configura la tua API key Google.
   - Apri il file: nonsensegenerator/src/main/resources/application.properties
   - Vedrai la riga:      google.api.key=yourkey "inizia con Aiza"
   - Inserisci al posto di yourkey "inizia con Aiza" la tua effettiva key

3) Avvia il progetto da terminale usando Maven Wrapper:
   - Su Linux/macOS:
       ./mvnw spring-boot:run
   - Su Windows (PowerShell o cmd):
       mvnw.cmd spring-boot:run

4) Apri il browser e vai all’indirizzo:
   http://localhost:8080
   Qui vedrai il sito in funzione.

---------------------------------------------

ALTERNATIVA USANDO IDE (IntelliJ, Eclipse, VS Code)

Al posto del punto 3, puoi:

- Aprire il progetto nell’IDE
- Navigare nella classe:
  com.nonsense.NonsenseApplication
- Eseguire direttamente questa classe (run/debug)
- Il sito sarà attivo su http://localhost:8080 come sopra
