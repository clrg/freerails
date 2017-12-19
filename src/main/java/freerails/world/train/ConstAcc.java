/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created on 09-Jul-2005
 *
 */
package freerails.world.train;

import freerails.util.Utils;

import java.io.Serializable;

/**
 *
 */
strictfp public class ConstAcc implements Serializable,
        SpeedAgainstTime {

    /**
     *
     */
    public static final ConstAcc STOPPED = new ConstAcc(0, 0, 0, 0);
    private static final long serialVersionUID = -2180666310811530761L;
    private final double u, a, finalS, finalT;

    private ConstAcc(double a, double t, double u, double s) {
        this.a = a;
        this.finalT = t;
        this.u = u;
        this.finalS = s;
    }

    /**
     * @param u
     * @param a
     * @param s
     * @return
     */
    public static ConstAcc uas(double u, double a, double s) {
        double t = calcT(u, a, s);
        return new ConstAcc(a, t, u, s);
    }

    private static double calcT(double u, double a, double s) {
        // Note, Utils.solveQuadratic throws an exception if a == 0
        return a == 0 ? s / u : Utils.solveQuadratic(a * 0.5d, u, -s);
    }

    /**
     * @param u
     * @param a
     * @param t
     * @return
     */
    public static ConstAcc uat(double u, double a, double t) {
        double s = u * t + a * t * t / 2;
        return new ConstAcc(a, t, u, s);
    }

    public double calcS(double t) {
        if (t == finalT)
            return finalS;
        validateT(t);
        double ds = u * t + a * t * t / 2;
        ds = Math.min(ds, finalS);
        return ds;
    }

    public double calcT(double s) {
        if (s == finalS)
            return finalT;
        if (s < 0 || s > this.finalS)
            throw new IllegalArgumentException(s + " < 0 || " + s + " > "
                    + finalS);
        double returnValue = calcT(u, a, s);
        returnValue = Math.min(returnValue, finalT);
        return returnValue;
    }

    public double calcV(double t) {
        validateT(t);
        return u + a * t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConstAcc))
            return false;

        final ConstAcc constAcc = (ConstAcc) o;

        if (a != constAcc.a)
            return false;
        if (finalT != constAcc.finalT)
            return false;
        return !(u != constAcc.u);
    }

    public double calcA(double t) {
        validateT(t);
        return a;
    }

    public double getT() {
        return finalT;
    }

    public double getS() {
        return finalS;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = u != +0.0d ? Double.doubleToLongBits(u) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = a != +0.0d ? Double.doubleToLongBits(a) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        temp = finalT != +0.0d ? Double.doubleToLongBits(finalT) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    private void validateT(double t) {
        if (t < 0 || t > finalT)
            throw new IllegalArgumentException("(" + t + " < 0 || " + t + " > "
                    + finalT + ")");

    }

    @Override
    public String toString() {
        return "ConstAcc [a=" + a + ", u=" + u + ", dt=" + finalT + "]";
    }

}
