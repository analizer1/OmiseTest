package net.analizer.domainlayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Donation implements Parcelable {

    @SerializedName("name")
    public String name;

    @SerializedName("token")
    public String token;

    @SerializedName("amount")
    public Long amount;

    public Donation() {
    }

    public Donation(String name, String token, Long amount) {
        this.name = name;
        this.token = token;
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.token);
        dest.writeValue(this.amount);
    }

    protected Donation(Parcel in) {
        this.name = in.readString();
        this.token = in.readString();
        this.amount = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Donation> CREATOR = new Parcelable.Creator<Donation>() {
        @Override
        public Donation createFromParcel(Parcel source) {
            return new Donation(source);
        }

        @Override
        public Donation[] newArray(int size) {
            return new Donation[size];
        }
    };
}
