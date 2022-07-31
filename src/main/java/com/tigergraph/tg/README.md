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

## Useful Commands
- `gsql` : This will start the GSQL shell inside the docker container
- `ssh -p 14022 tigergraph@localhost`: Connect to TigerGraph's SSH servers
- In CMD: `docker ps`: Find out the id of the container
- `docker cp {fileName} {hostName/ id}:{path}/{fileName}`: To copy files from CMD into the SSH server

## Loading Data into TigerGraph

To learn how you can load data into this instance of TigerGraph, refer to this [page](../../../../../../../data/tigergraph/gsql/README.md).

## Package Structure

### Data Files

`graphdb/data/tigergraph`: Contains all the data files used for TigerGraph.

`graphdb/data/tigergraph/files/json`: These were the original `json` files which were processed with Mongo Compass. They are ready to use for Neo4j due to the presence of apoc. 
TigerGraph does not have an equivalent of apoc, but has several flatten functions. However, as I opted for the most straigtforward way, I chose to convert the files in this folder into `csv` format instead for compatibility with the GUI. 
I used [JSON to CSV](https://marketplace.visualstudio.com/items?itemName=khaeransori.json2csv) as well as a [csv visualization tool](https://marketplace.visualstudio.com/items?itemName=janisdd.vscode-edit-csv) to aid me in the process.

`graphdb/data/tigergraph/files/csv`: These contains all the `csv` files used to upload data into the database. 
Those formatted with `f_...` in the front are formatted and ready, and are used in the database. 
Those without had further processing done on them in order to clean the data.

Currently, I'm using the GUI to upload data and doing the mapping as the loading of data is not the most important part of this project. 
Hence I opted for the most straightforward way.

*WIP: TigerGraph loading job script to do it for you instead of using the GUI*

`graphdb/data/tigergraph/scripts`: These files are used in order to process the `csv` files, and are not general purpose scripts. 
If you wish to process the files again, `formatMovies` should be ran first, after which the other files can be run in any order.

These scripts are mainly used to strip quotations, include separators which are recognized by TigerGraph and the respective data structure.
(Some data structures have different separators, e.g Map uses `#` to separate entries)

They also get rid of un-needed data (such as fullplot when plot exists). This is to prevent additional cleaning as fullplot usually contains quotations and commas, interfering with the data loading process.

Furthermore, in addition to setting defaults on TigerGraph, we initialize certain fields to default values in the scripts as an extra layer of caution.

*NOTE*: If there exist an edge E between nodes A and B, to input data into E, it requires a unique pair identifier of entry A and B.
Hence, there cannot be more than one instance of the same edge type for 2 nodes A and B. 
This is different from Neo4j and thus the scripts also generate separate `csv` files just for these edges.

*WIP: Bash script to run the files in order*

`graphdb/data/tigergraph/gsql`: These files are the GSQL files used to define the schema. 
To define the same schema in your local instance of TigerGraph, you can copy the last few lines of the files labelled `nodes.gsql` and `edges.gsql`.

*WIP: Bash script to automatically initialize schema + load all data through TigerGraph loading job*

### SpringBoot API

The middleware can be found under `graphdb/graphdb-mflix/src/main/java/com/tigergraph/tg`.

`connection`: 
The files inside here reads from `application.properties` (should be placed in `src/main/resources`) in order to establish a connection to the database.

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
The file should be placed in `graphdb/graphdb-mflix/src/main/resources` with the following properties, as illustrated by the example below:

```
tigergraph.datasource.username=tigergraph
tigergraph.datasource.password=tigergraph
tigergraph.datasource.graphName=mflix
tigergraph.datasource.ipAddr=127.0.0.1
tigergraph.datasource.port=14240
tigergraph.datasource.driver=com.tigergraph.jdbc.Driver
```

You may then use `@ConfigurationProperties('tigergraph.datasource')` to reference the environment variables specified.

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

One can specify the attribute name in order to get the value from the restpp call, as per the following:

For example, if we defined the schema of a User as per the following:

```sql
CREATE VERTEX User (
    PRIMARY_ID id STRING,
    name STRING,
    email STRING,
    password STRING
) WITH primary_id_as_attribute="true"
```

Then, you may extract the values and recreate the object by calling its constructor:

```java
public static User reconstructUser(java.sql.ResultSet rs) throws SQLException {
    return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
}
```

## Endpoints

This repository uses OpenAPI and [Swagger 3](https://github.com/springdoc/springdoc-openapi) to write the documentation for the API.
After starting the server, go to this link to access the API:
http://localhost:8080/swagger-ui/index.html#/

*NOTE*: As the search parameter may include symbols disallowed in URLs, we assume the parameter passed in **is** encoded.
As such, you may have to encode it previously using [URLEncoder](https://docs.oracle.com/javase/10/docs/api/java/net/URLEncoder.html).

## Glossary

*upsert*: In TigerGraph, this refers to an `UPDATE` and `INSERT` request. Hence, there is no difference between them, as an `UPDATE` query basically does an `INSERT` action.

*interpreted query*: There are two types of queries in TigerGraph, [interpreted](https://docs.tigergraph.com/tigergraph-server/current/api/built-in-endpoints#_run_an_interpreted_query) and [installed](https://docs.tigergraph.com/gsql-ref/current/querying/query-operations#_install_query). 
Installed queries have better performance and can produce an endpoint to be called.

However, developing with Java only allows usage of interpreted usage. I may be wrong but I'm guessing the difference is that one hasn't been compiled into GSQL code. 
There are several [limitations](https://docs.tigergraph.com/gsql-ref/current/appendix/interpreted-gsql-limitations) in developing with interpreted queries.

*restpp*: This is the name of their rest service, short for REST++ (REST plus plus)
