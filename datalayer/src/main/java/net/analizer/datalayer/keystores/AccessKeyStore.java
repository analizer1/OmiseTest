package net.analizer.datalayer.keystores;

public interface AccessKeyStore {
    /**
     * @return OMISE's API access key
     */
    String getAccessKey();

    /**
     * @return  OMISE's Request CC, which is what? exactly?
     */
    int getRequestCC();
}
