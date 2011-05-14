package net.renalias.xmlvalidator.support;

/**
 * Created by IntelliJ IDEA.
 * User: Oscar Renalias
 * Date: 14/05/11
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public class Assert {
    /**
     * Throws a runtime exception if the given object is null
     * @param o
     */
    public static void notNull(Object o) {
        if(o == null)
            throw(new RuntimeException("Object of type " + o.getClass().getName() + " is null"));
    }
}
