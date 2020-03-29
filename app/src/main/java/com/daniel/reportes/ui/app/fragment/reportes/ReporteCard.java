package com.daniel.reportes.ui.app.fragment.reportes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.daniel.reportes.R;

public class ReporteCard {

    private ImageView picture;
    private TextView location;
    private TextView description;
    private CheckBox fixed;


    public ReporteCard(View view) {
        picture = view.findViewById(R.id.reportePicture);
        location = view.findViewById(R.id.reporteLocation);
        description = view.findViewById(R.id.reporteDescription);
        fixed = view.findViewById(R.id.reporteFixed);
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    public TextView getLocation() {
        return location;
    }

    public void setLocation(TextView location) {
        this.location = location;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public CheckBox getFixed() {
        return fixed;
    }

    public void setFixed(CheckBox fixed) {
        this.fixed = fixed;
    }
}
