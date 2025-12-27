import subprocess
import csv
import numpy as np

TIMEOUT = 30


# run the given jar package,
# provide the given arg as the command-line
# argument,
# feed the given input file to the stdin of the
# process,
# and return the stdout from the process as string
def run_java(jar: str, arg: str, alpha: float, input_file: str) -> str:
    p = subprocess.Popen(
        ["java", "-jar", jar, arg, alpha], stdin=subprocess.PIPE, stdout=subprocess.PIPE
    )
    (output, _) = p.communicate(input_file.encode("utf-8"), timeout=TIMEOUT)
    return output.decode("utf-8")


IMPLEMENTATIONS = [
    "SortedArrayBinarySearch",
    "SearchTree",
    "OtherArrayBinarySearch",
    "KArrayBinarySearch",
]

ALPHAS = ["0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9"]

FILES = {
    "test": "input/uniform_data/small.txt",
    "small": "input/uniform_data/data_50k.txt",
    "medium": "input/uniform_data/data_500k.txt",
    "large": "input/uniform_data/data_2m.txt",
    "l1_locality": "input/locality_files/l1_high_locality.txt",
    "l3_locality": "input/locality_files/l3_high_locality.txt",
}

print("Loading input files...")

INPUT_DATA = {}

for name, filepath in FILES.items():
    with open(filepath, "r") as f:
        INPUT_DATA[name] = f.read()
    print(f" Loaded {name}: {filepath}")

print("Files loaded")


def benchmark(filename: str, runs=15):
    results = {}
    for impl in IMPLEMENTATIONS:
        results[impl] = {}
        print(f"\n=== TESTING {impl} ===")

        for alpha in ALPHAS:
            results[impl][alpha] = []

            print(
                f"  Alpha {alpha}: running program and adding time... ",
                end="",
                flush=True,
            )

            for run in range(runs):
                result = run_java(
                    "skewed_binary_trees/app/build/libs/app.jar", impl, alpha, filename
                ).strip()
                results[impl][alpha].append(int(result))
    return results


def results_to_csv(results, filename):
    """Save benchmark results to CSV with statistics"""

    with open(filename, "w", newline="") as f:
        writer = csv.writer(f)

        # Header
        writer.writerow(
            [
                "Implementation",
                "Alpha",
                "Mean (ns)",
                "Median (ns)",
                "Std Dev (ns)",
                "Min (ns)",
                "Max (ns)",
            ]
        )

        # Data rows
        for impl in results:
            for alpha in results[impl]:
                times = results[impl][alpha]
                writer.writerow(
                    [
                        impl,
                        alpha,
                        np.mean(times),
                        np.median(times),
                        np.std(times),
                        min(times),
                        max(times),
                    ]
                )

    print(f"✓ Results saved to {filename}")


# Run this test to ensure everything is set up nicely
# results_test = benchmark(INPUT_DATA["test"])
# results_to_csv(results_test, "benchmark_results/results_test.csv", "10")

# results_small = benchmark(INPUT_DATA["small"])
# results_to_csv(results_small, "benchmark_results/results_small.csv")
results_small = benchmark(INPUT_DATA["l3_locality"])
results_to_csv(results_small, "benchmark_results/results_l3_locality.csv")

# results_medium = benchmark(INPUT_DATA["medium"])
# results_to_csv(results_medium, "benchmark_results/results_medium.csv")


# results_large = benchmark(INPUT_DATA["large"])
# results_to_csv(results_large, "benchmark_results/results_large.csv")
