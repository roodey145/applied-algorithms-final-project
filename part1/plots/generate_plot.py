import matplotlib.pyplot as plt
import csv
from collections import defaultdict
from typing import Dict
import numpy as np


def plot_execution_time_csv(csv_file: str, filename: str):
    # Read CSV and organize data
    data: Dict[str, np.ndarray] = defaultdict(list)
    with open(csv_file, newline="") as f:
        reader = csv.DictReader(f)
        for row in reader:
            impl = row["Implementation"]
            alpha = float(row["Alpha"])
            mean = float(row["Mean (ns)"]) / 1e6  # convert ns -> ms
            std = float(row["Std Dev (ns)"]) / 1e6  # convert ns -> ms
            data[impl].append([alpha, mean, std])

    # Convert lists to sorted numpy arrays
    for impl in data:
        arr = np.array(data[impl])
        arr = arr[arr[:, 0].argsort()]  # sort by alpha
        data[impl] = arr

    # Plot
    fig, ax = plt.subplots(figsize=(8, 5))
    colors = plt.get_cmap("tab10")

    algorithms = list(data.keys())
    for i, algorithm in enumerate(algorithms):
        arr = data[algorithm]
        alphas, means, stds = arr[:, 0], arr[:, 1], arr[:, 2]
        ax.errorbar(alphas, means, yerr=stds, marker="o", capsize=3.0, color=colors(i))

    ax.set_xlabel("Alpha")
    ax.set_ylabel("Mean Execution Time (ms)")  # updated to ms
    ax.set_title("Execution Time vs Alpha")
    ax.legend(algorithms)
    ax.grid(True)
    fig.tight_layout()
    fig.savefig(filename, dpi=300)


plot_execution_time_csv(
    "../benchmark_results/results_small.csv", "plot_results_small.pdf"
)
