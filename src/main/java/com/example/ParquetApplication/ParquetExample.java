package com.example.ParquetApplication;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class ParquetExample {

    public static void main(String[] args) throws IOException {
        // Generate Parquet file
         generateParquetFile();

        // Read Parquet file
        readParquetFile();
    }

    private static void generateParquetFile() throws IOException {
        // Define the schema
        String schemaString = "message Customer {\n" +
                "  required int32 id;\n" +
                "  required binary name;\n" +
                "  required int32 age;\n" +
                "  required binary email;\n" +
                "}";
        MessageType schema = MessageTypeParser.parseMessageType(schemaString);

        // Create a ParquetWriter
        Path outputPath = new Path("src/main/resources/customer.parquet");
        Configuration configuration = new Configuration();
        Configuration hadoopConfig = new Configuration();
        hadoopConfig.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
        hadoopConfig.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        GroupWriteSupport.setSchema(schema, configuration);
        SimpleGroupFactory groupFactory = new SimpleGroupFactory(schema);

        try (ParquetWriter<Group> writer = new ParquetWriter<>(outputPath,
                new GroupWriteSupport(),
                CompressionCodecName.SNAPPY,
                ParquetWriter.DEFAULT_BLOCK_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                ParquetWriter.DEFAULT_WRITER_VERSION,
                hadoopConfig)) {

            // Write data to the Parquet file
            for (int i = 1; i <= 10; i++) {
                Group group = groupFactory.newGroup()
                        .append("id", i)
                        .append("name", "Customer " + i)
                        .append("age", 30 + i)
                        .append("email", "customer" + i + "@example.com");
                writer.write(group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readParquetFile() throws IOException {
        Path parquetFile = new Path("src/main/resources/customer.parquet");
        Configuration configuration = new Configuration();

        // Define the schema
        String schemaString = "message Customer {\n" +
                "  required int32 id;\n" +
                "  required binary name;\n" +
                "  required int32 age;\n" +
                "  required binary email;\n" +
                "}";
        MessageType schema = MessageTypeParser.parseMessageType(schemaString);

        GroupReadSupport readSupport = new GroupReadSupport();
       // readSupport.setSchema(schema);

        ParquetReader<Group> reader = ParquetReader.builder(readSupport, parquetFile)
                .withConf(configuration)
                .build();

        Group group;
        while ((group = reader.read()) != null) {
            // Process each record/group from the Parquet file
            int id = group.getInteger("id", 0);
            String name = group.getString("name", 0);
            int age = group.getInteger("age", 0);
            String email = group.getString("email", 0);

            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Email: " + email);
            System.out.println();
        }

        reader.close();
    }
}
