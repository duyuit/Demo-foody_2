package Model;

import java.io.Serializable;

/**
 * Created by Billy on 9/14/2017.
 */

public class Restaurant implements Serializable{
    String title,address,email,website,restype,food,status;
    double lati,longti;

    public Restaurant(String title, String address, String restype, String food, double lati, double longti, byte[] image) {
        this.title = title;
        this.address = address;
        this.restype = restype;
        this.food = food;
        this.lati = lati;
        this.longti = longti;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRestype() {
        return restype;
    }

    public void setRestype(String restype) {
        this.restype = restype;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Restaurant(String title, String address, String restype, byte[] image) {

        this.title = title;
        this.address = address;
        this.restype = restype;
        this.image = image;
    }

    byte[] image,logo;

}
