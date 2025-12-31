import subprocess
import csv
import numpy as np

TIMEOUT = 300


# run the given jar package,
# provide the given arg as the command-line
# argument,
# feed the given input file to the stdin of the
# process,
# and return the stdout from the process as string
def run_java(jar: str, arg: str, input_file: str, k: str = None) -> str:
    cmd = ["java", "-Xmx5G", "-jar", jar, arg]
    if k is not None:
        cmd.append(k)

    try:
        p = subprocess.Popen(
            cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE
        )
        (output, err) = p.communicate(input_file.encode("utf-8"), timeout=TIMEOUT)

        if p.returncode != 0:
            raise RuntimeError(
                f"Java process failed with code {p.returncode}: {err.decode('utf-8')}"
            )

        return output.decode("utf-8").strip()

    except subprocess.TimeoutExpired:
        p.kill()
        raise TimeoutError(f"Java process timed out after {TIMEOUT}s")
    except Exception as e:
        raise RuntimeError(f"Java execution failed: {e}")


RANK_SELECT_SPECIAL = "RankSelectSpaceEfficient"
SPACE_EFF_K_VALUES = ["32", "64", "256"]
FILE_SIZES = [
    "4096",
    "32768",
    "131072",
    "1048576",
    "8388608",
    "67108864",
    "268435456",  # 2^28
    "536870912",  # 2^29
    1073741824,
]

IMPLEMENTATIONS = [
    "RankSelectNaive",
    "RankSelectLookup",
]

FILES = {
    # n = 2^12 (4096)
    "n_4096_loc": "input/locality_data/n_4096loc.txt",
    "n_4096_rand": "input/random_data/n_4096rand.txt",
    "n_4096_seq": "input/sequential_data/n_4096seq.txt",
    # n = 2^15 (32768)
    "n_32768_loc": "input/locality_data/n_32768loc.txt",
    "n_32768_rand": "input/random_data/n_32768rand.txt",
    "n_32768_seq": "input/sequential_data/n_32768seq.txt",
    # n = 2^17 (131072)
    "n_131072_loc": "input/locality_data/n_131072loc.txt",
    "n_131072_rand": "input/random_data/n_131072rand.txt",
    "n_131072_seq": "input/sequential_data/n_131072seq.txt",
    # n = 2^20 (1048576)
    "n_1048576_loc": "input/locality_data/n_1048576loc.txt",
    "n_1048576_rand": "input/random_data/n_1048576rand.txt",
    "n_1048576_seq": "input/sequential_data/n_1048576seq.txt",
    # n = 2^23 (8388608)
    "n_8388608_loc": "input/locality_data/n_8388608loc.txt",
    "n_8388608_rand": "input/random_data/n_8388608rand.txt",
    "n_8388608_seq": "input/sequential_data/n_8388608seq.txt",
    # n = 2^26 (67108864)
    "n_67108864_loc": "input/locality_data/n_67108864loc.txt",
    "n_67108864_rand": "input/random_data/n_67108864rand.txt",
    "n_67108864_seq": "input/sequential_data/n_67108864seq.txt",
    # n = 2^28 (268435456)
    "n_268435456_loc": "input/locality_data/n_268435456loc.txt",
    "n_268435456_rand": "input/random_data/n_268435456rand.txt",
    "n_268435456_seq": "input/sequential_data/n_268435456seq.txt",
    # n = 2^29 (536870912)
    "n_536870912_loc": "input/locality_data/n_536870912loc.txt",
    "n_536870912_rand": "input/random_data/n_536870912rand.txt",
    "n_536870912_seq": "input/sequential_data/n_536870912seq.txt",
    # n = 2^30 (1073741824)
    "n_1073741824_loc": "input/locality_data/n_1073741824loc.txt",
    "n_1073741824_rand": "input/random_data/n_1073741824rand.txt",
    "n_1073741824_seq": "input/sequential_data/n_1073741824seq.txt",
}


# INPUT_DATA = {}

# for name, path in FILES.items():
#     with open(path, "r") as f:
#         INPUT_DATA[name] = f.read()
#     print(f"Loaded {name}: {path}")


def benchmark(filename: str, runs=15):
    results = {}

    for impl in IMPLEMENTATIONS:
        results[impl] = {
            "rank_times": [],
            "select_times": [],
            "build_times": [],
            "k": "N/A",
            "failed": False,
            "error": None,
        }
        print(f"\n=== TESTING {impl} ===")

        for run in range(runs):
            try:
                print(f"  Trial {run + 1}/{runs}...", end="", flush=True)
                output = run_java("rank_select/app/build/libs/app.jar", impl, filename)
                build_time, rank_time, select_time = output.split(",")

                results[impl]["build_times"].append(int(build_time))
                results[impl]["rank_times"].append(int(rank_time))
                results[impl]["select_times"].append(int(select_time))
                print(" ✓")

            except TimeoutError as e:
                print(f" ✗ TIMEOUT")
                results[impl]["failed"] = True
                results[impl]["error"] = str(e)
                break  # Stop this implementation, move to next

            except Exception as e:
                print(f" ✗ ERROR: {e}")
                results[impl]["failed"] = True
                results[impl]["error"] = str(e)
                break

    # Same pattern for RANK_SELECT_SPECIAL
    for k in SPACE_EFF_K_VALUES:
        print(f"\n=== Testing {RANK_SELECT_SPECIAL} (k = {k}) ===")
        dict_key = f"{RANK_SELECT_SPECIAL}_k{k}"
        results[dict_key] = {
            "rank_times": [],
            "select_times": [],
            "build_times": [],
            "k": k,
            "failed": False,
            "error": None,
        }

        for run in range(runs):
            try:
                print(f"  Trial {run + 1}/{runs}...", end="", flush=True)
                output = run_java(
                    "rank_select/app/build/libs/app.jar",
                    RANK_SELECT_SPECIAL,
                    filename,
                    k,
                )
                build_time, rank_time, select_time = output.split(",")

                results[dict_key]["build_times"].append(int(build_time))
                results[dict_key]["rank_times"].append(int(rank_time))
                results[dict_key]["select_times"].append(int(select_time))
                print(" ✓")

            except TimeoutError as e:
                print(f" ✗ TIMEOUT")
                results[dict_key]["failed"] = True
                results[dict_key]["error"] = str(e)
                break

            except Exception as e:
                print(f" ✗ ERROR: {e}")
                results[dict_key]["failed"] = True
                results[dict_key]["error"] = str(e)
                break

    return results


def run_all_experiments():
    all_results = {}
    for size in FILE_SIZES:
        print(f"\n\n>>>>>>>> PROCESSING SIZE: n = {size} <<<<<<<<")
        all_results[size] = {}
        for data_type in ["loc", "rand", "seq"]:
            file_key = f"n_{size}_{data_type}"
            if file_key in FILES:
                filepath = FILES[file_key]
                print(f"\n--- Running: {file_key} ---")
                with open(filepath, "r") as file:
                    file_data = file.read()
                file_results = benchmark(file_data, runs=15)
                all_results[size][data_type] = file_results

                del file_data
    return all_results


def save_results_to_csv(master_results):
    for size, data_types in master_results.items():
        filename = f"benchmark_results/results_n{size}.csv"

        rows = []
        for data_type, implementations in data_types.items():
            for impl_name, metrics in implementations.items():
                if metrics.get("failed", False):
                    rows.append(
                        {
                            "data_type": data_type,
                            "bit_size": size,
                            "implementation": impl_name,
                            "k": metrics["k"],
                            "rank_mean_ns": "FAILED",  # ← Add _ns suffix for clarity
                            "rank_median_ns": "FAILED",
                            "rank_std_ns": "FAILED",
                            "select_mean_ns": "FAILED",
                            "select_median_ns": "FAILED",
                            "select_std_ns": "FAILED",
                            "build_mean_ns": "FAILED",
                            "build_median_ns": "FAILED",
                            "build_std_ns": "FAILED",
                            "error": metrics.get("error", "Unknown error"),
                        }
                    )
                    continue

                if not metrics["rank_times"]:
                    continue

                rows.append(
                    {
                        "data_type": data_type,
                        "bit_size": size,
                        "implementation": impl_name,
                        "k": metrics["k"],
                        "rank_mean_ns": np.mean(metrics["rank_times"]),  # Keep as ns
                        "rank_median_ns": np.median(metrics["rank_times"]),
                        "rank_std_ns": np.std(metrics["rank_times"]),
                        "select_mean_ns": np.mean(metrics["select_times"]),
                        "select_median_ns": np.median(metrics["select_times"]),
                        "select_std_ns": np.std(metrics["select_times"]),
                        "build_mean_ns": np.mean(metrics["build_times"]),
                        "build_median_ns": np.median(metrics["build_times"]),
                        "build_std_ns": np.std(metrics["build_times"]),
                        "error": "",
                    }
                )

        if rows:
            with open(filename, "w", newline="") as f:
                writer = csv.DictWriter(f, fieldnames=rows[0].keys())
                writer.writeheader()
                writer.writerows(rows)
            print(f"✓ Created {filename} with {len(rows)} entries.")


final_results = run_all_experiments()
save_results_to_csv(final_results)
