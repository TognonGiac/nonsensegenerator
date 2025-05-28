# Domain Model

![Domain Model](img/diagrams/DomainModel.png)

```plantuml
@startuml
object User 
object InputSentence
object SentenceAnalyzer
object Dictionary
object Word {
  type
}
object SentenceGenerator {
  outputSentenceCount
}
object NonsenseSentence
object SentenceModerator
object Output

User "1" --> "1" InputSentence : inserts
User --> SentenceGenerator : requests generation
InputSentence "1" --> "1" SentenceAnalyzer : analyzed by
SentenceAnalyzer --> Word : extracts
SentenceAnalyzer --> Dictionary : accesses
Dictionary "1" --> "0..*" Word : contains
SentenceGenerator --> Dictionary : accesses
SentenceGenerator "1" --> "0..*" NonsenseSentence : generates
SentenceGenerator --> SentenceModerator : request validation
SentenceModerator "1" --> "1" NonsenseSentence : validates
Output "1" --> "1" NonsenseSentence : displays
User "1" --> "1" Output : views
@enduml
```
