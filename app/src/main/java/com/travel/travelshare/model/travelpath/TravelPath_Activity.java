package com.travel.travelshare.model.travelpath;

import com.travel.travelshare.model.DatabaseItem;
import java.util.List;

public class TravelPath_Activity implements DatabaseItem {
    private String id;
    private String nom;
    private String geohash;
    private String photo;
    private String photo_claire;
    private String telephone;
    private String description;
    private Double prix;
    private List<Double> localisation; // [latitude, longitude]
    private List<String> search_labels;

    public TravelPath_Activity() {
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getGeohash() { return geohash; }
    public void setGeohash(String geohash) { this.geohash = geohash; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getPhoto_claire() { return photo_claire; }
    public void setPhoto_claire(String photo_claire) { this.photo_claire = photo_claire; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public List<Double> getLocalisation() { return localisation; }
    public void setLocalisation(List<Double> localisation) { this.localisation = localisation; }

    public List<String> getSearch_labels() { return search_labels; }
    public void setSearch_labels(List<String> search_labels) { this.search_labels = search_labels; }
}