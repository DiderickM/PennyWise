Compile this project with Java SE 17
=================================

Summary
-------
This project was originally written targeting Java SE 25. If you only have JDK 17 available you can still compile and run the code by using the `--release 17` option when compiling. The steps below show how to compile and run on Windows PowerShell with only the JDK installed.

Prerequisites
-------------
- JDK 17 installed and `java` / `javac` on your PATH. Verify with:

```powershell
java -version
javac -version
```

Quick steps (PowerShell)
------------------------
From the repository folder that contains the `src` directory (the `PennyWise` folder), run the following commands.

1) Create an output directory and compile all sources for Java 17:

```powershell
cd PennyWise
mkdir -Force out
$files = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --release 17 -d .\out $files
```

2) Run the application (use classpath pointing at the compiled `out` directory):

```powershell
java -cp .\out pennywise.App
```

Troubleshooting
---------------
- If `javac` fails but `java -version` shows Java 17, check that `javac` and `java` are from the same JDK installation. On Windows it's common to have a JRE in PATH first.
- If you see lexical or syntax issues (for example, new keywords or class file version errors), the source is using features not supported by `--release 17` — you will need to refactor those parts.
