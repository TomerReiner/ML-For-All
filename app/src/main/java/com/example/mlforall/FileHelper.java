package com.example.mlforall;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.opencsv.CSVReader;

public class FileHelper {

    public static final int MIN_ROW_NUMBER_IN_DATASET = 10; // The min number of rows in the dataset.
    public static final int MIN_COLUMNS_NUMBER_IN_DATASET = 2; // The min number of columns in the dataset.

    private String path; // the path to the file.
    private ArrayList<String> columns = new ArrayList<>();

    public FileHelper(String path) {
        this.path = path;
    }

    /**
     * This function builds the dataset
     *
     * @return {@link HashMap} The keys will be the column names and the values will be the items in the column.
     */
    public HashMap<String, double[]> getDataset() {
        HashMap<String, double[]> dataset = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(this.path));
            ArrayList<ArrayList<String>> data = loadDataToArrayList(reader);
            data = dropNonNumericColumns(data);
            ArrayList<ArrayList<Double>> dataInDoubleValues = convertToDouble(data);
            dataset = loadDataset(dataInDoubleValues);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataset;
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
     *
     * @param reader The csv file.
     * @return {@link ArrayList} of all the values in the dataset.
     */
    private ArrayList<ArrayList<String>> loadDataToArrayList(CSVReader reader) {
        ArrayList<ArrayList<String>> data = new ArrayList<>(); // This ArrayList will store the data for later process.
        try {
            String[] line = reader.readNext();

            if (line == null) // If loading the columns failed.
                return null;
            else {
                for (int i = 0; i < line.length; i++) { // Loading the dataset columns
                    if (line[i] == null || line[i].equals(""))
                        this.columns.add("" + i);
                    else
                        this.columns.add(line[i]);
                }
            }

            for (int i = 0; i < line.length; i++) // Initialize the ArrayList.
                data.add(new ArrayList<>());

            while ((line = reader.readNext()) != null) {
                if (hasNullOrEmptyValues(line)) // If there is null or empty value in line we skip that line to prevent error or bad model accuracy.
                    continue;

                for (int i = 0; i < line.length; i++) // Adding the value to the dataset.
                    data.get(i).add(line[i]);
            }

            if (data.get(0).size() < MIN_ROW_NUMBER_IN_DATASET || data.size() < MIN_COLUMNS_NUMBER_IN_DATASET) // If the dataset has less than 10 rows and less than 2 columns.
                return null;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * This function drops all the {@link ArrayList<String>} items in <code>data</code> if they contain values that are not numbers.
     *
     * @param data The {@link ArrayList} that we want to remove all non numeric data from.
     * @return {@link ArrayList} only with numeric values in the dataset.
     */
    private ArrayList<ArrayList<String>> dropNonNumericColumns(ArrayList<ArrayList<String>> data) {

        if (data == null) // If data is empty.
            return null;

        final int DATA_SIZE = data.size();
        final int COLUMN_SIZE = data.get(0).size();

        for (int i = 0; i < DATA_SIZE; i++) {
            int j = 0;
            boolean hasNonNumericValues = false;
            while (j < COLUMN_SIZE && !hasNonNumericValues) {
                char[] values = data.get(i).get(j).toCharArray();

                for (char c : values) {
                    if (c < '0' || c > '9') // If there is non numeric value in data.
                        hasNonNumericValues = true;
                }

                if (hasNonNumericValues) {
                    data.remove(i); // Dropping the non numeric values in data.
                    this.columns.remove(i);
                }
                j++;
            }
        }
        if (data.get(0).size() < MIN_ROW_NUMBER_IN_DATASET || data.size() < MIN_COLUMNS_NUMBER_IN_DATASET) // If the dataset has less than 10 rows and less than 2 columns.
            return null;
        return data;
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

        final int NUM_COLUMNS = this.columns.size();
        final int NUM_ROWS = data.get(0).size();


        for (int i = 0; i < NUM_COLUMNS; i++) {
            double[] arr = new double[NUM_ROWS]; // Loading the data to double array.
            for (int j = 0; j < NUM_ROWS; j++) {
                arr[i] = data.get(i).get(j);
            }
            dataset.put(this.columns.get(i), arr);
        }
        return dataset; // loading the dataset.
    }
}
