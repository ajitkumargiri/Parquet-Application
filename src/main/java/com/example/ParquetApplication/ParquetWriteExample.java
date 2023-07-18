package com.example.ParquetApplication;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.RowFactory.*;

public class ParquetWriteExample {
    public static void main(String[] args) {
        // Create SparkSession
        SparkSession sparkSession = SparkSession.builder()
                .appName("ParquetWriteExample")
                .master("local[*]")
                .getOrCreate();

        // Create sample data
        Row row1 = RowFactory.create("John", 25);
        Row row2 = RowFactory.create("Jane", 30);

        // Create the schema for the data
        StructType schema = DataTypes.createStructType(new StructField[] {
                DataTypes.createStructField("name", DataTypes.StringType, true),
                DataTypes.createStructField("age", DataTypes.IntegerType, true)
        });

        // Create a DataFrame from the data and schema
        List<Row> rows = Arrays.asList(row1, row2);
        Dataset<Row> df = sparkSession.createDataFrame(rows, schema);

        // Write DataFrame to Parquet file
        String outputPath = "src/main/resources/output.parquet";
        df.write().mode("overwrite").parquet(outputPath);

        // Print the absolute path of the output file
        File outputFile = new File(outputPath);
        String absolutePath = outputFile.getAbsolutePath();
        System.out.println("Output Parquet file path: " + absolutePath);

        // Stop SparkSession
        sparkSession.stop();
    }
}
