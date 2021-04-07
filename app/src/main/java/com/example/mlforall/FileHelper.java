package com.example.mlforall;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHelper {

    public static final int MIN_ROW_NUMBER_IN_DATASET = 10; // The min number of rows in the dataset.
    public static final int MIN_COLUMNS_NUMBER_IN_DATASET = 2; // The min number of columns in the dataset.

    public static final String SEPARATOR = ",";

    private final BufferedReader reader;
    private ArrayList<String> columns = new ArrayList<>();

    public FileHelper(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * This function builds the dataset
     *
     * @return {@link HashMap} The keys will be the column names and the values will be the items in the column.
     */
    public HashMap<String, double[]> getDataset() {
        HashMap<String, double[]> dataset;
        try {
            ArrayList<ArrayList<String>> data = loadPreprocessedData();
            data = dropNonNumericColumns(data);
            ArrayList<ArrayList<Double>> dataInDoubleValues = convertToDouble(data);
            dataset = loadDataset(dataInDoubleValues);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dataset;
    }

    /**
     * This function sets the new columns for the dataset. The function will remove from {@link #columns} all the values in the indexes that are in <code>indexesForNonNumericValueColumns</code>.
     * @param indexesForNonNumericValueColumns The indexes of the columns that have numeric values and will be dropped.
     */
    private void setColumns(ArrayList<Integer> indexesForNonNumericValueColumns) {
        ArrayList<String> newColumns = new ArrayList<>();

        int columnsSize = this.columns.size();

        for (int i = 0; i < columnsSize; i++) { // This loop adds to newColumns all the column names that has no numeric values.
            if (!indexesForNonNumericValueColumns.contains(i))
                newColumns.add(this.columns.get(i));
        }
        this.columns = newColumns;
    }


    /**
     * This function checks if there is null value in {@link String[]}
     *
     * @param line The array that we want to check if it contains a <code>null</code> value.
     * @return <code>true</code> if there is null value or empty value in <code>line</code>, false if not.
     */
    private boolean hasNullOrEmptyValues(String[] line) {
        if (line == null)
            return true;
        for (String value : line) {
            if (value == null || value.equals("")) // If there is null or empty value in line.
                return true;
        }
        return false;
    }

    /**
     * This function loads the dataset without columns(There will be default numeric columns).
     * @return {@link ArrayList} of all the values in the dataset.
     */
    private ArrayList<ArrayList<String>> loadPreprocessedData() {
        ArrayList<ArrayList<String>> data = new ArrayList<>(); // This ArrayList will store the data for later process.
        try {
            String line = this.reader.readLine();
            String[] values = line.split(SEPARATOR);

            int valuesLength = values.length;

            if (line.equals("")) // If loading the columns failed.
                return null;
            else {
                for (int i = 0; i < valuesLength; i++) { // Loading the dataset columns
                    if (values[i] == null || values[i].equals("")) // If the column name is empty or null.
                        this.columns.add("" + i);
                    else
                        this.columns.add(values[i]);
                }
            }

            for (int i = 0; i < valuesLength; i++) // Initialize the ArrayList.
                data.add(new ArrayList<>());

            while ((line = reader.readLine()) != null) {
                values = line.split(SEPARATOR);

                if ((values.length != valuesLength) || hasNullOrEmptyValues(values)) // If the length of values is not the same as teh amount of columns in the dataset or there are empty values in the dataset.
                    return null;

                for (int i = 0; i < valuesLength; i++) // Adding the value to the dataset.
                    data.get(i).add(values[i]);
            }
            if (data.get(0).size() < MIN_ROW_NUMBER_IN_DATASET || data.size() < MIN_COLUMNS_NUMBER_IN_DATASET) // If the dataset has less than 10 rows and less than 2 columns.
                return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            this.reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    /**
     * This function drops all the {@link ArrayList<String>} items in <code>data</code> if they contain values that are not numbers.
     *
     * @param data The {@link ArrayList} that we want to remove all non numeric data from.
     * @return {@link ArrayList} of {@link ArrayList} that contain only with numeric values in the dataset.
     */
    private ArrayList<ArrayList<String>> dropNonNumericColumns(ArrayList<ArrayList<String>> data) {

        ArrayList<Integer> indexesForNonNumericValueColumns = new ArrayList<>();

        if (data == null) // If data is empty.
            return null;

        int data_size = data.size();

        for (int i = 0; i < data_size; i++) { // Loading the indexes of columns with non numeric values.
            int rows = data.get(i).size();
            boolean hasNonNumericValues = false;
            for (int j = 0; j < rows; j++) {
                try {
                    Double.parseDouble(data.get(i).get(j));
                }
                catch (Exception e) {
                    hasNonNumericValues = true;
                }
            }
            if (hasNonNumericValues)
                indexesForNonNumericValueColumns.add(i);
        }

        ArrayList<ArrayList<String>> dataWithoutNonNumericColumn = new ArrayList<>();

        this.setColumns(indexesForNonNumericValueColumns);

        for (int i = 0; i < data_size; i++) {
            if (!indexesForNonNumericValueColumns.contains(i))
                dataWithoutNonNumericColumn.add(data.get(i));
        }
        if (data.get(0).size() < MIN_ROW_NUMBER_IN_DATASET || data.size() < MIN_COLUMNS_NUMBER_IN_DATASET) // If the dataset has less than 10 rows and less than 2 columns.
            return null;
        return dataWithoutNonNumericColumn;
    }

    /**
     * This function converts all the columns to numeric columns.
     * @param data The data that we want to convert to numeric columns.
     * @return {@link ArrayList} the data in numeric value.
     */
    private ArrayList<ArrayList<Double>> convertToDouble(ArrayList<ArrayList<String>> data) {
        if (data == null) // If data is empty.
            return null;

        final int DATA_SIZE = data.size();
        final int COLUMN_SIZE = data.get(0).size();

        ArrayList<ArrayList<Double>> dataInDoubleValues = new ArrayList<>();

        for (int i = 0; i < DATA_SIZE; i++) // Initialize the ArrayList.
            dataInDoubleValues.add(new ArrayList<>());


        for (int i = 0; i < DATA_SIZE; i++) {
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String currentValue = data.get(i).get(j);
                dataInDoubleValues.get(i).add(Double.parseDouble(currentValue)); // Adding the numeric values to dataInDoubleValues.
            }
        }
        return dataInDoubleValues;
    }

    /**
     * This function loads the dataset.
     * @param data The dataset values.
     * @return {@link HashMap} the keys will be the items from {@link #columns} and the values will be <code>double[]</code> from <code>data</code>.
     */
    private HashMap<String, double[]> loadDataset(ArrayList<ArrayList<Double>> data) {
        if (data == null) // If data is empty.
            return null;
        HashMap<String, double[]> dataset = new HashMap<>();

        int num_columns = this.columns.size();
        int num_rows = data.get(0).size();


        for (int i = 0; i < num_columns; i++) {
            double[] arr = new double[num_rows]; // Loading the data to double array.
            for (int j = 0; j < num_rows; j++)
                arr[j] = data.get(i).get(j);
            dataset.put(this.columns.get(i), arr);
        }
        return dataset; // loading the dataset.
    }
}
