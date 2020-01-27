package com.daniel.reportes.ui.app.fragment.reportes;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daniel.reportes.data.Reporte;

public class ReporteViewModel extends ViewModel {

    private MutableLiveData<Reporte> reporte;
    private MutableLiveData<Uri> pictureUri;
    private MutableLiveData<String> pictureName;

    public ReporteViewModel() {
        reporte = new MutableLiveData<>();
        pictureUri = new MutableLiveData<>();
        pictureName = new MutableLiveData<>();
    }

    public void setReporte(Reporte reporte) {
        this.reporte.setValue(reporte);
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri.setValue(pictureUri);
    }

    public void setPictureName(String pictureName) {
        this.pictureName.setValue(pictureName);
    }

    public MutableLiveData<Reporte> getReporte() {
        return reporte;
    }

    public MutableLiveData<Uri> getPictureUri() {
        return pictureUri;
    }

    public MutableLiveData<String> getPictureName() {
        return pictureName;
    }
}
