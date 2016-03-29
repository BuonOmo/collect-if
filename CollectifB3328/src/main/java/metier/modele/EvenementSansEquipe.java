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

/**
 *
 * @author pbayle
 */
@Entity
public class EvenementSansEquipe extends Evenement {
    @OneToMany
    private List<Adherent> participants;

    public void setJoueurs(List<Adherent> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        String toReturn = "Date événement : " + date.toString().substring(0,10) + "\nActivité : " + activite.getDenomination();
        for(Adherent ad : participants) {
            toReturn += "\nAdhérent : " + ad.toString();
        }
        return toReturn;
    }

    public EvenementSansEquipe(List<Adherent> participants, Date date, List<Demande> demandes, Activite act) {
        super(date, demandes, act);
        this.participants = participants;
    }

    public EvenementSansEquipe() {
    }

    @Override
    public List<Adherent> getParticipants() {
        List<Adherent> adh = new ArrayList<>();
        adh.addAll(participants);
        return adh;
    }
}
