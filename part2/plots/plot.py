import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns  # <--- This defines 'sns'
import numpy as np


data_t = {"seq": "Sequential", "loc": "Locality", "rand": "Random Uniform Data"}


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
        f"Implementation Build Time as a Function of n ({data_t[data_type]} Queries)"
    )
    plt.ylabel("Time (ns) - Log Scale")
    plt.xlabel("n (Size in bits) - Log Scale")
    plt.grid(True, which="both", ls="-", alpha=0.3)

    plt.savefig(f"{fileName}.pdf")
    plt.close()


def compareQueryDataTypes(csv_path, op_type="rank"):
    df = load_and_clean_data(csv_path)
    data_types = ["rand", "seq"]
    metric = f"{op_type}_mean_ns"

    # Create a 1x3 grid
    fig, axes = plt.subplots(1, 2, figsize=(18, 6), sharey=True)

    for i, dtype in enumerate(data_types):
        subset = df[df["data_type"] == dtype]
        sns.lineplot(
            data=subset,
            x="bit_size",
            y=metric,
            hue="implementation",
            ax=axes[i],
            marker="o",
        )

        axes[i].set_title(
            f"{op_type.capitalize()} operations as a function of n - With {data_t[dtype]} queries"
        )
        axes[i].set_xscale("log", base=2)
        axes[i].set_yscale("log")
        axes[i].grid(True, which="both", ls="-", alpha=0.2)

        # Improve x-labels
        xticks = [2**13, 2**17, 2**21, 2**25, 2**29]
        axes[i].set_xticks(xticks)
        axes[i].set_xticklabels([f"$2^{{{int(np.log2(x))}}}$" for x in xticks])

    plt.suptitle(
        f"{op_type.capitalize()} Query Performance Across Data Distributions",
        fontsize=16,
    )
    plt.tight_layout(rect=[0, 0.03, 1, 0.95])
    plt.savefig(f"plot_{op_type}_all_distributions.pdf")


# compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_seq", "seq")
# compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_loc", "loc")
# compareBuilds("../benchmark_results/master_results.csv", "plot_buildTime_rand")
compareQueryDataTypes("../benchmark_results/master_results.csv")
compareQueryDataTypes("../benchmark_results/master_results.csv", "select")
