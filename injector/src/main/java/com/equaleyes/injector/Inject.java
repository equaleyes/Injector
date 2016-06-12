package com.equaleyes.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Žan Skamljič on 3. 08. 2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    int id() default -1;
}
