import numpy as np
import random

# My specs mac OS 12.7.6
L1_SIZE = 4096  # L1 cache
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


# Fits in L1
create_data_file("uniform_data/l1_cache_uniform_4096.txt", 4096, 100_000)

# Fits in L2
create_data_file("uniform_data/L2_cache_uniform_32_768.txt", 32_768, 500_000)

# Fits in L3
create_data_file("uniform_data/L3_cache_uniform_393_216.txt", 393_215, 2_000_000)

# Memory level
create_data_file("uniform_data/RAM_uniform_1_000_000.txt", 1_000_000, 5_000_000)
