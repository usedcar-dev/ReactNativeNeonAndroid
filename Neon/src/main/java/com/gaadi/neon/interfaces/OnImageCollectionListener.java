package com.gaadi.neon.interfaces;

import com.gaadi.neon.enumerations.ResponseCode;
import com.gaadi.neon.model.NeonResponse;
import com.gaadi.neon.util.FileInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @author princebatra
 * @version 1.0
 * @since 3/2/17
 */
public interface OnImageCollectionListener {

    void imageCollection(NeonResponse neonResponse);

}
