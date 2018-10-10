package com.example.medcare.patient;

public class Address {

  private String street;
  private String apt;
  private String city;
  private String province;
  private String postalCode;
  private String country;

  public Address(){}

  public Address(String st, String apt, String city, String pr, String pcode){
    this.street = st;
    this.apt = apt;
    this.city = city;
    this.province = pr;
    this.postalCode = pcode;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getApt() {
    return apt;
  }

  public void setApt(String apt) {
    this.apt = apt;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
