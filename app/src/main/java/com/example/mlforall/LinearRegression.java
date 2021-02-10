package com.example.mlforall;

/**
 * This class builds a simple machine learning model of Linear Regression with one x value, also known as Simple Linear Regression.
 * This is a simple model that builds the following equation: y = mx + b where m is the slope and b is the intercept.
 * Code example:
 * <pre>
 * {@code
 * double[] x = {1, 2, 3, 4, 5, 6, 7, 8};
 * double[] y = {1, 2, 5, 10, 13, 23, 31, 44};
 * double[] xTest = {1, 6, 8, 9, 10};
 * double[] yTest = {-1, 22, 31, 48, 53};
 *
 * OneFeatureLinearRegression model = new OneFeatureLinearRegression(x, y);
 *
 * System.out.println("Coefficients: " +Arrays.toString(model.getCoefficients()));
 * System.out.println("Predicted Value: " + model.predict(1.0));
 * System.out.println("Predicted Values: " + Arrays.toString(model.predict(xTest)));
 * System.out.println("Score: " + model.score(yTest, model.predict(xTest)));
 * }
 * </pre>
 *
 * <pre>
 * {@code
 * outputs:
 * Coefficients: [5.988095238095238, -10.821428571428573]
 * Predicted Value: 1.0
 * Predicted Values: [1.0, 23.0, 44.0, 43.07142857142857, 49.05952380952381]
 * Score: 0.8860972890825483
 * }
 * </pre>
 * @implNote We don't implement set method for {@link #linearEquation}, {@link #slope} and {@link #intercept} because this will prevent the model from working well.
 * We also don't implement get an set methods for {@link #x} and {@link #y}.
 * @see <a href="https://en.wikipedia.org/wiki/Simple_linear_regression">Linear Regression</a> (source: wikipedia)
 * @see LinearRegressionHelper
 * @author Tomer Reiner
 */
public class LinearRegression {
// TODO-change comments to {@link LinearEquation}
    private double[] x; // The x values for the LinearRegression.
    private double[] y; // The y values for the LinearRegression.
    private LinearEquation linearEquation;
    private double slope;
    private double intercept;

    public LinearRegression(double[] x, double[] y) throws IllegalArgumentException {
        this.x = x;
        this.y = y;

        if (this.x.length != this.y.length) // If the lengths of the arrays are not equal.
            throw new IllegalArgumentException(String.format("The length of the arrays don't match: %d, %d", this.x.length, this.y.length));
    }

    public LinearEquation getLinearEquation() {
        return this.linearEquation;
    }

    public double getSlope() {
        return this.slope;
    }

    public double getIntercept() {
        return this.intercept;
    }

    /**
     * This function fits the data to the model.
     */
    public void fit() {
        // fitting the model(creating the linear equation y = mx + b).
        this.linearEquation = LinearRegressionHelper.coefficients(this.x, this.y);
        this.slope = this.linearEquation.getSlope(); // Get the slope.
        this.intercept = this.linearEquation.getIntercept(); // Get the intercept.
    }

    /**
     * This function predicts the y value of <code>xValue</code> value based on {@link LinearRegression#x} and {@link LinearRegression#y} linear equation.
     * @param xValue The x value that we want to predict his y value.
     * @return The predicted value for x value.
     */
    private double predict(double xValue) {
        return this.slope * xValue + this.intercept;
    }

    /**
     * This function predicts the y value of <code>xValues</code> value based on {@link LinearRegression#x} and {@link LinearRegression#y} linear equation.
     * @param xValues The x values that we want to predict their y values.
     * @return The predicted values for x values.
     * @see #predict(double)
     */
    public double[] predict(double[] xValues) {
        int xValuesLength = xValues.length;
        double[] yPredicted = new double[xValuesLength]; // This array will store the predicted values xValues.

        for (int i = 0; i < xValuesLength; i++) // Putting the predicted values in the array.
            yPredicted[i] = predict(xValues[i]);
        return yPredicted;
    }

    /**
     * This function computes the score of the Machine Learning algorithm based on R^2 error
     * <a href="https://en.wikipedia.org/wiki/Coefficient_of_determination">R Squared</a>
     * @param xTest The x values.
     * @param yTest The y values.
     * @return The score of the Machine Learning model.
     * @see LinearRegressionHelper#mean(double[])
     */
    public double score(double[] xTest, double[] yTest) {
        double meanYTest = LinearRegressionHelper.mean(yTest); // The mean value of xTest

        double[] yPredicted = this.predict(xTest);

        double sumYTrueMinusMeanYTrue = 0;
        double sumYTrueMinusYPredicted = 0;

        int lengthYTrue = xTest.length;

        for (int i = 0; i < lengthYTrue; i++) {
            sumYTrueMinusMeanYTrue += Math.pow(yTest[i] - meanYTest, 2);
            sumYTrueMinusYPredicted += Math.pow(yTest[i] - yPredicted[i], 2);

        }
        return 1 - (sumYTrueMinusYPredicted / sumYTrueMinusMeanYTrue);
    }
}
