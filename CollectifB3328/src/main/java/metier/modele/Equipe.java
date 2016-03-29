/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Pierre
 */
@Entity
public class Equipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany
    private List<Adherent> joueurs;

    public Equipe() {
    }

    public Equipe(List<Adherent> joueurs) {
        this.joueurs = joueurs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Adherent> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<Adherent> joueurs) {
        this.joueurs = joueurs;
    }

    @Override
    public String toString() {
        return "Equipe{" + "id=" + id + ", joueurs=" + joueurs + '}';
    }
       
}
