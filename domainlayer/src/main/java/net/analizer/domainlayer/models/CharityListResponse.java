package net.analizer.domainlayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CharityListResponse implements Parcelable {
    public List<Charity> charityList;

    public CharityListResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.charityList);
    }

    protected CharityListResponse(Parcel in) {
        this.charityList = in.createTypedArrayList(Charity.CREATOR);
    }

    public static final Parcelable.Creator<CharityListResponse> CREATOR = new Parcelable.Creator<CharityListResponse>() {
        @Override
        public CharityListResponse createFromParcel(Parcel source) {
            return new CharityListResponse(source);
        }

        @Override
        public CharityListResponse[] newArray(int size) {
            return new CharityListResponse[size];
        }
    };
}
