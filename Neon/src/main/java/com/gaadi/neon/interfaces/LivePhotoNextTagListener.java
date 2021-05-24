package com.gaadi.neon.interfaces;

import com.gaadi.neon.util.FileInfo;

/**
 * @author Pavan
 * @version 1.0
 * @since 12/7/17
 */

public interface LivePhotoNextTagListener {
    boolean updateExifInfo(FileInfo fileInfo);
    void onNextTag();
}
