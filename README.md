# TigerGraph DB

## Setting up
### 1. Running the docker container of TigerGraph
- Ensure that docker and docker-compose is setup in your machine
- The yaml manifest file that describes the docker setup is in the same directory
- Run `docker-compose up -d` to have a running instance of TigerGraph DB in the background
    - The docker-compose file does not set up a shared drive between the host and the docker daemon process
    - All files will be lost once the docker container is stopped and removed

### 2. Starting the TigerGraph DB
- Once the container is setup, connect to the container `docker exec -it tigergraph /bin/sh`
- Run `su tigergraph` to be in the correct user environment
- Run `gadmin start all` to start TigerGraph DB

### 3. Accessing TigerGraph
- Once started, access `http://localhost:14240` for the TigerGraph Visual IDE

## Useful Command
- `gsql` : This will start the GSQL shell inside the docker container

## Package Structure

### Data Files

`graphdb/data/tigergraph`: Contains all the data files used for TigerGraph.

`graphdb/data/tigergraph/files/json`: These were the original `json` files which were processed with Mongo Compass. They are ready to use for Neo4j due to the presence of apoc. 
TigerGraph does not have an equivalent of apoc, but has several flatten functions. However, as I opted for the most straigtforward way, I chose to convert the files in this folder into `csv` format instead for compatibility with the GUI. 
I used [JSON to CSV](https://marketplace.visualstudio.com/items?itemName=khaeransori.json2csv) as well as a [csv visualization tool](https://marketplace.visualstudio.com/items?itemName=janisdd.vscode-edit-csv) to aid me in the process.

`graphdb/data/tigergraph/files/csv`: These contain all the `csv` files used to upload data into the database. 
Those formatted with `f_...` in the front are formatted and ready, and are used in the database. 
Those without had further processing done on them in order to clean the data.

Currently, I'm using the GUI to upload data and doing the mapping as the loading of data is not the most important part of this project. 
Hence I opted for the most straightforward way.

*WIP: TigerGraph loading job script to do it for you instead of using the GUI*

`graphdb/data/tigergraph/scripts`: These files are used in order to process the `csv` files, and are not general purpose scripts. 
If you wish to process the files again, `formatMovies` should be ran first, after which the other files can be run in any order.

*WIP: Bash script to run the files in order*

`graphdb/data/tigergraph/gsql`: These files are the GSQL files used to define the schema. 
To define the same schema in your local instance of TigerGraph, you can copy the last few lines of the files labelled `nodes.gsql` and `edges.gsql`.

*WIP: Bash script to automatically initialize schema + load all data through TigerGraph loading job*

### SpringBoot API

The middleware can be found under `graphdb/graphdb-internship/src/main/java/com/tigergraph/tg`.

`connection`: 
The file inside here reads from `application.properties` (should be placed in `src/main/resources`) in order to establish a connection to the database.

`controller`: 
The files here contain the API endpoints.

`model`: 
The files here contain the classes/ POJOs used by the API.

`repository`: 
The files here contain the logic behind the queries conducted.

`service`: 
The files here contain the code for the service layer and other helper functions needed for the respective models.

`util`: 
The files here are general helper functions abstracted into their own classes, and don't really belong on any service.

## Connecting to the database
TigerGraph uses its own [JDBC driver](https://github.com/tigergraph/ecosys/tree/master/tools/etl/tg-jdbc-driver) to allow it to interact with SpringBoot. 
It provides other methods such as using a [connection pool](https://github.com/tigergraph/ecosys/tree/master/tools/etl/tg-jdbc-driver#connection-pool) such as HikariCP to do so.


To initialize a connection, the application reads environment variables from the `application.properties` file. 
The file should be placed in `graphdb/graphdb-internship/src/main/resources` with the following properties, as illustrated by the example below:

```
spring.datasource.username=tigergraph
spring.datasource.password=tigergraph
spring.datasource.graphName=internship
spring.datasource.ipAddr=127.0.0.1
spring.datasource.port=14240
```

## Syntax

Some clarifications on the syntax:


```java
 do {
    java.sql.ResultSetMetaData metaData = rs.getMetaData();
    String nodeName = metaData.getCatalogName(1);
    while (rs.next()) {
        result.add(HandlerUtil.reconstructHandler(nodeName, rs));
    }
} while (!rs.isLast());
```

The double while loop is necessary, especially if returning a mix of nodes in the form of `List<Object>`, 
as the first while loop handles handles the different types. e.g `User`, `Movie`, ... .
The second while loop will iterate each entry of the type. e.g `user1`, `user2`, ... 

```java
// interpreted queries without parameters
stmt.setString(1, "");
stmt.setString(2, queryBody);
```

For interpreted queries without parameters, we would still have to reserve 1 slot for an empty string before being able to feed the query itself.
In other cases, depending on the number of `?` (represents a parameter), that would specify how many `stmt.setString()` are needed to feed in all paratmers.

*NOTE*: First `stmt.setString()` always starts from `1`, and the last `stmt.setString()` is always reserved for the query body.

## Response format

The response format of a restpp call is as follows:

```json
"results": [
    {
      "v_id": "id1",
      "v_type": "Person",
      "attributes": {...}
    }
]
```

Due to the lack of support with SpringBoot, there are no annotations available to conveniently map the response to an object. 
As such, one would have to recreate the object in order to return it for SpringBoot.

*NOTE*: The attributes are always returned in the same order but they **do not** follw the order as defined in the schema.

For example, you could define a Person in the following manner:

```sql
CREATE VERTEX User (
    PRIMARY_ID id STRING,
    name STRING,
    email STRING,
    password STRING
) WITH primary_id_as_attribute="true"
```

but the attributes may be returned in a following order:

`email, id, name, password`

Thus, one may need to spend more time figuring out the order returned. 
More examples can be seen from each service file and the respective reconstruct functions.

## Glossary

*upsert*: In TigerGraph, this refers to an `UPDATE` and `INSERT` request. Hence, there is no difference between them, as an `UPDATE` query basically does an `INSERT` action.

*interpreted query*: There are two types of queries in TigerGraph, [interpreted](https://docs.tigergraph.com/tigergraph-server/current/api/built-in-endpoints#_run_an_interpreted_query) and [installed](https://docs.tigergraph.com/gsql-ref/current/querying/query-operations#_install_query). 
Installed queries have better performance and can produce an endpoint to be called.

However, developing with Java only allows usage of interpreted usage. I may be wrong but I'm guessing the difference is that one hasn't been compiled into GSQL code. 
There are several [limitations](https://docs.tigergraph.com/gsql-ref/current/appendix/interpreted-gsql-limitations) in developing with interpreted queries.

*restpp*: This is the name of their rest service, short for REST++ (REST plus plus)