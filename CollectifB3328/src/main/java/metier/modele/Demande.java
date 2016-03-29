/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ubuonomo
 */
@Entity
public class Demande implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Activite activite;
    @ManyToOne
    private Adherent adherent;
    @Temporal (TemporalType.DATE)
    private Date date;
    private boolean hasEvenement;
    
    public Demande() {
    }

    @Override
    public String toString() {
        return "Activité : " + activite.getDenomination() + "\nDate demandée : " + date.toString().substring(0,10); 
    }

    public boolean isHasEvenement() {
        return hasEvenement;
    }

    public void setHasEvenement(boolean hasEvenement) {
        this.hasEvenement = hasEvenement;
    }

  
    
    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Activite getActivite() {
        return activite;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public Date getDate() {
        return date;
    }

    public Demande(Activite activite, Adherent adherent, Date date) {
        this.activite = activite;
        this.adherent = adherent;
        this.date = date;
        hasEvenement = false;
    }
    
}
