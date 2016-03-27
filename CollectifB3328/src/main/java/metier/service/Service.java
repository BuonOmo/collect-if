

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
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("Le mail est inconnu");
        }
        return null;
    }

    /**
     * Permet de voir l’historique des demandes d’un adherent
     * @param  AdherentId Id de l’adherent souhaité (peut se faire avec
     *                    la méthode getId)
     * @return            liste des demandes
     */
    public List<Demande> voirHistorique(Long AdherentId)
    {
        JpaUtil.creerEntityManager();
        AdherentDao dao = new AdherentDao();
        try {
            return dao.findById(AdherentId).getDemandes();
        } catch (Throwable ex) {
            System.err.println("DEBUG: id non valide");
            return null;
        }
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
            throw new ServiceException("Demande déjà existante")

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
            throw new ServiceException("activité inexistante")
        }
    }

    /**
     * Renvoi toutes les demandes
     * @return   Liste de demandes
     */
    public List<Demande> getDemandes(Adherent a)
    {
        JpaUtil.creerEntityManager();
        try {
            return new DemandeDao().findAll();
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void EnvoyerMail () {} // Service Technique

    /**
     * Affiche tous les évenements dans la base de données
     * @return
     */
    public List<Evenement> AfficherEvenements () throws Throwable
    {
        return new EvenementDao().findAll();
    }

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

    /**
     * Permet de repérer les différents participants à un evenement
     * @param  evt [description]
     * @return     Liste de coordonnées au format LatLng (getLat et getLng
     *             renvoient des entiers)
     */
    public List<LatLng> LocaliserParticpants(Evenement evt)
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
     *  Trouve les lieux les plus proches des membres d'un évenement.
     * @param evt
     * @param nombre nombre de lieux affichés
     * @return
     */
    public List<Lieu> trouverLieuxPlusProche(Evenement evt, int nombre) // TODO tester cette methode
    {
        try {
            List<LatLng> locPart = LocaliserParticpants(evt);
            int barycentreLat = 0;
            int barycentreLng = 0;
            for (LatLng coord : locPart)
            {
                barycentreLat+= coord.getLat;
                barycentreLng+= coord.getLng;
            }
            barycentreLng/=locPart.size();
            barycentreLat/=locPart.size();

            ArrayList<Lieu> lieux = new LieuDao().findAll();
            ArrayList<Lieu> plusProches = new ArrayList();
            plusProches.push(lieux.getFirst());
            for (Lieu lieu : lieux)
            {
                int i = 0;
                for (Lieu tmp : plusProches)
                {
                    if (abs(lieu.getLng - barycentreLng)+abs(lieu.getLat - barycentreLat) <
                        abs(tmp.getLng-barycentreLng)+abs(tmp.getLat - barycentreLat))
                    {
                        plusProches.add(i, lieu);
                    }
                    ++i;
                }
            }
            return plusProches.subList(0, nombre);
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Evenement ObtenirEvenement(Long id) throws Throwable {
        try {
            return new EvenementDao().findById(id);
        } catch (Throwable ex) {
            // TODO throw qque chose
            throw ex;
        }
    }
}
