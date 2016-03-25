/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Adherent;
import metier.modele.Evenement;
import metier.modele.EvenementSansEquipe;

/**
 *
 * @author pbayle
 */
public class EvenementDao {
    public void create(Evenement evenement) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(evenement);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Evenement update(Evenement evenement) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.merge(evenement);
        }
        catch(Exception e){
            throw e;
        }
    }
    
    public Evenement findById(long id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.find(Evenement.class, id);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<Evenement> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> evenements = null;
        try {
            Query q = em.createQuery("SELECT e FROM Evenement e");
            evenements = (List<Evenement>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return evenements;
    }

    public List<Adherent> obtenirParticipants(Evenement evt) {
               
        return evt.getParticipants();
    }
}
