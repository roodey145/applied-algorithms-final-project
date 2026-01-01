# applied-algorithms-final-project

## Running the experiments for part 1

To run the experiments for part 1, cd into part1 and execute the run.sh file

```bash
cd part1
bash run.sh
```

or

```bash
cd part1
zsh run.sh
```

This script will:

-   Build the gradle project and execute the tests
-   Create the input files needed to run the experiments in the folder input/uniform_data
-   Runs experiment and saves results to benchmark_results/uniform_results
-   Generates the plots from the csv files, the plots can be seen in the plots folder
-   Thats it!

### Alternatively

-   Build Project: cd into skewed_binary_trees && gradle build
-   Generate Data : cd input && mkdir uniform_data && python3 createInput.py
-   Run Experiment: cd part1 && python3 experiment.py
-   Generate Plots: cd into plots and run the python file generate_plot.py

### Running the java program

After building the gradle project, you can also run the java program with the jar file. The program requires two arguments: \<ImplementationName\> and \<AlphaValue\>. Data is passed via standard input (stdin). Input Format: The first integer is the array size, the second is the query size, followed by the data elements.\
3 // Array size \
3 // Query size \
1 1 1 // Array elements \
1 1 1 // Query elements

Here's how it would be used, assuming you're in the part1 directory:

```bash
java -jar skewed_binary_trees/app/build/libs/app.jar SearchTree 0.5 < input.txt
```

## Running the experiments for part 2

To run the experiments for part 2, cd into part2 and execute the run.sh file

```bash
cd part2
bash run.sh
```

or

```bash
cd part2
zsh run.sh
```

This script will:

-   Build the gradle project and execute the tests
-   Create the input files needed to run the experiments in the folder input/locality_data input/random_data input/sequential_data
-   Runs experiment and saves results to benchmark_results/uniform_results
-   Combines the csv's into one file benchmark_results/master_results.csv
-   Generates the plots from the csv file, the plots can be seen in the plots folder
-   Thats it!

### Alternatively

-   Build Project: cd rank_select && gradle build
-   Generate Data : cd input && mkdir locality_data random_data sequential_data && python3 createInput.py
-   Run Experiment: cd part1 && python3 experiment.py
-   Combine csv files: cd benchmark_results && python3 combine_csv.py
-   Generate Plots: cd plots && python3 plot.py

### Running the java program

After building the gradle project, you can also run the java program with the jar file. The program requires one argument if the implementation is RankSelectNaive or RankSelectLookup. And two arguments if the implementation is RankSelectSpaceEfficient, this is because it takes an additional argument k which is the second argument given.
Data is passed via standard input (stdin). Input Format: The first integer is the bit array size, the second is the query size, followed by the total number of ones in the bit string (for testing purposes) all in one line.\
The next 3 lines then represent a bit string of 1's and 0's, rank queries, select queries
Example: \
10 3 5 \
1010101010 // bitString \
1 2 3 // rank queries \
1 2 3 // select queries

Here's how it would be used, assuming you're in the part2 directory:
For RankSelectNaive & RankSelectLookup

```bash
java -jar rank_select/app/build/libs/app.jar RankSelectNaive < input.txt
```

or

```bash
java -jar rank_select/app/build/libs/app.jar RankSelectLookup < input.txt
```

For rankSelectSpaceEfficient specify a k value:

```bash
java -jar rank_select/app/build/libs/app.jar RankSelectSpaceEfficient 32 < input.txt
```
