package net.analizer.domainlayer.models;

import com.google.gson.annotations.SerializedName;

public class DonationResponse {
    @SerializedName("success")
    public Boolean success;

    public DonationResponse() {
    }

    public DonationResponse(Boolean success) {
        this.success = success;
    }
}
