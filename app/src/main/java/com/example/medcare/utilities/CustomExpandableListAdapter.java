package com.example.medcare.utilities;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.medcare.R;
import com.example.medcare.patient.MedicalDiagnostic;

import at.grabner.circleprogress.CircleProgressView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

  private Context context;
  private List<String> expandableListTitle;
  private HashMap<String, List<MedicalDiagnostic>> expandableListDetail;

  public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                     HashMap<String, List<MedicalDiagnostic>> expandableListDetail) {
    this.context = context;
    this.expandableListTitle = expandableListTitle;
    this.expandableListDetail = expandableListDetail;
  }

  @Override
  public Object getChild(int listPosition, int expandedListPosition) {
    return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
      .get(expandedListPosition);
  }

  @Override
  public long getChildId(int listPosition, int expandedListPosition) {
    return expandedListPosition;
  }

  @Override
  public View getChildView(int listPosition, final int expandedListPosition,
                           boolean isLastChild, View convertView, ViewGroup parent) {
    final MedicalDiagnostic expandedListItem = (MedicalDiagnostic) getChild(listPosition, expandedListPosition);
    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.list_item, null);
    }
    CircleProgressView mCircleView = convertView.findViewById(R.id.circleView);

    TextView expandedListTextView = (TextView) convertView
      .findViewById(R.id.diagnostic_msg);


    if(this.expandableListTitle.get(listPosition).equals("BLOOD PRESSURE")){

      mCircleView.setValue(expandedListItem.getBloodPressure());
      mCircleView.setUnit("mmHg");
      expandedListTextView.setText(expandedListItem.getBloodPressureMsg());

    }else if(this.expandableListTitle.get(listPosition).equals("BLOOD GLUCOSE LEVEL")){

      mCircleView.setValue(expandedListItem.getBloodGlucoseLvl());
      mCircleView.setUnit("mg/dL");
      expandedListTextView.setText(expandedListItem.getBloodGlucoseLvlMsg());

    }else if(this.expandableListTitle.get(listPosition).equals("CHOLESTEROL")){

      mCircleView.setValue(expandedListItem.getCholesterol());
      mCircleView.setUnit("mg/dl");
      expandedListTextView.setText(expandedListItem.getCholesterolMsg());

    }else if(this.expandableListTitle.get(listPosition).equals("BMI")) {

      mCircleView.setValue(expandedListItem.getBmi());
      mCircleView.setUnit("kg/m2");
      expandedListTextView.setText(expandedListItem.getBmiMsg());

    }else if(this.expandableListTitle.get(listPosition).equals("EKG")){

      mCircleView.setValue(expandedListItem.getElectrocardiogram());
      mCircleView.setUnit("bpm");
      expandedListTextView.setText(expandedListItem.getElectrocardiogramMsg());

    }

    return convertView;
  }

  @Override
  public int getChildrenCount(int listPosition) {
    return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
      .size();
  }

  @Override
  public Object getGroup(int listPosition) {
    return this.expandableListTitle.get(listPosition);
  }

  @Override
  public int getGroupCount() {
    return this.expandableListTitle.size();
  }

  @Override
  public long getGroupId(int listPosition) {
    return listPosition;
  }

  @Override
  public View getGroupView(int listPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    String listTitle = (String) getGroup(listPosition);
    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.context.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.list_row, null);
    }

    TextView listTitleTextView = (TextView) convertView
      .findViewById(R.id.listTitle);
    listTitleTextView.setTypeface(null, Typeface.BOLD);
    listTitleTextView.setText(listTitle);
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int listPosition, int expandedListPosition) {
    return true;
  }
}
