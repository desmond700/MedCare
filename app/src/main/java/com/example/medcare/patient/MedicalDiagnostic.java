package com.example.medcare.patient;

public class MedicalDiagnostic {

  private int bloodPressure;
  private float bloodGlucoseLvl;
  private int cholesterol;
  private float bmi;
  private int electrocardiogram;

  public MedicalDiagnostic() {
  }

  public MedicalDiagnostic(int bloodPressure, float bloodGlucoseLvl, int cholesterol, float bmi, int electrocardiogram) {
    this.bloodPressure = bloodPressure;
    this.bloodGlucoseLvl = bloodGlucoseLvl;
    this.cholesterol = cholesterol;
    this.bmi = bmi;
    this.electrocardiogram = electrocardiogram;
  }

  public int getBloodPressure() {
    return bloodPressure;
  }

  public void setBloodPressure(int bloodPressure) {
    this.bloodPressure = bloodPressure;
  }

  public float getBloodGlucoseLvl() {
    return bloodGlucoseLvl;
  }

  public void setBloodGlucoseLvl(int bloodGlucoseLvl) {
    this.bloodGlucoseLvl = bloodGlucoseLvl;
  }

  public int getCholesterol() {
    return cholesterol;
  }

  public void setCholesterol(int cholesterol) {
    this.cholesterol = cholesterol;
  }

  public float getBmi() {
    return bmi;
  }

  public void setBmi(int bmi) {
    this.bmi = bmi;
  }

  public int getElectrocardiogram() {
    return electrocardiogram;
  }

  public void setElectrocardiogram(int electrocardiogram) {
    this.electrocardiogram = electrocardiogram;
  }

  // Message
  public String getBloodPressureMsg() {

    if(bloodPressure <= 120){

      return "Normal";

    }else if(bloodPressure >= 120 || bloodPressure <= 129){

      return "Elevated";

    }else if(bloodPressure >= 130 || bloodPressure <= 139){

      return "High Blood pressure\n(Hypertension)Stage 1";

    }else if(bloodPressure >= 140){

      return "High Blood pressure\n(Hypertension)Stage 2";

    }else if(bloodPressure >= 180){

      return "Hypertension Crisis\n(Seek Emergency Care)";

    }
    return "";
  }

  public String getBloodGlucoseLvlMsg() {

    if(bloodGlucoseLvl < 70){

      return "Low blood sugar";

    }else if(bloodGlucoseLvl >= 70 || bloodGlucoseLvl <= 100){

      return "Normal";

    }else if(bloodGlucoseLvl >= 101 || bloodGlucoseLvl <= 125){

      return "Pre-diabetes";

    }else if(bloodGlucoseLvl > 126){

      return "Diabetes";

    }

    return "";
  }

  public String getCholesterolMsg() {

    if(cholesterol < 200){

      return "Desirable";

    }else if(cholesterol >= 200 || cholesterol <= 239){

      return "Borderline High";

    }else if(cholesterol > 240){

      return "High";

    }

    return "";
  }

  public String getBmiMsg() {

    if(bmi < 18.5){
      return "Underweight";

    }else if(bmi >= 18.5 || bmi <= 25){

      return "Normal";

    }else if(bmi >= 25 || bmi <= 30){

      return "Overweight";

    }else if(bmi > 30){

      return "Obese";

    }
    return "";
  }

  public String getElectrocardiogramMsg() {

    if(electrocardiogram < 60){
      return "Bradycardia";

    }else if(electrocardiogram >= 60 || electrocardiogram <= 100){

      return "Normal";

    }else if(electrocardiogram > 100 ){

      return "Tachycardia";

    }
    return "";
  }


}
