package com.gaadi.neon.model;

import com.gaadi.neon.interfaces.INeutralParam;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public class NeutralModel {

    public PhotosMode setParams(INeutralParam iParam) {
        return new PhotosMode(iParam);
    }
}
