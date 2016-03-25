package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Activite;

public class ActiviteDao {
    
    public void create(Activite activite) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(activite);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Activite update(Activite activite) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            activite = em.merge(activite);
        }
        catch(Exception e){
            throw e;
        }
        return activite;
    }
    
    public Activite findById(long id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.find(Activite.class, id);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<Activite> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Activite> activites = null;
        try {
            Query q = em.createQuery("SELECT a FROM Activite a");
            activites = (List<Activite>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }

    public Activite findByName(String name) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Query q = em.createQuery("SELECT a FROM Activite a WHERE a.denomination=:d");
        q.setParameter("d", name);
        try
        {
            return (Activite) q.getSingleResult();
        } catch (Throwable e)
        {
            System.err.println("DEBUG : mauvaise activité - "+name);
            return null;
        }
    }
}
