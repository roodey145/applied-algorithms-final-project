import numpy as np

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
        file.write(f"{arr_size}\n")
        file.write(f"{query_size}\n")
        for elem in arr:
            file.write(f"{elem}\n")
        for elem in queries:
            file.write(f"{elem}\n")


create_data_file("small.txt", 10, 20)
