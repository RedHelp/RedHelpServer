package org.redhelp.util;

import org.redhelp.common.exceptions.DependencyException;

public class Assert {

    public static void assertNotNull(Object o, String msg) {
	if(o == null)
	    throw new DependencyException(msg);
    }
}
