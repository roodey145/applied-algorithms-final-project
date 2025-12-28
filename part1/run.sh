cd skewed_binary_trees
echo "........Running Gradle build, automatically runs the tests........"
gradle build
cd ../input
mkdir uniform_data
echo "........Generating the input files needed for the experiments........"
python3 createInput.py
cd ..
echo "........Running the experiment, this will take a while........"
python3 experiment.py
cd plots/
echo "Generating the plots from the results of the experiments"
python3 generate_plot.py
open *.pdf
cd ..
