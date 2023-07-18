package com.example.ParquetApplication;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;

public class SparkJavaExample {
    public static void main(String[] args) {
        // Create SparkSession
        SparkSession sparkSession = SparkSession.builder()
                .appName("SparkJavaExample")
                .master("local[1]")
                .getOrCreate();

        // Create JavaSparkContext
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());

        // Read a text file into a JavaRDD
        JavaRDD<String> rdd = javaSparkContext.textFile("src/main/resources/customer1.parquet");

        // Process the RDD using Java and Spark API
        long count = rdd.count();

        System.out.println("Number of lines in the file: " + count);

        // Create a DataFrame programmatically
        StructType schema = DataTypes.createStructType(new StructField[] {
                DataTypes.createStructField("name", DataTypes.StringType, true),
                DataTypes.createStructField("age", DataTypes.IntegerType, true)
        });

        JavaRDD<Row> rowRDD = javaSparkContext.parallelize(Arrays.asList(
                RowFactory.create("John", 25),
                RowFactory.create("Jane", 30)
        ));

        Dataset<Row> df = sparkSession.createDataFrame(rowRDD, schema);
        df.show();

        // Stop SparkSession and JavaSparkContext
        sparkSession.stop();
        javaSparkContext.stop();
    }
}
