package com.vad.qrscanner.pojos;

public class Contact {
    private String name;
    private String lastname;
    private String numberPhone;

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

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public Contact() {
    }

    public Contact(String numberPhone, String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.numberPhone = numberPhone;
    }
}
