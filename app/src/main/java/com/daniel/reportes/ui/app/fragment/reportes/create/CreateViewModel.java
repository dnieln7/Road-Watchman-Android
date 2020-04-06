package com.daniel.reportes.ui.app.fragment.reportes.create;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daniel.reportes.data.Reporte;

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

    public void appendPictureName(String text) {
        this.pictureName.setValue(this.pictureName.getValue() + text);
    }
}