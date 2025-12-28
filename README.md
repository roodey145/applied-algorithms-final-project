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
