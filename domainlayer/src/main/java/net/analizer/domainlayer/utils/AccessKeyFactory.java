package net.analizer.domainlayer.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccessKeyFactory {

    @Inject
    public AccessKeyFactory() {
    }

    /**
     * @return Always return a new instance of AccessKeyStore's implementation.
     */
    public AccessKeyStore createAccessKeyStore() {
        // Implement logic of choosing which implementation to be instantiated here
        // but at the moment, we have only 1 simple implementation here.
        return new LocalAccessKeyStore();
    }
}
