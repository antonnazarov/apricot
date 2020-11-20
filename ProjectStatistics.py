#
# This script counts classes and lines of code (excluding the empty strings) for all
# non testing classes in the Maven project
#
import os

cnt = 0
line_count = 0
for root, dirs, files in os.walk("."):
    for file in files:
        if file.endswith(".java"):
            f_name = os.path.join(root, file)
            if "src\\test" not in f_name:
                print(os.path.join(root, file) + " ---> " + file)
                cnt += 1
                file = open(f_name, "r")
                for line in file:
                    if line != "\n":
                        line_count += 1
                file.close()
print("--------------------------------------------------------------------")
print("---> " + str(cnt) + " classes have been found, with " + str(line_count) + " lines of code")
print("--------------------------------------------------------------------")
