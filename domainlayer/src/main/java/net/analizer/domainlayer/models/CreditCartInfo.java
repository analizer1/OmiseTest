package net.analizer.domainlayer.models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("WeakerAccess")
public class CreditCartInfo implements Parcelable {

    public String creditCardNo;
    public String creditCardHolderName;
    public String creditCardExpiry;
    public String creditCardCCV;

    public CreditCartInfo() {
    }

    public CreditCartInfo(String creditCardNo,
                          String creditCardHolderName,
                          String creditCardExpiry,
                          String creditCardCCV) {

        this.creditCardNo = creditCardNo;
        this.creditCardHolderName = creditCardHolderName;
        this.creditCardExpiry = creditCardExpiry;
        this.creditCardCCV = creditCardCCV;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.creditCardNo);
        dest.writeString(this.creditCardHolderName);
        dest.writeString(this.creditCardExpiry);
        dest.writeString(this.creditCardCCV);
    }

    protected CreditCartInfo(Parcel in) {
        this.creditCardNo = in.readString();
        this.creditCardHolderName = in.readString();
        this.creditCardExpiry = in.readString();
        this.creditCardCCV = in.readString();
    }

    public static final Parcelable.Creator<CreditCartInfo> CREATOR = new Parcelable.Creator<CreditCartInfo>() {
        @Override
        public CreditCartInfo createFromParcel(Parcel source) {
            return new CreditCartInfo(source);
        }

        @Override
        public CreditCartInfo[] newArray(int size) {
            return new CreditCartInfo[size];
        }
    };
}
