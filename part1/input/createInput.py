import numpy as np
import random

# My specs mac OS 12.7.6
L1_SIZE = 4096  # L1 cache (8-byte ints)
L2_SIZE = 32768  # L2 cache
L3_SIZE = 393216  # L3 cache
MEMORY_SIZE = 1000000  # Beyond cache


SEED: int = 314159
rng = np.random.default_rng(SEED)


def input_data(arr_size: int, query_size: int):
    # Generate unique random integers for array 'a'
    a = rng.choice(2**31, size=arr_size, replace=False)

    # Generate unique random integers for query 'q'
    q = rng.choice(2**31, size=query_size, replace=False)

    return a, q


def generate_high_locality_test(
    n, q, concentration=0.8, hot_region=0.2, position="middle", output_file="input.txt"
):
    """
    Generate high locality test file.

    Args:
        n: array size
        q: number of queries
        concentration: fraction of queries in hot region (default 0.8 = 80%)
        hot_region: size of hot region as fraction of array (default 0.2 = 20%)
        output_file: output filename
    """
    # Generate random sorted array
    arr = sorted([random.randint(0, n * 10) for _ in range(n)])

    # Define hot region based on position
    if position == "left":
        # Hot region in leftmost part (first 20% of array)
        hot_start = 0
        hot_end = int(n * hot_region)
    elif position == "right":
        # Hot region in rightmost part (last 20% of array)
        hot_start = int(n * (1 - hot_region))
        hot_end = n - 1
    elif position == "middle":
        # Hot region in middle
        hot_start = int(n * (0.5 - hot_region / 2))
        hot_end = int(n * (0.5 + hot_region / 2))
    else:
        raise ValueError(
            f"Unknown position: {position}. Use 'left', 'middle', or 'right'"
        )

    hot_min = arr[hot_start]
    hot_max = arr[hot_end]

    # Generate queries
    queries = []
    num_hot = 0
    num_cold = 0
    for _ in range(q):
        if random.random() < concentration:
            # Query in hot region (80%)
            queries.append(random.randint(hot_min, hot_max))
            num_hot += 1
        else:
            # Query anywhere (20%)
            queries.append(random.randint(arr[0], arr[-1]))
            num_cold += 1

    # Write to file
    with open(output_file, "w") as f:
        f.write(f"{n} {q}\n")
        f.write(" ".join(map(str, arr)) + "\n")
        f.write(" ".join(map(str, queries)) + "\n")


def create_data_file(filename: str, arr_size: int, query_size: int):
    arr, queries = input_data(
        arr_size,
        query_size,
    )
    # Since the input will be read in java, we ensure that the first two ints
    # refer to the size of the array and the the size of the queries that will be given as argument to
    # the pred method
    with open(f"./{filename}", "w") as file:
        print(f"Creating file {filename}...")
        file.write(f"{arr_size}\n")
        file.write(f"{query_size}\n")
        for elem in arr:
            file.write(f"{elem}\n")
        for elem in queries:
            file.write(f"{elem}\n")
        print(f"File created ✓  ")


# create_data_file("small.txt", 10, 20)

# Fits entirely in L2 cache, this file is in github no need to run
# create_data_file("data_50k.txt", 50_000, 500_000)

# Fits in l3 cache
# create_data_file("data_500k.txt", 500_000, 5_000_000)

# This file will exceed all caches on my specs (cache miss heavy)
# Can increase the numbers as needed to ensure its big enough
# create_data_file("data_2m.txt", 2_000_000, 20_000_000)

# L1 cache test high locality

generate_high_locality_test(
    n=L1_SIZE,
    q=100_000,
    position="left",
    output_file="locality_files/l1_high_locality.txt",
)
generate_high_locality_test(n=L3_SIZE, q = 100_000, position = "left", output_file = "locality_files/l3_high_locality.txt")
