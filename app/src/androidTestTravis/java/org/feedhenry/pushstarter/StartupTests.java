package org.feedhenry.pushstarter;

import android.test.AndroidTestCase;

import junit.framework.Assert;

/**
 * Created by summers on 6/25/16.
 */
public class StartupTests extends AndroidTestCase {

    public void testGoogleServices() {
        Assert.assertNotNull(getContext().getResources().getString(R.string.gcm_defaultSenderId));
    }


}
