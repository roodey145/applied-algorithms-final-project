import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns  # <--- This defines 'sns'
import numpy as np


data_types = {"seq": "Sequential", "loc": "Locality", "rand": "Random Uniform Data"}


def load_and_clean_data(csv_path):
    """Helper to load data and handle the 'FAILED' strings consistently."""
    df = pd.read_csv(csv_path)
    # Convert metric columns to numeric, forcing 'FAILED' to NaN
    metrics = [
        "build_mean_ns",
        "build_std_ns",
        "rank_mean_ns",
        "select_mean_ns",
        "rank_median_ns",
        "select_median_ns",
    ]
    for col in metrics:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors="coerce")

    # Ensure bit_size is numeric for proper scaling
    df["bit_size"] = pd.to_numeric(df["bit_size"])
    return df


def compareBuilds(
    csv_path,
    fileName,
    data_type="rand",
):
    df = load_and_clean_data(csv_path)
    df = df[df["data_type"] == data_type]

    plt.figure(figsize=(10, 6))
    # Replace 0 with 1 for Naive so it appears on Log scale
    df["build_mean_ns"] = df["build_mean_ns"].replace(0, 1)

    sns.lineplot(
        data=df, x="bit_size", y="build_mean_ns", hue="implementation", marker="o"
    )
    plt.errorbar(
        df["bit_size"],
        df["build_mean_ns"],
        yerr=df["build_std_ns"],
        fmt="none",
        ecolor="gray",
        alpha=0.5,
    )

    plt.xscale("log", base=2)
    plt.yscale("log")
    plt.title(
        f"Implementation Build Time as a Function of n ({data_types[data_type]} Queries)"
    )
    plt.ylabel("Time (ns) - Log Scale")
    plt.xlabel("n (Size in bits) - Log Scale")
    plt.grid(True, which="both", ls="-", alpha=0.3)

    plt.savefig(f"{fileName}.pdf")
    plt.close()


compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_seq", "seq")
compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_loc", "loc")
compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_rand")
