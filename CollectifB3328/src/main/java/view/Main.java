/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import metier.modele.Adherent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Activite;
import metier.modele.Demande;
import metier.modele.Evenement;
import metier.modele.Lieu;
import metier.service.Service;
import metier.service.ServiceTechnique;
/**
 *
 * @author ubuonomo
 */
public class Main {

    /**
     * Insère quelques données en base
     * @param serv
     */    
    public static void Init(Service serv) {
        serv.InsertionAdherentsEnBase(20);
        serv.InsertionActivitesEnBase(5);
        serv.InsertionLieuxEnBase(3);
    }
    
    /**
     * Affiche les options pour un adherent non connecte ou non inscrit.
     */
    private static void AffichageOptionsNonConnecte() {
        System.out.println("-----------------------");
        System.out.println("I : Inscription");
        System.out.println("C : Connexion");
        System.out.println("E : Fin");
        System.out.println("-----------------------");
    } 
    
    /**
     * Affiche les options pour un adherent inscrit.
     */
    private static void AffichageOptionsConnecte() {
        System.out.println("-----------------------");
        System.out.println("D : Demander activité");
        System.out.println("H : Voir historique");
        System.out.println("E : Déconnexion");
        System.out.println("-----------------------");
    }
    
    private static void AffichageOptionsDepart() {
        System.out.println("-----------------------");
        System.out.println("A : Adhérent");
        System.out.println("G : Gérant");
        System.out.println("E : Fin");
        System.out.println("-----------------------");
    }
    
    /**
     * Affiche les options pour le gérant.
     */
    private static void AfficherOptionsGerant() {
        System.out.println("-----------------------------------------------------");
        System.out.println("AC : Afficher la liste des activités");
        System.out.println("AD : Afficher la liste des adhérents");
        System.out.println("L : Afficher la liste des lieux");        
        System.out.println("ENP : Afficher la liste des évènements non planifiés");        
        System.out.println("EP : Afficher la liste des évènements planifiés");
        System.out.println("P : Planifier un évènement");
        System.out.println("Exit : Fin");
        System.out.println("-----------------------------------------------------");
    }
    
    /**
     * Gère les entrées clavier d'un adherent non connecté ou non inscrit. 
     * Un adhérent non connecté ou non inscrit peut s'inscrire, se connecter ou sortir (exit).
     * @param serv 
     */
    public static void PremiereConnexion(Service serv) {
        Scanner sc = new Scanner(System.in);
        boolean fin = false;
        Adherent ad;
        while(!fin) {
            AffichageOptionsNonConnecte();
            String commande = sc.nextLine();
            switch (commande) {                
                case "I" :
                    System.out.println("Veuillez saisir votre nom :");
                    String nom = sc.nextLine();
                    System.out.println("Veuillez saisir votre prenom :");
                    String prenom = sc.nextLine();
                    System.out.println("Veuillez saisir votre adresse postale :");
                    String postale = sc.nextLine();
                    System.out.println("Veuillez saisir votre adresse mail :");
                    String mail = sc.nextLine();
                    Adherent newAd = new Adherent(nom, prenom, postale, mail);
            
                    try {
                        serv.Inscription(newAd);
                        ServiceTechnique.envoiMailConfInscr(newAd,true);                        
                    } catch (Throwable ex) {
                        ServiceTechnique.envoiMailConfInscr(newAd, false);
                    }
                                
                    break;
                
                case "C" :
                    System.out.println("Login (adresse mail) : ");
                    String mailCo = sc.nextLine();
                    ad = serv.Connexion(mailCo);
                    if (ad != null)
                        GestionClavier(serv, ad);
                    else
                        System.err.println("Désolé le mail est invalide");
                    break;
                
                case "E" :
                    fin = true;
                    break;
                
                default :
                    System.out.println("Désolé la commande n'est pas bonne.");
                    break;
            }
        }
        System.out.println("Au revoir.");
    }
    
    /**
     * Gère les entrées clavier d'un adhérent connecté. 
     * Un adhérent peut demander un évènement, voir son historique ou sortir (exit);
     * @param serv
     * @param ad 
     */
    public static void GestionClavier(Service serv, Adherent ad) {
        System.out.println("Bienvenue " + ad.getPrenom() + " !");
        Scanner sc = new Scanner(System.in);
        boolean fin = false;
        while(!fin) {
            AffichageOptionsConnecte();
            String commande = sc.nextLine();
            switch (commande) {
               
                case "D" :
                    List<Activite> acts = serv.voirActivites();
                    int taille = acts.size();
                    if(taille > 0) {
                        int i = 1;
                        for(Activite act : acts) {
                            System.out.println("Numéro " + i + " : "+ act.getDenomination() + " (" + act.getNbParticipants() + ")");
                            ++i;
                        }
                    }
                    System.out.println("Veuillez indiquer le numéro de l'activité choisie : ");
                    int numChoisi = sc.nextInt();
                    if(0 <= numChoisi - 1 && numChoisi - 1 < taille) 
                    {
                        Activite actChoisie = acts.get(numChoisi-1);
                        ArrayList<Date> dates = new ArrayList<>();
                        Date d1 = new Date(2016, 04, 02);
                        Date d2 = new Date(2016, 04, 03);
                        Date d3 = new Date(2016, 04, 04);
                        dates.add(d1);
                        dates.add(d2);
                        dates.add(d3);
                        System.out.println("Choisir une date parmis les suivantes : ");
                        System.out.println("Date 1 : " + d1.toString().substring(0, 10));
                        System.out.println("Date 2 : " + d2.toString().substring(0, 10));
                        System.out.println("Date 3 : " + d3.toString().substring(0, 10));
                        
                        System.out.println("Veuillez indiquer le numéro de la date choisie : ");
                        int numdateChoisie = sc.nextInt();
                        sc.nextLine();
                        if(0 <= numdateChoisie - 1 && numdateChoisie - 1 < taille) {
                            Date dateChoisie = dates.get(numdateChoisie-1);
                            try {
                                if(!serv.demanderEvenement(ad, actChoisie, dateChoisie))
                                {
                                    System.out.println("Demande déjà effectuée !");
                                }
                                else
                                {
                                    System.out.println("Demande envoyée !");
                                    System.out.println("Un mail de confirmation vous sera envoyé si l'évènement à lieu "
                                            + "et vous indiquera où il aura lieu.");
                                }
                            } catch (Throwable ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else
                            System.out.println("Faux numéro de date !");                      
                            
                    }
                    else
                        System.out.println("Faux numéro d'activité !");
                    break;
                
                case "H" :
                    List<Demande> demandes = serv.voirHistorique(ad.getId());
                    if(demandes.isEmpty())
                        System.out.println("Aucunes demandes à ce jour.");                    
                    else {
                       System.out.println("----------- Liste demandes -----------");
                        for(Demande dem : demandes)
                        {
                            System.out.println(dem.toString());
                        }    
                    }
                                     
                    break;
                
                case "E" :
                    fin = true;
                    break;
                
                default :
                    System.out.println("Désolé, la commande n'est pas bonne.");
                    break;
            }
        }
        System.out.println("Au revoir " + ad.getPrenom() +" !");
    }
        
    private static void GestionGerant(Service serv) {
        System.out.println("Bienvenue Big Boss !");
        Scanner sc = new Scanner(System.in);
        boolean fin = false;
        while(!fin)
        {
            AfficherOptionsGerant();
            String commande = sc.nextLine();
            switch(commande) {
                
                case "AC" :
                    List<Activite> acts = serv.voirActivites();
                    for(Activite act : acts)
                    {
                        System.out.println(act.toString());
                    }
                    break;
                    
                case "AD" :
                    List<Adherent> ads = serv.voirAdherents();
                    if(ads.size() > 0) 
                    {
                        for(Adherent ad : ads)
                        {
                           System.out.println(ad.toString());
                        }
                    }
                    else
                        System.out.println("Aucun adhérent présent dans la base.");
                    break;
                    
                case "L" :
                    List<Lieu> lieux = serv.voirLieux();
                    if(lieux.size() > 0) 
                    {
                       for(Lieu l : lieux)
                        {
                            System.out.println(l.toString());
                        } 
                    }
                    else
                        System.out.println("Aucun lieu présent dans la base.");                    
                    break;
                    
                case "ENP" :
                    List<Evenement> eventsNonPlan = serv.voirEvenementsNonPlanifies();
                    if(eventsNonPlan.size() > 0) 
                    {
                       for(Evenement e : eventsNonPlan)
                        {
                            System.out.println(e.toString());
                        } 
                    }
                    else
                        System.out.println("Aucun évènement non planifié présent dans la base.");                    
                    break;
                    
                case "EP" :
                    List<Evenement> eventsPlan = serv.voirEvenementsPlanifies();
                    if(eventsPlan.size() > 0) 
                    {
                       for(Evenement e : eventsPlan)
                        {
                            System.out.println(e.toString());
                        } 
                    }
                    else
                        System.out.println("Aucun évènement planifié présent dans la base.");                    
                    break;
                    
                case "P" :
                    List<Evenement> evts = serv.voirEvenementsNonPlanifies();                 
                    if(evts.size() > 0) 
                    {
                        int i = 1;
                        int taille = evts.size();
                        for(Evenement e : evts)
                        {
                            System.out.println("------------------------------- Numéro " + i + " ---------------------------------------------------");
                            System.out.println(e.toString());
                            System.out.println("----------------------------------------------------------------------------------------------------");
                            i++;
                        }
                        
                        System.out.println("Veuillez indiquer le numéro de l'évènement choisi : ");
                        int numChoisi = sc.nextInt();
                        sc.nextLine();
                        if(0 <= numChoisi - 1 && numChoisi - 1 < taille) 
                        {
                            Evenement evtChoisie = evts.get(numChoisi-1);
                            List<Lieu> lieuxProches = serv.trouverLieuxPlusProche(evtChoisie, 5);
                            System.out.println("\nChoisir un lieu parmis les suivants : ");
                            for(int j = 1; j < lieuxProches.size()+1; j++)
                            {
                                System.out.println("Lieu " + j + " : " + lieuxProches.get(j-1).getDenomination() + " (" + lieuxProches.get(j-1).getDescription() + ")");
                            }

                            System.out.println("Veuillez indiquer le numéro du lieu choisi : ");
                            int numLieuChoisi = sc.nextInt();
                            if(0 <= numLieuChoisi - 1 && numLieuChoisi - 1 < lieuxProches.size()) {
                                Lieu lieuChoisi = lieuxProches.get(numLieuChoisi-1);
                                try {
                                    serv.AffecterLieux(evtChoisie, lieuChoisi);
                                } catch (Throwable ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            else
                                System.out.println("Faux numéro de lieu !");                            

                        }
                        else
                            System.out.println("Faux numéro d'évènement !");
                        }
                    else
                        System.out.println("Aucun évènement non planifié.");
                    break;
                    
                case "Exit" :
                    fin = true;
                    break;
                    
                default :
                    System.out.println("Fausse commande.");
                    break;
            }
        }
    }
    
    
    public static void main(String[] args) {
        Service serv = new Service();
        Init(serv);
        boolean fin = false;
        Scanner sc = new Scanner(System.in);
        while(!fin) {
            AffichageOptionsDepart();
            String cmd = sc.nextLine();
            switch (cmd) {
                case "A" :
                    PremiereConnexion(serv);
                    break;
                case "G" :
                    System.out.println("Mot de passe : ");
                    String mdp = sc.nextLine();
                    if(mdp.equals("dasi"))
                        GestionGerant(serv);
                    else
                        System.out.println("Mot de passe faux.");
                    break;
                case "E" :
                    fin = true;
                    break;
            }
        }
        System.out.println("Fin.");
        
        /*JpaUtil.creerEntityManager();
        ActiviteDao adao = new ActiviteDao();
        JpaUtil.ouvrirTransaction();
        List<Activite> list;
        try {
            Activite a;
            for (int i = 0 ; i < 10 ; i++)
            {
                boolean equipe = true;
                if(i%2 == 0)
                    equipe = false;
                a = new Activite("Foot"+i,equipe,22);
                try {
                    adao.create(a);
                } catch (Throwable ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JpaUtil.validerTransaction();
            list = adao.findAll();
            for (Activite ac : list)
                System.out.println(ac.toString());
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);            
        }
        //"H:\Mes documents\collectif\CollectifB3328\src\main\java\view\Main.java"
        Service serv = new Service();
        try {
            Adherent a;
            for (int i = 0 ; i < 100 ; i++)
            {
                serv.Inscription("nom"+i, "prenom"+i,i+" rue des antonins","mail"+i);
                a = serv.Connexion("mail"+i);
                serv.demanderEvenement(a, serv.ObtenirActivite("Tarot"), new Date(116, 2, 14));
            }
            a = serv.Connexion("asing8183@free.fr");
            serv.demanderEvenement(a, serv.ObtenirActivite("Tarot"), new Date(116, 2, 14));
            List<Demande> histo = serv.voirHistorique(a.getId());
            for (Demande d : histo)
            {
                System.out.println(d);
            }
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.creerEntityManager();
        AdherentDao adao = new AdherentDao();
        JpaUtil.ouvrirTransaction();
        List<Adherent> list;
        try {
            Adherent a;
            for (int i = 1 ; i < 11 ; i++)
            {
                a = new Adherent("nom"+i, "prenom"+i, i+" Chemin des sources", "mail"+i);
                try {
                    adao.create(a);
                } catch (Throwable ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            list = adao.findAll();
            for (Adherent a : list)
            {                
               a.setCoordonnees(GeoTest.getLatLng(a.getAdresse()));
            }
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);            
        }        
        
        /*Service serv = new Service();
        Adherent a;
        a = serv.Connexion("mail2");
        System.out.println(a.getAdresse());
        
        ActiviteDao acdao = new ActiviteDao();
        
        Activite ac = acdao.findByName("Badminton");
        if (ac!= null)
        {
            try {
                serv.demanderEvenement(a, ac, null);
            } catch (Throwable ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            DemandeDao dadao = new DemandeDao();
            List<Demande> demandes = dadao.findByAdherentId(a);
            if(demandes.isEmpty()){
                System.out.println("Erreur");
            }
            else {
                for(Demande d : demandes)
                {
                    System.out.println(d.toString());
                }
            }
                       
        }
       
        
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        Service serv = new Service();
        ActiviteDao actdao = new ActiviteDao();
        Activite act1 = new Activite("Tennis", false, 2);
        LieuDao ldao = new LieuDao();
        Lieu l1 = new Lieu("Terrain de tennis", "Jolie", "14 chemin des sources");
        try {
            actdao.create(act1);
            ldao.create(l1);
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.validerTransaction();
        
        JpaUtil.creerEntityManager();
        Activite ac1 = actdao.findByName("Tennis");
        Adherent a1 = adao.findByMail("mail3");
        Adherent a2 = adao.findByMail("mail4");
        Adherent a3 = adao.findByMail("mail5");
        Adherent a4 = adao.findByMail("mail6");
        Adherent a5 = adao.findByMail("mail1");
        Adherent a6 = adao.findByMail("mail2");
        Adherent a7 = adao.findByMail("mail7");
        Adherent a8 = adao.findByMail("mail8");
        Adherent a9 = adao.findByMail("mail9");
        
        try {
            serv.demanderEvenement(a1, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a2, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a3, ac1, new Date(0, 0, 0));            
            serv.demanderEvenement(a4, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a5, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a6, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a7, ac1, new Date(0, 0, 0));            
            serv.demanderEvenement(a8, ac1, new Date(0, 0, 0));
            serv.demanderEvenement(a9, ac1, new Date(0, 0, 0));
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<LatLng> l;
        try {
            l = serv.LocaliserParticpants(serv.ObtenirEvenement(serv.AfficherEvenements().get(0).getId()));
            for (LatLng toPrint : l)
            {
                System.out.println(toPrint);
            }
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Evenement e = serv.ObtenirEvenement(serv.AfficherEvenements().get(0).getId());
            serv.AffecterLieux(e, serv.trouverLieuxPlusProche(e, 2).get(0));
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }        
}
