cd rank_select && gradle build
echo "........Running Gradle build, automatically runs the tests........"
cd ../input
mkdir locality_data random_data sequential_data
echo "........Generating the input files needed for the experiments........"
python3 create_input.py
cd ..
echo "........Running the experiment, this will take a while........"
python3 experiment.py
cd plots
echo "Generating the plots from the results of the experiments"
python3 plot.py
open *.pdf
cd ..