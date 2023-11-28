package com.smlab.santaspotter;

//public class SelectSantaModel {
//    int santaSticker;
//
//    public SelectSantaModel(int santaSticker) {
//        this.santaSticker = santaSticker;
//    }
//
//    public int getSantaSticker() {
//        return santaSticker;
//    }
//
//    public void setSantaSticker(int santaSticker) {
//        this.santaSticker = santaSticker;
//    }
//}

import android.os.Parcel;
import android.os.Parcelable;

public class SelectSantaModel implements Parcelable {
    private int santaSticker;

    public SelectSantaModel(int santaSticker) {
        this.santaSticker = santaSticker;
    }

    public int getSantaSticker() {
        return santaSticker;
    }

    public void setSantaSticker(int santaSticker) {
        this.santaSticker = santaSticker;
    }

    // Parcelable implementation
    protected SelectSantaModel(Parcel in) {
        santaSticker = in.readInt();
    }

    public static final Creator<SelectSantaModel> CREATOR = new Creator<SelectSantaModel>() {
        @Override
        public SelectSantaModel createFromParcel(Parcel in) {
            return new SelectSantaModel(in);
        }

        @Override
        public SelectSantaModel[] newArray(int size) {
            return new SelectSantaModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(santaSticker);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
