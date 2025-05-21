# System Sequence Diagram

![SystemSequenceDiagram.png](img/diagrams/SystemSequenceDiagram.png)

```plantuml
@startuml
actor User
participant UI
participant "AnalizzatoreFrasi" as AF
participant "Dizionario" as D
participant "GeneratoreFrasi" as GF
participant "ModeratoreFrasi" as MF
participant "Output" as O

== Inserimento e analisi frase ==
User -> UI : insertInitialSentence()
UI -> AF : analizzaFrase(frase)
AF -> D : salvaParole(parole)

== Selezione numero di frasi ==
User -> UI : selectNumberOfSentences(n)

== Generazione frasi ==
UI -> GF : generaFrasi(n, dizionario)
GF -> D : getParole()
GF -> GF : creaFrasiRandom()

== Controllo tossicità ==
GF -> MF : controllaTossicità(frasi)
MF -> MF : filtraFrasiSicure()

== Visualizzazione ==
MF -> O : mostra(frasiSicure)
User -> UI : visualizzaOutput()
@enduml
```


# Internal Sequence Diagrams


## Analisi frasi e salvataggio

![InternalSequenceDiagram1.png](img/diagrams/InternalSequenceDiagram1.png)

```plantuml
@startuml
title Internal SD – Analisi Frase & Salvataggio Parole

participant FraseInInput
participant AnalizzatoreFrasi
participant Dizionario
participant Parola

FraseInInput -> AnalizzatoreFrasi : analizza()
AnalizzatoreFrasi -> Parola : creaParole()
loop per ogni parola
  Parola -> Dizionario : salvaParola(parola)
end
@enduml
```


## Generazione di frasi nonsense

![InternalSequenceDiagram2.png](img/diagrams/InternalSequenceDiagram2.png)

```plantuml
@startuml
title Internal SD – Generazione Frasi Nonsense

participant GeneratoreFrasi
participant Dizionario
participant Parola
participant FraseNonSense

GeneratoreFrasi -> Dizionario : getParole()
loop n volte
  Dizionario -> Parola : getRandom(tipo)
  Parola -> FraseNonSense : componi()
end
GeneratoreFrasi -> FraseNonSense : ritornaFrasi()
@enduml
```


## Controllo tossicità

![InternalSequenceDiagram3.png](img/diagrams/InternalSequenceDiagram3.png)

```plantuml
@startuml
title Internal SD – Controllo Tossicità

participant FraseNonSense
participant ModeratoreFrasi
participant GoogleAPI
participant Output

FraseNonSense -> ModeratoreFrasi : inviaFrasi()
loop per ogni frase
  ModeratoreFrasi -> GoogleAPI : isTossica(frase)
  alt frase sicura
    ModeratoreFrasi -> Output : mostra(frase)
  else frase tossica
    ModeratoreFrasi -> Output : mostraWarning()
  end
end
@enduml
```

## Azioni post-output: reset, copia, salvataggio

![InternalSequenceDiagram4.png](img/diagrams/InternalSequenceDiagram4.png)

```plantuml
@startuml
title Internal SD – Azioni Post-Output

participant Output
participant Clipboard
participant FileWriter

== Copia frasi ==
Output -> Clipboard : copia(frasi)

== Reset ==
Output -> Output : clear()

== Salva su file ==
Output -> FileWriter : salvaTXT(frasi)
@enduml
```
