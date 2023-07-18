package com.example.ParquetApplication;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

public class ParquetReadWriteExample {
    public static void main(String[] args) {
        try {
            // Define the schema for the Parquet file
            String schemaString = "message example {\n" +
                    "  required binary name;\n" +
                    "  required int32 age;\n" +
                    "  required binary city;\n" +
                    "}\n";
            MessageType schema = MessageTypeParser.parseMessageType(schemaString);

            // Write the Parquet file
            writeParquetFile(schema);

            // Read the Parquet file
            readParquetFile(schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeParquetFile(MessageType schema) throws IOException {
        // Create a SimpleGroupFactory for creating Parquet groups
        SimpleGroupFactory groupFactory = new SimpleGroupFactory(schema);
        Configuration configuration = new Configuration();
        GroupWriteSupport.setSchema((MessageType)  schema, configuration);
        WriteSupport writeSupport = new GroupWriteSupport();

        // Create a ParquetWriter with the custom write support, output file path, and configuration
        String outputFile = "src/main/resources/example.parquet";

        try (ParquetWriter<Group> writer = new ParquetWriter<>(
                new Path(outputFile),
                writeSupport,
                CompressionCodecName.SNAPPY,
                ParquetWriter.DEFAULT_BLOCK_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                true,
                false,
                ParquetWriter.DEFAULT_WRITER_VERSION,
                configuration
        )) {
            // Create some example records and write them to the Parquet file
            Group record1 = groupFactory.newGroup()
                    .append("name", "John Doe")
                    .append("age", 30)
                    .append("city", "City1");
            writer.write(record1);

            Group record2 = groupFactory.newGroup()
                    .append("name", "Jane Smith")
                    .append("age", 35)
                    .append("city", "City2");
            writer.write(record2);

            System.out.println("Parquet file written successfully.");
        }
    }

    private static void readParquetFile(MessageType schema) throws IOException {
        // Create a ParquetReader with the input file path and configuration
        String inputFile = "/path/to/your/input.parquet";
        Configuration configuration = new Configuration();
        try (ParquetReader<Group> reader = ParquetReader.builder(new GroupReadSupport(), new Path(inputFile))
                .withConf(configuration)
                .build()) {

            // Read records from the Parquet file
            Group record;
            while ((record = reader.read()) != null) {
                String name = record.getString("name", 0);
                int age = record.getInteger("age", 0);
                String city = record.getString("city", 0);

                System.out.println("Name: " + name + ", Age: " + age + ", City: " + city);
            }

            System.out.println("Parquet file read successfully.");
        }
    }
}
