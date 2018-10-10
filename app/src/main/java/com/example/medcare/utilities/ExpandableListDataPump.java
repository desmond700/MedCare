package com.example.medcare.utilities;

import com.example.medcare.patient.MedicalDiagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
  public static HashMap<String, List<MedicalDiagnostic>> getData() {
    HashMap<String, List<MedicalDiagnostic>> expandableListDetail = new HashMap<>();

    List<MedicalDiagnostic> bloodpressure = new ArrayList<>();
    bloodpressure.add(new MedicalDiagnostic(130,89,150,25, 60));

    List<MedicalDiagnostic> bloodpressurelvl = new ArrayList<>();
    bloodpressurelvl.add(new MedicalDiagnostic(130,89,150,25, 60));

    List<MedicalDiagnostic> cholesterol = new ArrayList<>();
    cholesterol.add(new MedicalDiagnostic(130,89,150,25, 60));

    List<MedicalDiagnostic> bmi = new ArrayList<>();
    bmi.add(new MedicalDiagnostic(130,89,150,25, 60));

    List<MedicalDiagnostic> ekg = new ArrayList<>();
    ekg.add(new MedicalDiagnostic(130,89,150,25, 60));

    expandableListDetail.put("BLOOD PRESSURE", bloodpressure);
    expandableListDetail.put("BLOOD GLUCOSE LEVEL", bloodpressurelvl);
    expandableListDetail.put("CHOLESTEROL", cholesterol);
    expandableListDetail.put("BMI", bmi);
    expandableListDetail.put("EKG", ekg);
    return expandableListDetail;
  }
}
