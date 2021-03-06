

import com.google.maps.model.LatLng;
import dao.ActiviteDao;
import dao.AdherentDao;
import dao.DemandeDao;
import dao.EvenementDao;
import dao.JpaUtil;
import dao.LieuDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;
import metier.modele.Evenement;
import metier.modele.EvenementAvecEquipe;
import metier.modele.EvenementSansEquipe;
import metier.modele.Lieu;
import sun.misc.GC;
import sun.text.normalizer.NormalizerImpl;
import util.GeoTest;
package metier.service;
/**
 *
 * @author ubuonomo
 */
public class Service {


    //------------- ECRITURE DANS LA BASE DE DONNEES ---------------------

    /**
     * Ajoute une activite à la base de données
     * @param a activié à ajouter
     */
    public void AjouterActivite(Activite a) throws ServiceException
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new ActiviteDao().create(a);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * Ajoute un Lieu à la base de données
     * @param l lieu à ajouter
     */
    public void AjouterLieu(Lieu l) throws ServiceException
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new LieuDao().create(l);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * TODO verifier les cas de null ou de doublons
     * @param nom
     * @param prenom
     * @param postale adresse postale
     * @param mail adresse mail
     * @throws Throwable
     */
    public void Inscription (Adherent a) throws ServiceException
    {
        a.setCoordonnees(GeoTest.getLatLng(a.getAdresse()));
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new AdherentDao().create(a);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * Permet d’envoyer une requete d’evenement pour un adherent
     * Renvoi une Exception si la demande existe déjà
     * @param   ad  Adherent demandant l’evenement
     * @param   act Activité souhaitée par l’adherent
     * @param   d   Date ou l’adherent souhaite réaliser l’évenement
     */
    public void demanderEvenement(Adherent ad, Activite act, Date d) throws ServiceException
    {
        Demande dem = new Demande(act, ad, d);
        DemandeDao da = new DemandeDao();
        if (da.existe(dem))
            throw new ServiceException("Demande déjà existante");

        ad.addDemande(dem);
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new AdherentDao().update(ad);
        da.create(dem);
        if (da.verifNombre(act, d))
        {
            Evenement ev;
            List<Demande> allDemande = da.findDemandeForEvenement(act, d);
            if (act.isParEquipe())
            {
                int c = 1;
                List eqA,eqB;
                eqA = new ArrayList<Adherent>();
                eqB = new ArrayList<Adherent>();

                for (Demande iterDemande : allDemande)
                {
                    c = 1 - c;
                    if (c == 1)
                    {
                        eqA.add(iterDemande.getAdherent());
                    }
                    else
                    {
                        eqB.add(iterDemande.getAdherent());
                    }
                }
                ev = new EvenementAvecEquipe(eqA, eqB, d, allDemande);
            }
            else
            {
                List joueurs = new ArrayList<Adherent>();
                  for (Demande iterDemande : allDemande)
                {
                    joueurs.add(iterDemande.getAdherent());
                }
                  ev = new EvenementSansEquipe(joueurs, d, allDemande);
            }
            new EvenementDao().create(ev);
            da.validerDemande(allDemande); // a tester
        }
        JpaUtil.validerTransaction();
    }


    //------------- LECTURE UNIQUE DANS LA BASE DE DONNEES ---------------

    /**
     * Renvoi l’adherent s’il est connecté
     * @param mail
     * @return Adherent
     */
    public Adherent Connexion (String mail) throws ServiceException
    {
        try {
            JpaUtil.creerEntityManager();
            AdherentDao adao = new AdherentDao();
            JpaUtil.fermerEntityManager();

            return adao.findByMail(mail);
        } catch (Throwable ex) {
            throw new ServiceException("Le mail est inconnu");
        }
    }

    /**
     * Renvoi une activité en fonction de son nom
     * Lance une Exception si l’activité n’est pas dans la base de données
     * @param  name nom de l’activité
     * @return      Activité recherchée
     */
    public Activite ObtenirActivite(String name) throws ServiceException
    {
        JpaUtil.creerEntityManager();
        try {
            return new ActiviteDao().findByName(name);
        } catch (Throwable ex) {
            throw new ServiceException("activité inexistante");
        }
    }

    /**
     * Renvoi un evenement en fonction de son ID, peut lancer des
     * exceptions si aucun évenement ne correspond à l’id
     * @param  id identifiant de l’evenement
     * @return    Evenement correspondant à l’id
     */
    public Evenement ObtenirEvenement(Long id) throws ServiceException
    {
        try {
            return new EvenementDao().findById(id);
        } catch (Throwable ex) {
            throw new ServiceException("evenement inexistant");
        }
    }

    //------------- LECTURE DE LISTE DANS LA BASE DE DONNEES -------------

    /**
     * Permet de voir l’historique des demandes d’un adherent
     * @param  AdherentId Id de l’adherent souhaité (peut se faire avec
     *                    la méthode getId)
     * @return            liste des demandes
     */
    public List<Demande> voirDemandes(Long AdherentId)
    {
        JpaUtil.creerEntityManager();
        AdherentDao dao = new AdherentDao();
        try {
            return dao.findById(AdherentId).getDemandes();
        } catch (Throwable ex) {
            System.err.println("DEBUG: id non valide");
            throw new ServiceException("Id non valide");
        }
    }

    /**
     * Permet de voir toutes les activités
     * @return Liste des activités
     */
    List<Activite> voirActivites ()
    {
        JpaUtil.creerEntityManager();
        try {
            return new ActiviteDao().findAll();
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Permet de voir tous les adherents
     * @return Liste des adherents
     */
    List<Adherent> voirAdherent()
    {
        JpaUtil.creerEntityManager();
        try {
            return new AdherentDao().findAll();
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * Affiche tous les évenements dans la base de données.
     * @return
     */
    public List<Evenement> VoirEvenements ()
    {
        try
        {
            return new EvenementDao().findAll();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Renvoi toutes les demandes
     * @return   Liste de demandes
     */
    public List<Demande> voirDemandes()
    {
        JpaUtil.creerEntityManager();
        try {
            return new DemandeDao().findAll();
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Affiche tous les évenements non planifies dans la base de données
     * @return
     */
    public List<Evenement> voirEvenementsNonPlanifies ()
    {
        JpaUtil.creerEntityManager();
        List<Evenement> evts = new ArrayList<>();
        try {
            for(Evenement e : new EvenementDao().findAll())
            {
                if(!e.isPlanifie())
                    evts.add(e);
            }
            JpaUtil.fermerEntityManager();
            return evts;
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
            return null;
        }
    }

    /**
     * Affiche tous les évenements planifies dans la base de données
     * @return
     */
    public List<Evenement> voirEvenementsPlanifies ()
    {
        JpaUtil.creerEntityManager();
        List<Evenement> evts = new ArrayList<>();
        try {
            for(Evenement e : new EvenementDao().findAll())
            {
                if(e.isPlanifie())
                    evts.add(e);
            }
            JpaUtil.fermerEntityManager();
            return evts;
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
            return null;
        }
    }

    /**
     * Permet de repérer les différents participants à un evenement
     * @param  evt
     * @return     Liste de coordonnées au format LatLng (getLat et getLng
     *             renvoient des entiers)
     */
    public List<LatLng> voirParticpants(Evenement evt)
    {
        EvenementDao edao = new EvenementDao();
        List <LatLng> toReturn = new ArrayList();
        for (Adherent a : edao.obtenirParticipants(evt))
        {
            toReturn.add(a.getCoordonnees());
        }
        return toReturn;
    }

    /**
     *  Trouve les lieux les plus proches des membres d'un évènement.
     * @param evt
     * @param nombre nombre de lieux affichés
     * @return
     */
     public List<Lieu> voirLieuxPlusProche(Evenement evt, int nombre)
     {
         try {
             List<LatLng> locPart = LocaliserParticipants(evt);
             int barycentreLat = 0;
             int barycentreLng = 0;
             for (LatLng coord : locPart)
             {
                 barycentreLat+= coord.lat;
                 barycentreLng+= coord.lng;
             }
             barycentreLng/=locPart.size();
             barycentreLat/=locPart.size();

             List<Lieu> lieux = new LieuDao().findAll();
             ArrayList<Lieu> plusProches = new ArrayList();
             plusProches.addAll(lieux);
             tri_selection(plusProches, barycentreLng, barycentreLat);

             if(nombre < plusProches.size())
                 return plusProches.subList(0, nombre);
             return plusProches;

         } catch (Throwable ex) {
             return null;
         }
     }


    //------------- MODIFICATION DANS LA BASE DE DONNEES -----------------


    /**
     * Associe un évenement et un lieu
     * @param evt
     * @param lieu
     */
    public void AffecterLieux(Evenement evt, Lieu lieu) throws Throwable
    {
        evt.setLieu(lieu);
        evt.setPlanifie(true);
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new EvenementDao().update(evt);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        ServiceTechnique.EnvoyerMail(evt);
    }


    //------------- METHODES PRIVEES -------------------------------------

    /**
     * Tri le tableau passé en paramètre selon la distance par rapport
     * à barycentreLng et barycentreLat
     * @param t
     * @param barycentreLng
     * @param barycentreLat
     */
    private static void tri_selection(ArrayList<Lieu> t, int barycentreLng, int barycentreLat)
    {
        for(int i = 0; i < t.size(); i++) {
            int min = i;
            for(int j = i + 1 ; j < t.size(); j++)
            {
                if(abs(t.get(j).getLongitude() - barycentreLng)+abs(t.get(j).getLatitude() - barycentreLat) <
                        abs(t.get(min).getLongitude()-barycentreLng)+abs(t.get(min).getLatitude() - barycentreLat))
                {
                    min = j;
                }
            }
            if(min != i)
            {
                Lieu tmp = t.get(i);
                t.set(i,t.get(min));
                t.set(min, tmp);
            }
        }
    }
}
