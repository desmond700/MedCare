package com.example.medcare.patient;

import com.example.medcare.utilities.LetterSectionListItem;

import java.security.acl.LastOwnerException;
import java.util.Date;

public class Patient extends LetterSectionListItem {

  private int patientId;
  private String email;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private int age;
  private char gender;
  private Address address;
  private MedicalDiagnostic medicalDiagnostic;

  public Patient(){}

  public Patient(int patientId, String email, String firstName, String lastName, String dateOfBirth, int age, char gender, Address address, MedicalDiagnostic medicalDiagnostic) {
    this.patientId = patientId;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.age = age;
    this.gender = gender;
    this.address = address;
    this.medicalDiagnostic = medicalDiagnostic;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public char getGender() {
    return gender;
  }

  public void setGender(char gender) {
    this.gender = gender;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public MedicalDiagnostic getMedicalDiagnostic() {
    return medicalDiagnostic;
  }

  public void setMedicalDiagnostic(MedicalDiagnostic medicalDiagnostic) {
    this.medicalDiagnostic = medicalDiagnostic;
  }

  @Override
  public int getUniqueId() {
    return patientId;
  }

  @Override
  public void calculateSortString() {
    String sortString = "";
    sortString = (lastName.trim() + firstName.trim());
    sortString += address.getCity().toUpperCase().trim();
    setSortString(sortString);
  }

}
