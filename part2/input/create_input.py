import random

# My specs mac OS 12.7.6
L1_SIZE = 4096  # L1 cache
L2_SIZE = 32768  # L2 cache
L3_SIZE = 393216  # L3 cache
MEMORY_SIZE = 1000000  # Beyond cache


SEED: int = 314159

rnd = random.Random(SEED)


def save_to_file(
    fileName: str, bit_string, rank_queries, select_queries, n, num_queries, total_ones
):
    print(f"Creating {fileName} | n={n}, Q={num_queries}, 1s={total_ones}")
    with open(f"{fileName}", "w") as file:
        file.write(f"{n} {num_queries} {total_ones}\n")
        # second line bit string
        file.write(f"{bit_string}\n")
        # rank queries
        file.write(" ".join(map(str, rank_queries)) + "\n")
        # select queries
        file.write(" ".join(map(str, select_queries)))
    print(f"{fileName} created ✓")


def create_locality_list(n, queries, min_val=0):
    """
    Generates queries using a Bimodal Gaussian (Normal) Distribution.
    Instead of picking indices uniformly at random, we cluster queries around
    two "hot spots" (at 25% and 75% of the array length).
    """
    locality = []
    hot_spots = [n // 4, 3 * n // 4]
    for i in range(queries):
        spot = rnd.choice(hot_spots)
        val = int(rnd.gauss(spot, n // 100))
        # Ensure we stay within [min_val, n]
        # For Rank, n is the size (so n-1 is max index)
        # For Select, n is total_ones (so n is max count)
        locality.append(max(min_val, min(n, val)))
    return locality


def generate_test_data(n: int, num_queries: int):
    bits = [rnd.choice(["0", "1"]) for i in range(n)]
    bit_string = "".join(bits)
    # Need to make sure we don't select anything more than total_ones for select
    total_ones = bit_string.count("1")

    # Sequential pattern
    rank_seq = [i % n for i in range(num_queries)]
    select_seq = [(i % total_ones) + 1 for i in range(num_queries)]

    save_to_file(
        f"sequential_data/n_{n}seq.txt",
        bit_string,
        rank_seq,
        select_seq,
        n,
        num_queries,
        total_ones,
    )

    # random uniformly distributed
    rank_rand = [rnd.randint(0, n - 1) for i in range(num_queries)]
    # remember that only numbers <= total_ones are valid as a select query
    select_rand = [rnd.randint(1, total_ones) for i in range(num_queries)]
    save_to_file(
        f"random_data/n_{n}rand.txt",
        bit_string,
        rank_rand,
        select_rand,
        n,
        num_queries,
        total_ones,
    )

    rank_loc = create_locality_list(n - 1, num_queries)
    select_loc = create_locality_list(total_ones, num_queries, 1)
    save_to_file(
        f"locality_data/n_{n}loc.txt",
        bit_string,
        rank_loc,
        select_loc,
        n,
        num_queries,
        total_ones,
    )


sizes = [
    2**12,  # 4K bits   = 16 KB   - L2 (in cache)
    2**15,  # 32K bits  = 128 KB  - L2/L3 boundary
    2**17,  # 131K bits = 524 KB  - Just past L3 → RAM transition ⚡
    2**20,  # 1M bits   = 4 MB    - Solidly in RAM
    2**23,  # 8M bits   = 32 MB   - Large RAM
    2**26,  # 67M bits  = 268 MB  - Very large (test limits)
    2**28,  # 256M bits = 1 GB
    2**29,  # 512M bits = 2 GB
    2**30,
]
num_queries = 100000

for n in sizes:
    print(f"---- Generating Data for n = {n} ---")
    generate_test_data(n, num_queries)
