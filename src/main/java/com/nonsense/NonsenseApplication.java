package com.nonsense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Classe principale dell'applicazione Nonsense.
* <p>
* Questa classe si occupa della configurazione automatica e dell'avvio del container Spring.
* </p>
*/
@SpringBootApplication
public class NonsenseApplication {
   
    /**
    * Metodo main che avvia l'applicazione 
    *
    * @param args   argomenti passati dalla linea di comando all'applicazione.
    */
    public static void main(String[] args) {
        SpringApplication.run(NonsenseApplication.class, args);
    }
}
