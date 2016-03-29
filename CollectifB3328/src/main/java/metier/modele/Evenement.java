/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pbayle
 */
@Entity
@Inheritance (strategy= InheritanceType.JOINED)
public abstract class Evenement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected boolean planifie;
    @ManyToOne
    protected Lieu lieu;

    @Temporal (TemporalType.DATE)
    protected Date date;
    @OneToMany
    protected List<Demande> demandes;
    @ManyToOne
    protected Activite activite;
    
    
    
    public Evenement() {
    }
    
    public Evenement(Date date, List<Demande> demandes, Activite act) {
        this.planifie = false;
        this.lieu = null;
        this.date = date;
        this.activite = act;
        this.demandes = demandes;
    }

    public Long getId() {
        return id;
    }

    public void setPlanifie(boolean planifie) {
        this.planifie = planifie;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setActivite(Activite a) {
        this.activite = a;
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }

    public boolean isPlanifie() {
        return planifie;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public Date getDate() {
        return date;
    }
    
    public Activite getActivite() {
        return activite;
    }

    public List<Demande> getDemandes() {
        return demandes;
    }
    
    public abstract List<Adherent> getParticipants();
    
    @Override
    public String toString() {
        return "Evenement{" + "id=" + id + ", planifie=" + planifie + ", lieu=" + lieu + ", date=" + date + ", demandes=" + demandes + ", activite=" + activite + '}';
    }
}
