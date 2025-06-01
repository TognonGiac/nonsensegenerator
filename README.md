# NONSENSE-generator
Gruppo composto da Girotto Giulia, Marcato Silvia, Smiderle Nicola, Tognon Giacomo

Per visualizzare la documentazione --> https://tognongiac.github.io/nonsensegenerator/

Per visualizzare le User Stories su Jira --> https://giuliagirotto.atlassian.net/jira/software/projects/NGE/list

---


## DOWNLOAD

Per utilizzare NONSENSE Generator si raccomanda di:

1) Clonare il progetto da GitHub in una cartella a piacere:
   git clone https://github.com/TognonGiac/nonsensegenerator.git

2) Configurare la tua API key Google:
      - Aprire il file: nonsensegenerator/src/main/resources/application.properties
      - Si vedrà la riga:      google.api.key=yourkey "inizia con AIza"
      - Inserire al posto di yourkey "inizia con AIza" la propria key

3) Avviare il progetto da terminale usando Maven Wrapper:
      - Su Linux/macOS:
          ./mvnw spring-boot:run

         - Nota per macOS: se compare un errore di tipo *Permission denied*, esegui prima -> 
           chmod +x mvnw
      - Su Windows (PowerShell o cmd):
          mvnw.cmd spring-boot:run

4) Aprire il browser e andare all’indirizzo:
   http://localhost:8080
   --> qui si vedrà il sito in funzione.

---------------------------------------------

ALTERNATIVA USANDO IDE (IntelliJ, Eclipse, VS Code)

Invece di fare le operazioni descritte al punto 3, è necessario
- Aprire il progetto nell’IDE
- Navigare nella classe: com.nonsense.NonsenseApplication
- Eseguire direttamente questa classe (run/debug)
- Il sito sarà attivo su http://localhost:8080
  come riportato al punto 4 sopra
