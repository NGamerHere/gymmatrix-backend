package com.coderstack.gymmatrix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String equipmentName;
    private String equipmentPhotoLink;
    private String remarks;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Equipment Name
    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    // Equipment Photo Link
    public String getEquipmentPhotoLink() {
        return equipmentPhotoLink;
    }

    public void setEquipmentPhotoLink(String equipmentPhotoLink) {
        this.equipmentPhotoLink = equipmentPhotoLink;
    }

    // Remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }
}
