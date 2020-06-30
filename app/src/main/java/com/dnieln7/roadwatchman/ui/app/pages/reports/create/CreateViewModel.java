package com.dnieln7.roadwatchman.ui.app.pages.reports.create;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dnieln7.roadwatchman.data.model.Reporte;

public class CreateViewModel extends ViewModel {

    private MutableLiveData<Reporte> reporte;
    private MutableLiveData<Boolean> picture;
    private MutableLiveData<Uri> pictureUri;
    private MutableLiveData<String> pictureName;

    public CreateViewModel() {
        reporte = new MutableLiveData<>();
        picture = new MutableLiveData<>(false);
        pictureUri = new MutableLiveData<>();
        pictureName = new MutableLiveData<>();
    }

    public MutableLiveData<Reporte> getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte.setValue(reporte);
    }

    public MutableLiveData<Boolean> getPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture.setValue(picture);
    }

    public MutableLiveData<Uri> getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri.setValue(pictureUri);
    }

    public MutableLiveData<String> getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName.setValue(pictureName);
    }

    public void appendUserId(int id) {
        String name = pictureName.getValue();

        if (name != null) {
            String[] nameSplit = name.split("\\.");
            String type = nameSplit[nameSplit.length - 1];
            String formattedName = name.replace("." + type, "_" + id + "." + type);

            this.pictureName.setValue(formattedName);
        }
    }
}
