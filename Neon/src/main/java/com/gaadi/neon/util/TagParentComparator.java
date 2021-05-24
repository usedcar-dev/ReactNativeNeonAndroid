package com.gaadi.neon.util;

import java.util.Comparator;

/**
 * @author dipanshugarg
 * @version 1.0
 * @since 25/1/17
 */
public class TagParentComparator implements Comparator<FileInfo> {


    @Override
    public int compare(FileInfo lhs, FileInfo rhs) {
        return  1;
        //return lhs.getParent_name().compareTo(rhs.getParent_name());
    }
}
