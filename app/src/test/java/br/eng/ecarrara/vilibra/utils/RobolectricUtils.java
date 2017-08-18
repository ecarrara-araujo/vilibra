package br.eng.ecarrara.vilibra.utils;

import android.content.ContentProvider;

import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowLog;

import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract;
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraProvider;

public class RobolectricUtils {

    public static void redirectLogsToSystemOutput() {
        ShadowLog.stream = System.out;
    }

    public static void setupVilibraContentProvider() {
        VilibraProvider vilibraProvider = new VilibraProvider();
        vilibraProvider.onCreate();
        setupContentProvider(VilibraContract.CONTENT_AUTHORITY, vilibraProvider);
    }

    public static void setupContentProvider(String contentAuthority,
                                            ContentProvider contentProvider) {
        ShadowContentResolver.registerProvider(contentAuthority, contentProvider);
    }

}
