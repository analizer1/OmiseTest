package net.analizer.domainlayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Charity implements Parcelable {
    @SerializedName("id")
    public Long id;

    @SerializedName("name")
    public String name;

    @SerializedName("logo_url")
    public String logoUrl;

    public Charity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logoUrl);
    }

    protected Charity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.logoUrl = in.readString();
    }

    public static final Parcelable.Creator<Charity> CREATOR = new Parcelable.Creator<Charity>() {
        @Override
        public Charity createFromParcel(Parcel source) {
            return new Charity(source);
        }

        @Override
        public Charity[] newArray(int size) {
            return new Charity[size];
        }
    };
}
