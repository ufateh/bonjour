package com.example.bonjour;

/**
 * Created by Sanjay Kumar on 4/10/2015.
 */
public class Lift {
    public int Id;
    public String name;
    public String destination;
    public String vacancy;
    public String contactNo;
    public String time;
    public String slat;
    public String slon;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getContactNo() {

        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String elat;
    public String elon;


    public String getElat() {
        return elat;
    }

    public void setElat(String elat) {
        this.elat = elat;
    }

    public String getSlat() {
        return slat;
    }

    public void setSlat(String slat) {
        this.slat = slat;
    }

    public String getSlon() {
        return slon;
    }

    public void setSlon(String slon) {
        this.slon = slon;
    }

    public String getElon() {
        return elon;
    }

    public void setElon(String elon) {
        this.elon = elon;
    }



    public Lift() {

    }


    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Lift(String name, String dest, String vacancy, String time,String slat,String slon,String elat,String elon) {
        this.name = name;
        this.destination = dest;
        this.vacancy = vacancy;
        this.time = time;
        this.slat=slat;
        this.slon=slon;
        this.elat=elat;
        this.elon=elon;
    }

}
