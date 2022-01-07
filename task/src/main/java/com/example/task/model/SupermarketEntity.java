package com.example.task.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@Entity
@Table(name = "SUPERMARKETS")
public class SupermarketEntity {
    private Long id;
    private String englishName;
    private String arabicName;
    private String address;
    private byte[] image;

    private boolean activated = true;
    public SupermarketEntity() {}
    public SupermarketEntity(String englishName,String arabicName,String address,byte[]image)
    {
	this.englishName=englishName;
	this.arabicName=arabicName;
	this.address = address;
	this.image = image;
	this.activated=true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Basic
    @Column(name = "ARABIC_NAME")
    public String getArabicName() {
	return arabicName;
    }

    public void setArabicName(String arabicName) {
	this.arabicName = arabicName;
    }

    @Basic
    @Column(name = "ENGLISH_NAME")
    public String getEnglishName() {
	return englishName;
    }

    public void setEnglishName(String englishName) {
	this.englishName = englishName;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    @Basic
    @Column(name = "ACTIVATED", columnDefinition = "boolean default true")
    public boolean getActivated() {
	return activated;
    }

    public void setActivated(boolean activated) {
	this.activated = activated;
    }

    @Lob
    @Column(name = "IMAGE"//,// columnDefinition ="MEDIUMBLOB"//
    )
    public byte[] getImage() {
	return image;
    }

    public void setImage(byte[] image) {
	this.image = image;
    }


}
