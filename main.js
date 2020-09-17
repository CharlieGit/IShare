const { app, BrowserWindow, clipboard } = require("electron");
const { initScreenshots } = require("./screenShots/main/main");
const spawn = require('child_process');
const ip = require('ip');
const IPAddress = ip.address();
const config = require('./constants');
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
//    maximizable: false,
    webPreferences: {
      nodeIntegration: true,
    },
  });

  win.loadFile("index.html");

//  win.webContents.openDevTools();

  win.on("closed", () => {
    win = null;
  });

  clipboard.writeText(`http://${IPAddress}:${config.port}/ishare/screenShot.png`, 'selection')
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

