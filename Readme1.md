```code

flowchart TD
    A[Start] --> B[Initial Load File Creator]
    B -->|Read Data from Blob Storage| B1[File Reading & Extraction]
    B1 -->|Unzip & Process Files| B2[Transform to Parquet Format]
    B2 -->|Partition by Party ID| B3[Save to Azure Blob Storage]
    B3 --> C[Recalculation Engine]
    C -->|Load Transformed Data| C1[Apply Business Rules]
    C1 -->|Recalculate Derived Attributes| C2[Enrich Data (if applicable)]
    C2 -->|Flag Invalid Records| C3[Log Errors or Move to DLQ]
    C3 --> D[DB Saver]
    D -->|Save to Cosmos DB| D1[Write to NoSQL (Cosmos DB)]
    D -->|Save to SQL Server| D2[Write to Relational DB (SQL Server)]
    D1 & D2 --> E[End]
    
    subgraph Error Handling
        B3 -.-> F[Error Logger]
        C3 -.-> F
        D1 -.-> F
        D2 -.-> F
        F -->|Log Errors| G[Retry Mechanism]
        G -->|Persist Failed Records| H[Dead-letter Queue (DLQ)]
        H -->|Alert Teams| I[Azure Monitor Alerts]
    end

```
