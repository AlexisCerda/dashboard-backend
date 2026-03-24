const fs = require('fs');
const path = require('path');

function processDir(dir) {
    const files = fs.readdirSync(dir);
    for (const file of files) {
        const fullPath = path.join(dir, file);
        if (fs.statSync(fullPath).isDirectory()) {
            processDir(fullPath);
        } else if (fullPath.endsWith('.java')) {
            try {
                let code = fs.readFileSync(fullPath, 'utf8');
                
                // Remove block comments /* ... */
                code = code.replace(/\/\*[\s\S]*?\*\//g, '');
                
                // Remove line comments // ...
                // Be careful not to match // inside strings, though in Java // is almost always a comment if it's not in a protocol string
                // A safe enough regex for this context:
                code = code.replace(/^\s*\/\/.*$/gm, ''); // Line-starting comments
                code = code.replace(/[^\:]\/\/.*$/gm, (match) => match.split('//')[0]); // End-of-line comments (ignoring http://)
                
                fs.writeFileSync(fullPath, code, 'utf8');
            } catch(e) {
                console.error("Error processing " + fullPath + ": " + e.message);
            }
        }
    }
}

const targetDir = path.join(__dirname, 'src', 'main', 'java');
if (fs.existsSync(targetDir)) {
    processDir(targetDir);
    console.log("Done removing comments from backend!");
} else {
    console.error("Directory not found: " + targetDir);
}
