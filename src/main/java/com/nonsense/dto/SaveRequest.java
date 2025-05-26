package com.nonsense.dto;

import java.util.List;

/**
* Data Transfer Object utilizzato per richevere una richiesta che contine una lista di frasi da salvare su un file
*/
public class SaveRequest {
    /**
    * Insieme di tutte la frasi che voglio salvare.
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
    * @param phrases lista di stringhe contentente le frasi
    */
    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }
}
