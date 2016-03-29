/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import metier.modele.Adherent;
import metier.modele.Evenement;

/**
 *
 * @author pbayle
 */
public class ServiceTechnique {

    static void EnvoyerMail(Evenement evt) {
        if (evt.getActivite().isParEquipe())
        {
            for(Adherent a : evt.getParticipants()) {
                
            }
        }
        else
        {
            
        }
    }

    public static void envoiMailConfInscr(Adherent newAd, boolean reussite) {
        if (reussite) {
            System.out.println("Expediteur : collectif@collectif.org \nPour : " + newAd.getMail() 
                + "\nSujet : Bienvenue chez Collect'IF \nCorps : \nBonjour " + newAd.getPrenom() 
                + ",\nNous vous confirmons votre adhésion à l'association COLLECT'IF. Votre numéro d'adhérent est : " 
                + newAd.getId() + ".");
        }
        else {
            System.out.println("Expediteur : collectif@collectif.org \nPour : " + newAd.getMail() 
                + "\nSujet : Bienvenue chez Collect'IF \nCorps : \nBonjour " + newAd.getPrenom() 
                + ",\nVotre adhésion à l'association COLLECT'IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
        }
            
        
    }
    
}
