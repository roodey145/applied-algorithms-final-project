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
    "small": "input/uniform_data/L1_cache_uniform_4096.txt",
    "medium": "input/uniform_data/L2_cache_uniform_32_768.txt",
    "large": "input/uniform_data/L3_cache_uniform_393_216.txt",
    "memory": "input/uniform_data/RAM_uniform_1_000_000.txt",
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


print("--- Starting Benchmarks (Uniform Data) ---")
print("\n[1/4] Running SMALL (L1 Cache Fit)...")
print(f"Targeting: {FILES['small']} (n=4,096)")
# SMALL - L1 Cache
results_l1 = benchmark(INPUT_DATA["small"])
results_to_csv(results_l1, "benchmark_results/uniform_results/results_l1.csv")

# MEDIUM - L2 Cache
print("\n[2/4] Running MEDIUM (L2 Cache Fit)...")
print(f"Targeting: {FILES['medium']} (n=32,768)")
results_l2 = benchmark(INPUT_DATA["medium"])
results_to_csv(results_l2, "benchmark_results/uniform_results/results_l2.csv")

# LARGE - L3 Cache
print("\n[3/4] Running LARGE (L3 Cache Fit)...")
print(f"Targeting: {FILES['large']} (n=393,216)")
results_l3 = benchmark(INPUT_DATA["large"])
results_to_csv(results_l3, "benchmark_results/uniform_results/results_l3.csv")

# MEMORY - RAM
print("\n[4/4] Running MEMORY (Beyond Cache/RAM level)...")
print(f"Targeting: {FILES['memory']} (n=1,000,000)")
results_memory = benchmark(INPUT_DATA["memory"])
results_to_csv(results_memory, "benchmark_results/uniform_results/results_memory.csv")
