package com.jcbriones.gmu.remotecamera;

/**
 * Created by jayzybriones on 12/5/17.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
