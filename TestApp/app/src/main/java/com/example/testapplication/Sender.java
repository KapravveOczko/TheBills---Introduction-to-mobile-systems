package com.example.testapplication;

public class Sender {

    String string;
    String name;
    String surname;

    public Sender() {
    }

    public Sender(String string, String name, String surname) {
        this.string = string;
        this.name = name;
        this.surname = surname;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
