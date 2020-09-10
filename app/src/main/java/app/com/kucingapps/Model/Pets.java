package app.com.kucingapps.Model;

import com.google.gson.annotations.SerializedName;

public class Pets {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("species")
    private String species;
    @SerializedName("breed")
    private String breed;
    @SerializedName("gender")
    private int gender;
    @SerializedName("birth")
    private String birth;
    @SerializedName("picture")
    private String picture;
    @SerializedName("uid")
    private int uid;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String message;
    @SerializedName("pid")
    private String pid;
    @SerializedName("riwayat")
    private String riwayat;
    @SerializedName("kesehatan")
    private String kesehatan;
    @SerializedName("vaksinasi")
    private String vaksinasi;
    @SerializedName("pengobatan")
    private String pengobatan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(String riwayat) {
        this.riwayat = riwayat;
    }

    public String getKesehatan() {
        return kesehatan;
    }

    public void setKesehatan(String kesehatan) {
        this.kesehatan = kesehatan;
    }

    public String getVaksinasi() {
        return vaksinasi;
    }

    public void setVaksinasi(String vaksinasi) {
        this.vaksinasi = vaksinasi;
    }

    public String getPengobatan() {
        return pengobatan;
    }

    public void setPengobatan(String pengobatan) {
        this.pengobatan = pengobatan;
    }
}
