package com.example.mlforall;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class helps setting the coefficients in {@link LinearRegression}
 * and splitting the data to training and testing data.
 */
public class LinearRegressionHelper {

    public static final double TRAIN_SPLIT = 0.8;
    public static final int X_TRAIN_INDEX = 0;
    public static final int X_TEST_INDEX = 1;
    public static final int Y_TRAIN_INDEX = 2;
    public static final int Y_TEST_INDEX = 3; // The indexes for the variables.

    public static final String X_TRAIN = "xTrain";
    public static final String X_TEST = "xTest";
    public static final String Y_TRAIN = "yTrain";
    public static final String Y_TEST = "yTest";


    /**
     * This function computes the sum of a double array.
     * @param arr The array which we want to sum.
     * @return The sum of arr.
     * @see #sum(double[])
     */
    private static double sum(double[] arr) {
        double sum = 0;
        for (double v : arr)
            sum += v;
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
    private static double variance(double[] arr) {
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
    private static double covariance(double[] x, double[] y) {
        double[] xCopy = x.clone();
        double[] yCopy = y.clone();

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
     * @return {@link LinearEquation} of the slope and intercept.
     * The item in the first index will be the slope, and the item in the second index will be the intercept.
     * @see #mean(double[])
     * @see #variance(double[])
     * @see #covariance(double[], double[])
     */
    public static LinearEquation coefficients(double[] x, double[] y){
        double[] xCopy = x.clone();
        double[] yCopy = y.clone();

        double meanX = mean(xCopy);
        double meanY = mean(yCopy);

        double slope = covariance(xCopy, yCopy) / variance(xCopy);
        double intercept = meanY - (slope * meanX);

        return new LinearEquation(slope, intercept);
    }

    /**
     * This function creates {@link ArrayList} of random indexes for training data.
     * @param size The dataset size.
     * @return random indexes fot training data.
     */
    private static ArrayList<Integer> randomTrainingIndexes(int size) {
        int trainSize = (int) (size * TRAIN_SPLIT);
        ArrayList<Integer> randomIndexes = new ArrayList<>();
        int randIndex = (int) (Math.random() * size);
        for (int i = 0; i < trainSize; i++) {
            while (randomIndexes.contains(randIndex))
                randIndex = (int) (Math.random() * size);
            randomIndexes.add(randIndex);
        }
        return randomIndexes;
    }

    /**
     * This function create {@link ArrayList} of the testing indexes. The function will iterate through
     * <code>size</code> <code>for (int i = 0; i < size; i++)</code> and will add every
     * <code>i</code> that is not in <code>trainingIndexes</code>.
     * @param size The dataset size.
     * @param trainingIndexes The training indexes.
     * @return The testing indexes.
     * @see #randomTrainingIndexes(int)
     */
    private static ArrayList<Integer> testingIndexes(int size, ArrayList<Integer> trainingIndexes) {
        ArrayList<Integer> testingIndexes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (!trainingIndexes.contains(i))
                testingIndexes.add(i);
        }
        return testingIndexes;
    }

    /**
     * This function loads the training/testing data to {@link ArrayList}.
     * @param x The x data.
     * @param y The y data.
     * @param randomIndexes The random indexes for training/testing data.
     * @return {@link ArrayList} with size of 2.
     * The first item will be training/testing X data and the second item will be training/testing y data.
     */
    private static ArrayList<double[]> getShuffledData(double[] x, double[] y, ArrayList<Integer> randomIndexes) {
        int randomIndexesSize = randomIndexes.size();

        ArrayList<double[]> data = new ArrayList<>();
        double[] xData = new double[randomIndexesSize];
        double[] yData = new double[randomIndexesSize];

        for (int i = 0; i < randomIndexesSize; i++) {
            int currentIndex = randomIndexes.get(i);
            xData[i] = x[currentIndex];
            yData[i] = y[currentIndex];
        }

        data.add(xData);
        data.add(yData);
        return data;
    }

    /**
     * This function splits the data to training and testing data.
     * 80% of the data will go to train the model, and the other 20% of the data will go to testing the model.
     * @param x The x data.
     * @param y The y data.
     * @return {@link ArrayList} with size of 4, The first item will be training X data(xTrain),
     * the second item will be testing X data(xTest),
     * the third item will be training y data(yTrain),
     * and the last item will be testing y data(yTest).
     */
    public static ArrayList<double[]> splitData(double[] x, double[] y) {
        ArrayList<Integer> trainingIndexes = randomTrainingIndexes(x.length);
        ArrayList<Integer> testingIndexes = testingIndexes(x.length, trainingIndexes);

        ArrayList<double[]> trainingData = getShuffledData(x, y, trainingIndexes);
        ArrayList<double[]> testingData = getShuffledData(x, y, testingIndexes); // Getting the training and testing data.

        double[] xTrain = trainingData.get(0);
        double[] yTrain = trainingData.get(1);

        double[] xTest = testingData.get(0);
        double[] yTest = testingData.get(1); // Unpacking the data.

        sortData(xTrain, yTrain);
        sortData(xTest, yTest);


        ArrayList<double[]> data = new ArrayList<>();
        data.add(xTrain);
        data.add(xTest);
        data.add(yTrain);
        data.add(yTest); // Repacking the data.

        return data;
    }

    /**
     * This function sorts <code>x</code> from minimum value to maximum value and then change <y>so</y> so the y values for each x value will be saved.
     * @param x The x values.
     * @param y The y values.
     */
    private static void sortData(double[] x, double[] y) {
        double[] xCopy = x.clone();
        double[] yCopy = y.clone();

        Arrays.sort(x); // Sorting the array.

        int xLength = x.length;

        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < xLength; j++) { // x and y lengths are equal.
                if (xCopy[j] == x[i])
                    y[i] = yCopy[j];
            }
        }
    }

    /**
     * This function checks if the data is too large or too small to display on the graph.
     * @param arr The array that we want to check if it is too large or too small to display on the graph.
     * The array is sorted from minimal value to maximal value.
     * @return <code>true</code> if the values are too large or too small to display on the graph, <code>false</code> if not.
     */
    public static boolean isDataTooLargeForGraph(double[] arr) {
        return arr[0] < -100 || arr[arr.length - 1] > 100;
    }

    /**
     * This function computes the difference between the min value and max value in <code>arr</code>.
     * @param arr The array that we want to find the difference between its min value and its max value.
     * The array is sorted from minimal value to maximal value.
     * @return The difference between the min value and max value.
     */
    public static double differenceBetweenMinAndMaxValues(double[] arr) {
        return arr[arr.length - 1] - arr[0];
    }

}
