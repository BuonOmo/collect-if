package metier.modele;

import com.google.maps.model.LatLng;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

 
@Entity
public class Adherent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private String adresse;
    private Double longitude;
    private Double latitude;
    @OneToMany
    private List<Demande> demandes;

    public Adherent() {
    }

    public List<Demande> getDemandes() {
        return demandes;
    }
    
    public void addDemande(Demande d)
    {
        demandes.add(d);
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }

    public Adherent(String nom, String prenom, String adresse,  String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.adresse = adresse;
        this.demandes = new ArrayList<Demande>();
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCoordonnees(LatLng latLng) {
        this.longitude = latLng.lng;
        this.latitude = latLng.lat;
    }
    
    public LatLng getCoordonnees() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Adherent{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", adresse=" + adresse + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }
}