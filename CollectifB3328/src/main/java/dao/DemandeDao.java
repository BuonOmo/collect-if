package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;

public class DemandeDao {
    
    public void create(Demande demande) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(demande);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Demande update(Demande demande) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.merge(demande);
        }
        catch(Exception e){
            throw e;
        }
    }
    
    public Demande findById(long id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.find(Demande.class, id);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<Demande> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> demandes = null;
        try {
            Query q = em.createQuery("SELECT a FROM Demande a");
            demandes = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return demandes;
    }

    public List<Demande> findByAdherentId(Adherent Adherent) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query q = em.createQuery("SELECT d FROM Demande d WHERE d.adherent=:identifiant");
            q.setParameter("identifiant", Adherent);
            return (List<Demande>) q.getResultList();
            
        }
        catch(Exception e) {
            System.out.println("va dans l'excpetion");
            throw e;
        }        
    }

    public boolean verifNombre(Activite act, Date d) {
        try {
            return this.findDemandeForEvenement(act, d).size() == act.getNbParticipants();
        }
        catch(Exception e) {
            System.out.println("va dans l'excpetion");
            throw e;
        }   
    }

    public boolean existe(Demande dem) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query q = em.createQuery("SELECT COUNT(d) FROM Demande d WHERE d.date=:dat AND d.activite=:act AND d.adherent=:ad");
            q.setParameter("dat", dem.getDate());
            q.setParameter("act", dem.getActivite());
            q.setParameter("ad", dem.getAdherent());
            return (Long)q.getSingleResult() > 0;
            
        }
        catch(Exception e) {
            System.out.println("va dans l'excpetion");
            throw e;
        }   
    }

    public List<Demande> findDemandeForEvenement(Activite act, Date d) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query q = em.createQuery("SELECT d FROM Demande d WHERE d.date=:dat AND d.activite=:act AND d.hasEvenement=false");
            q.setParameter("dat", d);
            q.setParameter("act", act);
            return q.getResultList();
            
        }
        catch(Exception e) {
            System.out.println("va dans l'excpetion");
            throw e;
        }   
    }

    public void validerDemande(List<Demande> allDemande) throws Throwable {
        for (Demande d : allDemande)
        {
            d.setHasEvenement(true);
            this.update(d);
        }
    }
}
