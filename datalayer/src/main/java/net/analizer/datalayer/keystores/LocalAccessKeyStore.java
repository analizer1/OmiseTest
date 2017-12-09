package net.analizer.datalayer.keystores;

import net.analizer.datalayer.AppConfig;
import net.analizer.domainlayer.utils.CryptoUtils;

import java.math.BigInteger;

public class LocalAccessKeyStore implements AccessKeyStore {

    @Override
    public String getAccessKey() {
        BigInteger bigIntKey = new BigInteger(AppConfig.OMISE_PKEY);
        return CryptoUtils.decode(bigIntKey);
    }

    @Override
    public int getRequestCC() {
        return AppConfig.REQUEST_CC;
    }
}
