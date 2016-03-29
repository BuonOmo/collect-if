/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Equipe;

/**
 *
 * @author Pierre
 */
public class EquipeDao {
     public void create(Equipe equipe) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(equipe);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Equipe update(Equipe equipe) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.merge(equipe);
        }
        catch(Exception e){
            throw e;
        }
    }
    
    public Equipe findById(long id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            return em.find(Equipe.class, id);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<Equipe> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Equipe> equipes = null;
        try {
            Query q = em.createQuery("SELECT e FROM Equipe e");
            equipes = (List<Equipe>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return equipes;
    }
}
