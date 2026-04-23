const fs = require('fs');
const path = require('path');

// Configuration
const OUTPUT_FILE = 'output.txt';
const TARGET_JAVA_FOLDER = path.join(process.cwd(), 'src', 'main', 'java');
const POM_FILE = path.join(process.cwd(), 'pom.xml');

/**
 * Utility to recursively get all files in a directory
 */
function getFilesRecursively(dir, fileList = []) {
    if (!fs.existsSync(dir)) return fileList;

    const files = fs.readdirSync(dir);
    files.forEach(file => {
        const fullPath = path.join(dir, file);
        if (fs.statSync(fullPath).isDirectory()) {
            getFilesRecursively(fullPath, fileList);
        } else {
            fileList.push(fullPath);
        }
    });
    return fileList;
}

/**
 * Utility to generate a visual tree structure string
 */
function generateDirectoryTree(rootPath) {
    if (!fs.existsSync(rootPath)) return "Directory not found.\n";

    let tree = "";

    function buildTree(currentPath, prefix) {
        const items = fs.readdirSync(currentPath);
        // Sort items: directories first, then files
        items.sort((a, b) => {
            const aPath = path.join(currentPath, a);
            const bPath = path.join(currentPath, b);
            const aIsDir = fs.statSync(aPath).isDirectory();
            const bIsDir = fs.statSync(bPath).isDirectory();
            if (aIsDir && !bIsDir) return -1;
            if (!aIsDir && bIsDir) return 1;
            return a.localeCompare(b);
        });

        items.forEach((item, index) => {
            const itemPath = path.join(currentPath, item);
            const isLast = index === items.length - 1;
            const connector = isLast ? '└── ' : '├── ';
            tree += `${prefix}${connector}${item}\n`;

            if (fs.statSync(itemPath).isDirectory()) {
                const extension = isLast ? '    ' : '│   ';
                buildTree(itemPath, prefix + extension);
            }
        });
    }

    // Add the root folder name to start
    tree += `${path.basename(rootPath)}\n`;
    buildTree(rootPath, "");
    return tree;
}

// --- Main Execution ---

let outputContent = "";

console.log("Starting generation...");

// 1. Handle pom.xml
if (fs.existsSync(POM_FILE)) {
    console.log("Found pom.xml. Reading content...");
    outputContent += `================================================================================\n`;
    outputContent += `FILE: pom.xml\n`;
    outputContent += `================================================================================\n\n`;

    const pomContent = fs.readFileSync(POM_FILE, 'utf8');
    outputContent += pomContent;
    outputContent += `\n\n`;
} else {
    console.warn("pom.xml not found in the current directory. Skipping.");
}

// 2. Handle src/main/java tree and contents
if (fs.existsSync(TARGET_JAVA_FOLDER)) {
    console.log("Scanning src/main/java...");

    // A. Generate the /f tree
    outputContent += `================================================================================\n`;
    outputContent += `DIRECTORY TREE: src/main/java\n`;
    outputContent += `================================================================================\n\n`;

    const treeStructure = generateDirectoryTree(TARGET_JAVA_FOLDER);
    outputContent += treeStructure;
    outputContent += `\n`;

    // B. Gather files and print contents
    const javaFiles = getFilesRecursively(TARGET_JAVA_FOLDER);

    javaFiles.forEach(filePath => {
        const relativePath = path.relative(process.cwd(), filePath);

        outputContent += `================================================================================\n`;
        outputContent += `FILE: ${relativePath}\n`;
        outputContent += `================================================================================\n\n`;

        try {
            const content = fs.readFileSync(filePath, 'utf8');
            outputContent += content;
        } catch (err) {
            outputContent += `[Error reading file: ${err.message}]`;
        }

        // Ensure new line between files
        outputContent += `\n\n`;
    });
} else {
    console.warn("src/main/java folder not found. Skipping Java source dump.");
}

// 3. Write to output.txt
fs.writeFileSync(OUTPUT_FILE, outputContent, 'utf8');
console.log(`Successfully generated ${OUTPUT_FILE}`);