import csv
import os

csvs = [
    "results_n4096.csv",
    "results_n32768.csv",
    "results_n131072.csv",
    "results_n1048576.csv",
    "results_n8388608.csv",
    "results_n67108864.csv",
    "results_n268435456.csv",
    "results_n536870912.csv",
    "results_n1073741824.csv",
]

output_file = "master_results.csv"
first_file = True

with open(output_file, "w", newline="") as fout:
    writer = None

    for csv_file in csvs:
        if not os.path.exists(csv_file):
            print(f"Skipping {csv_file}: File not found.")
            continue

        with open(csv_file, "r") as fin:
            reader = csv.DictReader(fin)

            # Initialize the writer using the header from the very first file
            if first_file:
                writer = csv.DictWriter(fout, fieldnames=reader.fieldnames)
                writer.writeheader()
                first_file = False

            # Write the rows
            for row in reader:
                writer.writerow(row)

        print(f"Successfully merged {csv_file}")

print(f"\nDone! Created {output_file}")
