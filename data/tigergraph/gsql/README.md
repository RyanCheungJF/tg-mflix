# GSQL Scripts

The GSQL scripts here are to help you create the schema and load data all at once by just copying and pasting a few lines of code. 
This will make your local instance of TigerGraph match the dataset used in `graphdb-mflix`.

### Pre-Requisites

- Connected to the SSH server of TigerGraph after Docker is set up. \
Use `ssh -p 14022 tigergraph@localhost`

- Started TigerGraph services. \
Use `gadmin start all`

- Created a folder on the SSH server named `data`, and copied the csv files prefixed with `f_...` over to the `data` folder. \
i.e. The pwd `/home/tigergraph/data` should have all of the csv files which starts with `f_...`, which will be used in the loading phase.

- Opened up the GSQL shell. \
Use `gsql`

### Nodes

In `nodes.gsql`, copy the last few lines after the gsql label and copy that into the shell.

### Edges

In `edges.gsql`, copy the last few lines after the gsql label and copy that into the shell.

### Creating and Using a Graph

Using the GUI, create a new graph and import all nodes and edges over. 
For our scripts, we call our graph `intenership`.

### Loading Data

In `loading.gsql`, copy and paste from `USE GRAPH` to `END` (lines 1-37) and paste in into the shell. \
Do remember to change the name of the graph according to what you named it. \
This process will take a while. Once done, run the very last line in the shell `RUN LOADING JOB lj`.

