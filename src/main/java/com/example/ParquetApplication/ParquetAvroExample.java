package com.example.ParquetApplication;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

public class ParquetAvroExample {
    public static void main(String[] args) throws IOException {

        // Define the schema
        String schemaString = "message example {\n" +
                "  required binary name;\n" +
                "  required int32 age;\n" +
                "  required binary city;\n" +
                "}\n";

        // Define the Avro schema
        Schema avroSchema = new Schema.Parser().parse(schemaString);

        // Define the Parquet schema
        MessageType parquetSchema = MessageTypeParser.parseMessageType(avroSchema.toString());

        // Write a Parquet file
        writeParquetFile(parquetSchema, avroSchema);

        // Read the Parquet file
        readParquetFile(parquetSchema);
    }

    private static void writeParquetFile(MessageType parquetSchema, Schema avroSchema) throws IOException {
        // Create a ParquetWriter with AvroParquetWriter
        try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(new Path("output.parquet"))
                .withSchema(avroSchema)
                .withConf(new org.apache.hadoop.conf.Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                .build()) {

            // Create a GenericRecordBuilder for creating Avro records
            GenericRecordBuilder recordBuilder = new GenericRecordBuilder(avroSchema);

            // Write records to the Parquet file
            for (int i = 1; i <= 10; i++) {
                // Build the Avro record
                GenericRecord record = recordBuilder.set("name", "Customer " + i)
                        .set("age", 20 + i)
                        .build();

                // Write the record to the Parquet file
                writer.write(record);
            }
        }

        System.out.println("Parquet file written successfully.");
    }

    private static void readParquetFile(MessageType parquetSchema) throws IOException {
        // Create a ParquetReader with AvroParquetReader
        try (AvroParquetReader<GenericRecord> reader = new AvroParquetReader<>(new Path("output.parquet"))) {
            // Read records from the Parquet file
            GenericRecord record;
            while ((record = reader.read()) != null) {
                String name = record.get("name").toString();
                int age = (int) record.get("age");
                System.out.println("Name: " + name + ", Age: " + age);
            }
        }

        System.out.println("Parquet file read successfully.");
    }
}
