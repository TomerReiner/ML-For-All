package com.example.mlforall;

/**
 * This class helps setting the coefficients in {@link LinearRegression}
 */
public class LinearRegressionHelper {


    /**
     * This function computes the sum of a double array.
     * @param arr The array which we want to sum.
     * @return The sum of arr.
     * @see #sum(double[])
     */
    public static double sum(double[] arr) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++)
            sum += arr[i];
        return sum;
    }

    /**
     * This function computes the mean(average) value of a double array.
     * @param arr The array which we want to find his mean.
     * @return Mean(average) value of the values in <code>arr</code>.
     * @see #sum(double[])
     */
    public static double mean(double[] arr) {
        return sum(arr) / arr.length;
    }

    /**
     * This function computes the variance of arr.
     * <pre>
     * {@code
     * double[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
     * System.out.println(variance(arr));
     * }
     * output:
     * 42.0
     * </pre>
     * @param arr The array which we want to compute its variance.
     * @return The variance of arr.
     * @see #mean(double[])
     */
    public static double variance(double[] arr) {
        double meanArr = mean(arr); // The mean of the arr
        double[] arrCopy = new double[arr.length];

        for (int i = 0; i < arrCopy.length; i++) {
            arrCopy[i] = 0;
            arrCopy[i] = Math.pow((arr[i] - meanArr), 2);
        }
        return sum(arrCopy);
    }

    /**
     * This function computes the covariance between x and y.
     * <pre>Code Example:
     * </pre>
     * <pre>
     * {@code
     * double[] x = {1, 2, 3, 4, 5, 6, 7, 8};
     * double[] y = {1, 2, 5, 10, 13, 23, 31, 44};
     * System.out.println(covariance(x, y));
     * }
     * output:
     * 251.5
     * </pre>
     * @param x X values array.
     * @param y Y values array.
     * @return The covariance between x and y.
     * @see #mean(double[])
     */
    public static double covariance(double[] x, double[] y) {
        double[] xCopy = x;
        double[] yCopy = y;

        double meanX = mean(xCopy);
        double meanY = mean(yCopy);

        double covariance = 0;

        for (int i = 0; i < xCopy.length; i++)
            covariance += (xCopy[i] - meanX) * (yCopy[i] - meanY);
        return covariance;
    }

    /**
     * This function computes the slope and intercept for x and y. The slope is m and intercept is b in the equation: y = mx + b.
     * <pre>Code Example:
     * </pre>
     * <pre>
     * {@code
     * double[] x = {1, 2, 3, 4, 5, 6, 7, 8};
     * double[] y = {1, 2, 5, 10, 13, 23, 31, 44};
     * System.out.println(Arrays.toString(coefficients(x, y)));
     * }
     * output:
     * [5.988095238095238, -10.821428571428573]
     * </pre>
     * @param x the x values.
     * @param y the y values.
     * @return array in the size of two.
     * The item in the first index will be the slope, and the item in the second index will be the intercept.
     * @see #mean(double[])
     * @see #variance(double[])
     * @see #covariance(double[], double[])
     */
    public static double[] coefficients(double[] x, double[] y){
        double[] xCopy = x;
        double[] yCopy = y;

        double meanX = mean(xCopy);
        double meanY = mean(yCopy);

        double slope = covariance(xCopy, yCopy) / variance(xCopy);
        double intercept = meanY - (slope * meanX);

        return new double[] {slope, intercept};
    }
}
