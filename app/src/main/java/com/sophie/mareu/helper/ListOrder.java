package com.sophie.mareu.helper;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ListOrder {

    public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;
    public static final int FILTERED = 2;
    public static final int SORTED = 3;
    public static final int UNCHANGED = -1;
    @Retention(RetentionPolicy.SOURCE)

    @IntDef({ASCENDING,DESCENDING,FILTERED,SORTED,UNCHANGED})
    @interface ListState{}
}
