package com.equaleyes.injector;

/**
 * Created by zan on 7/4/16.
 */
public @interface InjectList {
    int[] ids() default {};
}
