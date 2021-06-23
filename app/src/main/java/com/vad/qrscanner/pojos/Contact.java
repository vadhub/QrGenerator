package com.vad.qrscanner.pojos;

public class Contact {
    private String name;
    private String lastname;
    private int numberPhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(int numberPhone) {
        this.numberPhone = numberPhone;
    }

    public Contact() {
    }

    public Contact(String name, String lastname, int numberPhone) {
        this.name = name;
        this.lastname = lastname;
        this.numberPhone = numberPhone;
    }
}
