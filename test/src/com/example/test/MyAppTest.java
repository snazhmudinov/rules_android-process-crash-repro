package com.example.test;

import static org.junit.Assert.assertTrue;

import com.example.app.Helper;
import com.example.app.MyApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * This test references app classes directly. When filter_zip_exclude strips
 * these classes from the test APK, the classloader loads R8-processed versions
 * from the app APK instead, causing LinkageError / AbstractMethodError.
 */
@RunWith(AndroidJUnit4.class)
public class MyAppTest {

    @Test
    public void testHelperInitialized() {
        // Helper.class was removed from test APK by filter_zip_exclude.
        // Classloader loads R8-processed version from app APK.
        // If R8 inlined or repackaged Helper, this crashes with NoClassDefFoundError.
        assertTrue(Helper.isInitialized());
    }
}
