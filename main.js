const { app, BrowserWindow } = require("electron");
const { initScreenshots } = require("./screenShots/main/main");
const path = require("path");

let win;

function createWindow() {
  initScreenshots();

  win = new BrowserWindow({
    width: 140,
    height: 100,
    x: 0,
    y: 0,
    resizable: false,
    webPreferences: {
      nodeIntegration: true,
    },
  });

  win.loadFile("index.html");

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
