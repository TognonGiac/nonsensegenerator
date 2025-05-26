package com.nonsense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Classe di avvio dell'applicazione Spring Boot
*/
@SpringBootApplication
public class NonsenseApplication {
   
    /**
    * Metodo main che avvia l'applicazione 
    *
    * @param args : argomenti passati da linea di comando
    */
    public static void main(String[] args) {
        SpringApplication.run(NonsenseApplication.class, args);
    }
}
