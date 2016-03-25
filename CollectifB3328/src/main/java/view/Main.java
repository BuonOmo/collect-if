/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import com.google.maps.model.LatLng;
import dao.ActiviteDao;
import metier.modele.Adherent;
import dao.AdherentDao;
import dao.DemandeDao;
import dao.JpaUtil;
import dao.LieuDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Activite;
import metier.modele.Demande;
import metier.modele.Evenement;
import metier.modele.Lieu;
import metier.service.Service;
import util.GeoTest;
/**
 *
 * @author ubuonomo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        }*/
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
                       
        }*/
        
        
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
        }
        
    }
        
    
}
