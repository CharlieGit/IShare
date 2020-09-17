const { app, BrowserWindow } = require("electron");
const { initScreenshots } = require("./screenShots/main/main");
const path = require("path");

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
