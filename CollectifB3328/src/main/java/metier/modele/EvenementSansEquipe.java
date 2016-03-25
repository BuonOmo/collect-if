/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author pbayle
 */
@Entity
public class EvenementSansEquipe extends Evenement {
    private List<Adherent> participants;

    public void setJoueurs(List<Adherent> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "EvenementSansEquipe{" + "participants=" + participants + '}';
    }

    public EvenementSansEquipe(List<Adherent> participants, Date date, List<Demande> demandes) {
        super(date, demandes);
        this.participants = participants;
    }

    public EvenementSansEquipe() {
    }

    public List<Adherent> getParticipants() {
        return participants;
    }
}
