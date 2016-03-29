/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author pbayle
 */
@Entity
public class EvenementAvecEquipe extends Evenement {
    @OneToOne
    private Equipe EquipeA;
    @OneToOne
    private Equipe EquipeB;

    public void setEquipeA(Equipe EquipeA) {
        this.EquipeA = EquipeA;
    }

    public EvenementAvecEquipe(Equipe EquipeA, Equipe EquipeB, Date date, List<Demande> demandes, Activite act) {
        super(date, demandes, act);
        this.EquipeA = EquipeA;
        this.EquipeB = EquipeB;
    }

    public EvenementAvecEquipe() {
    }
    

    public void setEquipeB(Equipe EquipeB) {
        this.EquipeB = EquipeB;
    }

    public Equipe getEquipeA() {
        return EquipeA;
    }

    public Equipe getEquipeB() {
        return EquipeB;
    }

    @Override
    public String toString() {
        String toReturn = "Date événement : " + date.toString().substring(0,10) + "\nActivité : " + activite.getDenomination();
        toReturn += "\nÉquipe A :";
        for(Adherent ad : EquipeA.getJoueurs()) {
            toReturn += "\n\tAdhérent : " + ad.toString();
        }
        toReturn += "\nÉquipe B :";
        for(Adherent ad : EquipeB.getJoueurs()) {
            toReturn += "\n\tAdhérent : " + ad.toString();
        }
        return toReturn;
    }

    @Override
    public List<Adherent> getParticipants() {
        List<Adherent> adh = new ArrayList<>();
        adh.addAll(EquipeA.getJoueurs());
        adh.addAll(EquipeB.getJoueurs());
        return adh;
    }
    
}
