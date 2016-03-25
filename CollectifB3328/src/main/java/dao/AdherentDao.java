package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Adherent;

public class AdherentDao {
    
    public void create(Adherent adherent) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(adherent);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Adherent update(Adherent adherent) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            adherent = em.merge(adherent);
        }
        catch(Exception e){
            throw e;
        }
        return adherent;
    }
    
    public Adherent findById(long id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        try {
            adherent = em.find(Adherent.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return adherent;
    }
    
    public Adherent findByMail(String mail) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        Query q = em.createQuery("SELECT a FROM Adherent a WHERE a.mail=:mail");
        q.setParameter("mail", mail);
        try
        {
            adherent = (Adherent) q.getSingleResult();
        } catch (Throwable e)
        {
            System.err.println("DEBUG : mauvais email - "+mail);
            return null;
        }
        return adherent;
    }
    
    public List<Adherent> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Adherent> adherents = null;
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a");
            adherents = (List<Adherent>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return adherents;
    }
}
