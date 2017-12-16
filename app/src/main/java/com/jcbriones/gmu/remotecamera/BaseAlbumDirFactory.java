package com.jcbriones.gmu.remotecamera;

import java.io.File;
import android.os.Environment;

/**
 * Created by jayzybriones on 12/5/17.
 */

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
