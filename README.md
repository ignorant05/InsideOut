# Compression Tool

A Java-based tool for handling various compression formats including **Zstandard (zstd)**, **XZ**, **Gzip (gz)**, **Zip**, **Tar**, and **Bzip2**. This tool allows users to compress and decompress files easily via the command line.

## Features

- Supports multiple compression formats: zstd, xz, gz, zip, tar, bzip2
- Easy to use command line interface
- Written in Java

## Requirements

- Java Development Kit (JDK) 8 or higher
- Apache Commons Compress library (included in the `lib` folder)

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/compression-tool.git
   cd compression-tool
Compile the source code:

bash
Copy code
javac -d bin -cp "lib/*" src/**/*.java
Usage
Decompression
To decompress a file, use the following command:

bash
Copy code
java -cp "bin:lib/*" InsideOut -d -p <path_to_compressed_file>
Compression
To compress a file, use the following command:

bash
Copy code
java -cp "bin:lib/*" InsideOut -c -p <path_to_input_file> -t <compression_format>
Parameters
-d : Specifies that the operation is decompression.
-c : Specifies that the operation is compression.
-p <file_path> : Path to the file to be compressed or decompressed.
-t <format> : Compression format (for compression only, e.g., zstd, xz, gz, zip, bzip2).
Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bugs.

License
This project is licensed under the MIT License - see the LICENSE file for details.

css
Copy code

This version uses placeholders for the file paths and compression formats, making it more general and applicable to a wider range of use cases.
