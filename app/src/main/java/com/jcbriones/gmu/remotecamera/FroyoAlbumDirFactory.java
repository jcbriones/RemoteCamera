package com.jcbriones.gmu.remotecamera;

import java.io.File;

import android.os.Environment;

/**
 * Created by jayzybriones on 12/5/17.
 */

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

    @Override
    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }
}

