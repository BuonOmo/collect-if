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

/**
 *
 * @author pbayle
 */
@Entity
public class EvenementAvecEquipe extends Evenement {
    private List<Adherent> EquipeA;
    private List<Adherent> EquipeB;

    public void setEquipeA(List<Adherent> EquipeA) {
        this.EquipeA = EquipeA;
    }

    public EvenementAvecEquipe(List<Adherent> EquipeA, List<Adherent> EquipeB, Date date, List<Demande> demandes) {
        super(date, demandes);
        this.EquipeA = EquipeA;
        this.EquipeB = EquipeB;
    }

    public EvenementAvecEquipe() {
    }
    

    public void setEquipeB(List<Adherent> EquipeB) {
        this.EquipeB = EquipeB;
    }

    public List<Adherent> getEquipeA() {
        return EquipeA;
    }

    public List<Adherent> getEquipeB() {
        return EquipeB;
    }

    @Override
    public String toString() {
        return "EvenementAvecEquipe{" + "EquipeA=" + EquipeA + ", EquipeB=" + EquipeB + '}';
    }

    @Override
    public List<Adherent> getParticipants() {
        List<Adherent> adh = new ArrayList();
        adh.addAll(EquipeA);
        adh.addAll(EquipeB);
        return adh;
    }
    
}
