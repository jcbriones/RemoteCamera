package com.jcbriones.gmu.remotecamera;

import java.io.File;

/**
 * Created by jayzybriones on 12/5/17.
 */

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
