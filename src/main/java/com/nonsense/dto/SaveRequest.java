package com.nonsense.dto;

import java.util.List;

/**
* Data Transfer Object (DTO) per le richieste di salvataggio di frasi nonsense.
* <p>
* Questa classe rappresenta il payload delle richieste HTTP che contengono 
* una collezione di frasi da salvare u un file.
* </p>
*/
public class SaveRequest {
    /** Costruttore pubblico di default. */
    public SaveRequest(){
    }
    
    /**
    * Insieme di tutte la frasi da salvare su file.
    * <p>
    * Contiene la collezione di stringhe che rappresentano le frasi nonsense
    * che l'utente vuole salvare su file.
    * <p>
    */
    private List<String> phrases;

    /**
    * Restituisce la lista di frasi da salvare.
    *
    * @return lista di stringhe contentente le frasi
    */
    public List<String> getPhrases() {
        return phrases;
    }
   
    /**
    * Imposta la lista di frasi da salvare.
    *
    * @param phrases    lista di stringhe contentente le frasi
    */
    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }
}
