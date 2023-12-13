const fs = require('fs');
const path = require('path');

const sourceFolder = path.join(__dirname, 'build/static/');
const destinationFolder = path.join(__dirname, '../ui.apps/src/main/content/jcr_root/apps/aem-component-versioning/clientlibs/clientlib-react/');

// Function to recursively copy files
function copyFiles(source, destination, relativePath = '') {
  const parentDir = path.dirname(destination);
  if (!fs.existsSync(parentDir)) {
    fs.mkdirSync(parentDir, { recursive: true });
  }

  if (!fs.existsSync(destination)) {
    fs.mkdirSync(destination);
  }

  const cssTxtPath = path.join(destination, 'css.txt');
  const jsTxtPath = path.join(destination, 'js.txt');

  let cssFiles = [];
  let jsFiles = [];

  fs.readdirSync(source).forEach((file) => {
    const sourcePath = path.join(source, file);
    const destinationPath = path.join(destination, file);

    if (fs.lstatSync(sourcePath).isDirectory()) {
      copyFiles(sourcePath, destination, path.join(relativePath, file));
    } else {
      const extension = path.extname(file).toLowerCase();
      if (extension === '.js') {
        fs.copyFileSync(sourcePath, destinationPath);
        jsFiles.push(path.join(relativePath, file));
      } else if (extension === '.css') {
        fs.copyFileSync(sourcePath, destinationPath);
        cssFiles.push(path.join(relativePath, file));
      }
    }
  });

  if (cssFiles.length > 0) {
    const cssFileList = cssFiles.map((file) => file.replace(/^css\//, '')).join('\n');
    fs.writeFileSync(cssTxtPath, cssFileList);
  }

  if (jsFiles.length > 0) {
    const jsFileList = jsFiles.map((file) => file.replace(/^js\//, '')).join('\n');
    fs.writeFileSync(jsTxtPath, jsFileList);
  }
}

// Copy JS and CSS files and create txt files
copyFiles(sourceFolder, destinationFolder);
