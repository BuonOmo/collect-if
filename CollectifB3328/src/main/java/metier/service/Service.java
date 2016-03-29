
package metier.service;
import com.google.maps.model.LatLng;
import dao.ActiviteDao;
import dao.AdherentDao;
import dao.DemandeDao;
import dao.EquipeDao;
import dao.EvenementDao;
import dao.JpaUtil;
import dao.LieuDao;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;
import metier.modele.Equipe;
import metier.modele.Evenement;
import metier.modele.EvenementAvecEquipe;
import metier.modele.EvenementSansEquipe;
import metier.modele.Lieu;
import util.GeoTest;
/**
 *
 * @author ubuonomo
 */
public class Service {
    
     /**
     * Inscription de l'adherent a
     * @param a 
     * @throws Throwable
     */
    public void Inscription (Adherent a) throws Throwable
    {
        JpaUtil.creerEntityManager();
        AdherentDao adao = new AdherentDao();
        if(adao.findByMail(a.getMail()) == null)
        {
            a.setCoordonnees(GeoTest.getLatLng(a.getAdresse()));
            JpaUtil.ouvrirTransaction();
            new AdherentDao().create(a);
            JpaUtil.validerTransaction();            
        }
        else
        {
            Exception e = new Exception();
            throw e;
        }
        JpaUtil.fermerEntityManager();
        
    }
    
    /**
     * Renvoi l’adherent s’il est connecté
     * @param mail
     * @return Adherent
     */
    public Adherent Connexion (String mail)
    {
        try {
            JpaUtil.creerEntityManager();
            AdherentDao adao = new AdherentDao();
            Adherent ad = adao.findByMail(mail);
            JpaUtil.fermerEntityManager();
            return ad;
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Permet d’envoyer une requete d’evenement pour un adherent
     * Renvoi une Exception si la demande existe déjà
     * @param   ad  Adherent demandant l’evenement
     * @param   act Activité souhaitée par l’adherent
     * @param   d   Date ou l’adherent souhaite réaliser l’évenement
     * @return Vrai si la demande a été enregistrée, faux sinon.
     * @throws java.lang.Throwable
     */       
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
        JpaUtil.validerTransaction();
        JpaUtil.ouvrirTransaction();
        if (da.verifNombre(act, d))
        {
            Evenement ev;
            List<Demande> allDemande = da.findDemandeForEvenement(act, d);
            if (act.isParEquipe())
            {
                int c = 1;
                List<Adherent> eqA,eqB;
                eqA = new ArrayList<>();
                eqB = new ArrayList<>();

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
                Equipe equipeA = new Equipe(eqA);
                Equipe equipeB = new Equipe(eqB);
                EquipeDao eqdao = new EquipeDao();
                eqdao.create(equipeA);
                eqdao.create(equipeB);
                
                ev = new EvenementAvecEquipe(equipeA, equipeB, d, allDemande, act);
            }
            else
            {
                List joueurs = new ArrayList<>();
                  for (Demande iterDemande : allDemande)
                {
                    joueurs.add(iterDemande.getAdherent());
                }
                  ev = new EvenementSansEquipe(joueurs, d, allDemande, act);
            }
            new EvenementDao().create(ev);
            da.validerDemande(allDemande); // a tester
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        return true;
    }
       
    /**
     * Renvoie l'activité correspondant au nom.
     * @param name
     * @return L'activité correspondante
     */
    public Activite ObtenirActivite(String name) {
        JpaUtil.creerEntityManager();        
        try {
            return new ActiviteDao().findByName(name);
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
            return null;
        }        
    }
    
            
    /**
     * Affecte le lieu à l'évènement.
     * @param evt 
     * @param lieu 
     * @throws java.lang.Throwable 
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
    
    public List<LatLng> LocaliserParticipants(Evenement evt)
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
    public List<Lieu> trouverLieuxPlusProche(Evenement evt, int nombre)
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
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Tri le tableau passé en paramètre selon la distance par rapport à barycentreLng et barycentreLat
     * @param t
     * @param barycentreLng
     * @param barycentreLat 
     */
    private static void tri_selection(ArrayList<Lieu> t, int barycentreLng, int barycentreLat) {
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
    
    /**
     * Récupère l'evenement 
     * @param id
     * @return
     * @throws Throwable 
     */
    public Evenement ObtenirEvenement(Long id) throws Throwable {
        try {
            return new EvenementDao().findById(id);
        } catch (Throwable ex) {
            throw ex;
        }
    }
    
    /********************* Fonctions d'insertion en base au démarrage **********************/
    
    /**
     * Insertion en base de nb adhérents.
     * @param nb 
     */
    public void InsertionAdherentsEnBase(int nb) {
        JpaUtil.creerEntityManager();
        AdherentDao adao = new AdherentDao();
        JpaUtil.ouvrirTransaction();
        for(int i = 1; i < nb+1; i++)
        {
            Adherent a = new Adherent("nom"+i, "prenom"+i, i+" Avenue des champs-elysées", "mail"+i);
            a.setCoordonnees(GeoTest.getLatLng(a.getAdresse()));
            try {
                adao.create(a);
            } catch (Throwable ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * Insertion de nb activités en base.
     * @param nb 
     */
    public void InsertionActivitesEnBase(int nb) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        ActiviteDao adao = new ActiviteDao();
        boolean parEquipe;
        for(int i = 1; i < nb+1; i++)
        {
            parEquipe = i%2==0;
                    
            Activite ac = new Activite("Activité "+i, parEquipe, i);
            try {
                adao.create(ac);
            } catch (Throwable ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /**
     * Insertion en base de nb Lieux au démarrage.
     * @param nb 
     */
    public void InsertionLieuxEnBase(int nb) {
        JpaUtil.creerEntityManager();
        LieuDao ldao = new LieuDao();
        JpaUtil.ouvrirTransaction();
        for(int i = 1; i < nb+1; i++)
        {
           Lieu l = new Lieu("Lieu " + i, "Type " +i, i*2 + " Rue de la paix");
           l.setCoordonnees(GeoTest.getLatLng(l.getAdresse()));
            try {
                ldao.create(l);
            } catch (Throwable ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    /********************* Fin fonctions d'insertion en base au démarrage *****************/
    
    
    /********************* Fonctions pour les affichages de listes ************************/    
    
    /**
     * Renvoi la liste des demandes
     * @param AdherentId
     * @return Liste des demandes pour l'adherent
     */
    public List<Demande> voirHistorique(Long AdherentId)
    {
        JpaUtil.creerEntityManager();
        AdherentDao dao = new AdherentDao();
        try {
            return dao.findById(AdherentId).getDemandes();
        } catch (Throwable ex) {
            System.err.println("DEBUG: id non valide");            
        }
        JpaUtil.fermerEntityManager();
        return null;
    }    
    /**
     * Retourne toutes les activités
     * @return
     */
    public List<Activite> voirActivites ()
    {
        JpaUtil.creerEntityManager();
        try {
            return new ActiviteDao().findAll();
        } catch (Throwable ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.fermerEntityManager();
        return null;
    }
    
    /**
     * Retourne la liste des adhérents
     * @return
     */
    public List<Adherent> voirAdherents()
    {
        JpaUtil.creerEntityManager();
        try {
            return new AdherentDao().findAll();
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
            return null;
        }        
    }
    
    /**
     * Retourne tous les lieux
     * @return
     */
    public List<Lieu> voirLieux() {
        JpaUtil.creerEntityManager();
        try {
            return new LieuDao().findAll();
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
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
            return evts;
        } catch (Throwable ex) {
            JpaUtil.fermerEntityManager();
            return null;
        }
    }
    
    /********************* Fin fonctions pour les affichages de listes ************************/
}
