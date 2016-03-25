
package metier.service;
import com.google.maps.model.LatLng;
import dao.ActiviteDao;
import dao.AdherentDao;
import dao.DemandeDao;
import dao.EvenementDao;
import dao.JpaUtil;
import dao.LieuDao;
import java.util.ArrayList;
import java.util.Date;
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
    public void Inscription (Adherent a) throws Throwable
    {
        a.setCoordonnees(GeoTest.getLatLng(a.getAdresse()));
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        new AdherentDao().create(a);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
    
    /**
     * 
     * @param mail
     * @return vrai si connecté
     */
    public Adherent Connexion (String mail)
    {
        try {
            JpaUtil.creerEntityManager();
            AdherentDao adao = new AdherentDao();
            JpaUtil.fermerEntityManager();

            return adao.findByMail(mail);
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        // throw new ServiceException("Le mail est inconnu")
        return null;
    }
    
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
    
    public boolean demanderEvenement(Adherent ad, Activite act, Date d) throws Throwable
    {
        Demande dem = new Demande(act, ad, d);
        DemandeDao da = new DemandeDao();
        if (da.existe(dem))
            return false;
        
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
        return true;
    }
    
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
    
    List<Adherent> voirAdherent()
    {
        JpaUtil.creerEntityManager();
        try {
            return new AdherentDao().findAll();
        } catch (Throwable ex) {
            return null;
        }
    }
   
    public Activite ObtenirActivite(String name) {
        JpaUtil.creerEntityManager();        
        try {
            return new ActiviteDao().findByName(name);
        } catch (Throwable ex) {
            return null;
        }
    }
    
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
     * 
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
    public List<Lieu> trouverLieuxPlusProche(Evenement evt, int nombre) // TODO pour l'instant uniquement tous les lieux
    {
        try {
            return new LieuDao().findAll();
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
