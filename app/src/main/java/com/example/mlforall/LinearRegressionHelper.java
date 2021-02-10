package com.example.mlforall;

import java.util.ArrayList;

/**
 * This class helps setting the coefficients in {@link LinearRegression}
 * and splitting the data to training and testing data.
 */
public class LinearRegressionHelper {

    public static final double TRAIN_SPLIT = 0.8;

    /**
     * This function computes the sum of a double array.
     * @param arr The array which we want to sum.
     * @return The sum of arr.
     * @see #sum(double[])
     */
    public static double sum(double[] arr) {
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
     * @return {@link LinearEquation} of the slope and intercept.
     * The item in the first index will be the slope, and the item in the second index will be the intercept.
     * @see #mean(double[])
     * @see #variance(double[])
     * @see #covariance(double[], double[])
     */
    public static LinearEquation coefficients(double[] x, double[] y){
        double[] xCopy = x;
        double[] yCopy = y;

        double meanX = mean(xCopy);
        double meanY = mean(yCopy);

        double slope = covariance(xCopy, yCopy) / variance(xCopy);
        double intercept = meanY - (slope * meanX);

        return new LinearEquation(slope, intercept);
    }

    /**
     * This function creates {@link ArrayList} of random indexes for training data.
     * @param trainSize The amount of training items. <code>trainSize</code> will be <code>{@link LinearRegression#x}.length</code> * {@link #TRAIN_SPLIT}.
     * @return random indexes fot training data.
     */
    private static ArrayList<Integer> randomTrainingIndexes(int trainSize) {
        ArrayList<Integer> randomIndexes = new ArrayList<>();
        for (int i = 0; i < trainSize; i++) {
            int randIndex = (int) (Math.random() * trainSize);
            while (randomIndexes.contains(randIndex))
                randIndex = (int) (Math.random() * trainSize);
            randomIndexes.add(randIndex);
        }
        return randomIndexes;
    }

    /**
     * This function create {@link ArrayList} of the testing indexes. The function will iterate through
     * <code>size</code> <code>for (int i = 0; i < size; i++)</code> and will add every
     * <code>i</code> that is not in <code>trainingIndexes</code>.
     * @param testSize The amount of testing items.
     * @param trainingIndexes The training indexes.
     * @return The testing indexes.
     * @see #randomTrainingIndexes(int)
     */
    private static ArrayList<Integer> testingIndexes(int testSize, ArrayList<Integer> trainingIndexes) {
        ArrayList<Integer> testingIndexes = new ArrayList<Integer>();

        for (int i = 0; i < testSize; i++) {
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

        data.add(x);
        data.add(y);
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
        int trainSize = (int) (x.length * TRAIN_SPLIT);
        int testSize = x.length - trainSize;

        ArrayList<Integer> trainingIndexes = randomTrainingIndexes(trainSize);
        ArrayList<Integer> testingIndexes = testingIndexes(testSize, trainingIndexes);

        ArrayList<double[]> trainingData = getShuffledData(x, y, trainingIndexes);
        ArrayList<double[]> testingData = getShuffledData(x, y, testingIndexes);

        double[] xTrain = trainingData.get(0);
        double[] yTrain = testingData.get(1);

        double[] xTest = trainingData.get(0);
        double[] yTest = trainingData.get(1);

        ArrayList<double[]> data = new ArrayList<>();
        data.add(xTrain);
        data.add(xTest);
        data.add(yTrain);
        data.add(yTest);

        return data;
    }

}
