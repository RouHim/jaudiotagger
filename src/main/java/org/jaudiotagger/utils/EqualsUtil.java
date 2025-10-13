package org.jaudiotagger.utils;

import java.util.Objects;

/**
 * Collected methods which allow easy implementation of <code>equals</code>.
 * <p>
 * Example use case in a class called Car:
 * <pre>
 * public boolean equals(Object aThat){
 * if ( this == aThat ) return true;
 * if ( !(aThat instanceof Car) ) return false;
 * Car that = (Car)aThat;
 * return
 * EqualsUtil.areEqual(this.fName, that.fName) &amp;&amp;
 * EqualsUtil.areEqual(this.fNumDoors, that.fNumDoors) &amp;&amp;
 * EqualsUtil.areEqual(this.fGasMileage, that.fGasMileage) &amp;&amp;
 * EqualsUtil.areEqual(this.fColor, that.fColor) &amp;&amp;
 * Arrays.equals(this.fMaintenanceChecks, that.fMaintenanceChecks); //array!
 * }
 * </pre>
 *
 * <em>Arrays are not handled by this class</em>.
 * This is because the <code>Arrays.equals</code> methods should be used for
 * array fields.
 */
public final class EqualsUtil {

    public static boolean areEqual(boolean aThis, boolean aThat) {
        //log.info("boolean");
        return aThis == aThat;
    }

    public static boolean areEqual(char aThis, char aThat) {
        //log.info("char");
        return aThis == aThat;
    }

    public static boolean areEqual(long aThis, long aThat) {
        /*
         * Implementation Note
         * Note that byte, short, and int are handled by this method, through
         * implicit conversion.
         */
        //log.info("long");
        return aThis == aThat;
    }

    public static boolean areEqual(float aThis, float aThat) {
        //log.info("float");
        return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
    }

    public static boolean areEqual(double aThis, double aThat) {
        //log.info("double");
        return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
    }

    /**
     * Possibly-null object field.
     * <p>
     * Includes type-safe enumerations and collections, but does not include
     * arrays. See class comment.
     */
    public static boolean areEqual(Object aThis, Object aThat) {
        //log.info("Object");
        return Objects.equals(aThis, aThat);
    }
}
