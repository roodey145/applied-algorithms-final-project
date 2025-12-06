import subprocess

TIMEOUT = 30


# run the given jar package,
# provide the given arg as the command-line
# argument,
# feed the given input file to the stdin of the
# process,
# and return the stdout from the process as string
def run_java(jar: str, arg: str, input_file: str) -> str:
    with open(input_file, "r") as f:
        data = f.read()
    p = subprocess.Popen(
        ["java", "-jar", jar, arg], stdin=subprocess.PIPE, stdout=subprocess.PIPE
    )
    (output, _) = p.communicate(data.encode("utf-8"), timeout=TIMEOUT)
    return output.decode("utf-8")


print(run_java("skewed_binary_trees/app/build/libs/app.jar", "0.1", "input/small.txt"))
