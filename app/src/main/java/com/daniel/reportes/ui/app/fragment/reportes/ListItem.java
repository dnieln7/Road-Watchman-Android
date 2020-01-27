package com.daniel.reportes.ui.app.fragment.reportes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.daniel.reportes.R;

public class ListItem {

    ImageView picture;
    TextView location;
    TextView description;
    CheckBox fixed;


    public ListItem(View view) {
        picture = view.findViewById(R.id.reportePicture);
        location = view.findViewById(R.id.reporteLocation);
        description = view.findViewById(R.id.reporteDescription);
        fixed = view.findViewById(R.id.reporteFixed);
    }
}
