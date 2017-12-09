package net.analizer.domainlayer.models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("WeakerAccess")
public class CreditCartInfo implements Parcelable {

    private String creditCardNo;
    private String creditCardHolderName;
    private String creditCardExpiry;
    private String creditCardCCV;

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

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public String getCreditCardHolderName() {
        return creditCardHolderName;
    }

    public String getCreditCardCCV() {
        return creditCardCCV;
    }

    public int getExpiryMonth() {
        String[] split = creditCardExpiry.split("/");
        return Integer.parseInt(split[0].trim());
    }

    public int getExpiryYear() {
        String[] split = creditCardExpiry.split("/");
        return Integer.parseInt(split[1].trim());
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public void setCreditCardHolderName(String creditCardHolderName) {
        this.creditCardHolderName = creditCardHolderName;
    }

    public void setCreditCardExpiry(String creditCardExpiry) {
        this.creditCardExpiry = creditCardExpiry;
    }

    public void setCreditCardCCV(String creditCardCCV) {
        this.creditCardCCV = creditCardCCV;
    }

    public boolean isValid() {
        return (creditCardHolderName != null && creditCardHolderName.length() > 0)
                && (creditCardNo != null && creditCardNo.length() > 0)
                && (creditCardExpiry != null && creditCardExpiry.length() > 0)
                && (creditCardCCV != null && creditCardCCV.length() > 0)
                && (creditCardCCV.matches("\\b[0-9]{3}\\b"))
                && (creditCardExpiry.matches("^[\\d ]+/[\\d ]+"));
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
