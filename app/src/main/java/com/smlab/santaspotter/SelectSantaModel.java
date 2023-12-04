package com.smlab.santaspotter;


import android.os.Parcel;
import android.os.Parcelable;

public class SelectSantaModel implements Parcelable {
    private int santaSticker;

    private int stickerImageResource;
    private boolean isLocked;
    private int stickerColor;

    public SelectSantaModel(int stickerImageResource, boolean isLocked, int stickerColor) {
        this.stickerImageResource = stickerImageResource;
        this.isLocked = isLocked;
        this.stickerColor = stickerColor;
    }

    public int getStickerImageResource() {
        return stickerImageResource;
    }

    public void setStickerImageResource(int stickerImageResource) {
        this.stickerImageResource = stickerImageResource;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getStickerColor() {
        return stickerColor;
    }

    public void setStickerColor(int stickerColor) {
        this.stickerColor = stickerColor;
    }

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
