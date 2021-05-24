package com.gaadi.neon.interfaces;

import com.gaadi.neon.model.NeonResponse;

/**
 * @author Pavan
 * @version 1.0
 * @since 11/7/17
 */

public interface LivePhotosListener {
    void onLivePhotoCollected(NeonResponse neonResponse);
}
