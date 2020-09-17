const { app, BrowserWindow } = require("electron");
const { initScreenshots } = require("./screenShots/main/main");
const spawn = require('child_process');
let win;

function createWindow() {
  initScreenshots();

  win = new BrowserWindow({
    width: 100,
    height: 100,
    x: 0,
    y: 0,
    frame: false,
    resizable: false,
    transparent: true,
    maximizable: false,
    skipTaskbar: true,
    alwaysOnTop: true,
    webPreferences: {
      nodeIntegration: true,
    },
  });

  win.loadFile("index.html");

  win.setSkipTaskbar(true);

//  win.webContents.openDevTools();

  win.on("closed", () => {
    win = null;
  });

  spawn.exec('node ./server.js', (err, stdout, stderr) => {
      if(err) {
        console.log('spawn error', err);
      }
  });
}
app.on("ready", createWindow);

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", () => {
  if (win === null) {
    createWindow();
  }
});

