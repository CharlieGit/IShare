const { BrowserWindow, ipcMain, globalShortcut, screen, clipboard } = require("electron");
const os = require("os");
const path = require("path");
const ip = require('ip');
const IPAddress = ip.address();
const config = require('../../constants');

let captureWins = [];

const hideAllWindow = () => {
  const captureWinIds = captureWins.map((w) => w.id);
  BrowserWindow.getAllWindows().forEach((window) => {
    if (!captureWinIds.includes(window.id)) {
      window.hide();
    }
  });
};

const showAllWindow = () => {
  const captureWinIds = captureWins.map((w) => w.id);
  BrowserWindow.getAllWindows().forEach((window) => {
    if (!captureWinIds.includes(window.id)) {
      window.show();
    }
  });
};

const captureScreen = (e, args) => {
  if (captureWins.length) {
    return;
  }
  hideAllWindow();
  const { screen } = require("electron");

  let displays = screen.getAllDisplays();
  captureWins = displays.map((display) => {
    let captureWin = new BrowserWindow({
      fullscreen: os.platform() === "win32" || undefined,
      width: display.bounds.width,
      height: display.bounds.height,
      x: display.bounds.x,
      y: display.bounds.y,
      transparent: true,
      frame: false,
      // skipTaskbar: true,
      // autoHideMenuBar: true,
      movable: false,
      resizable: false,
      skipTaskbar: true,
      enableLargerThanScreen: true,
      hasShadow: false,
      webPreferences: {
        nodeIntegration: true,
      },
    });
//    captureWin.webContents.openDevTools();
    // captureWin.setAlwaysOnTop(true, "screen-saver");
    captureWin.setVisibleOnAllWorkspaces(true);
    captureWin.setFullScreenable(false);

    console.log(__dirname)
    console.log(path.join(__dirname, "../render/render.html"))
    captureWin.loadFile(path.join(__dirname, "../render/render.html"));

    let { x, y } = screen.getCursorScreenPoint();
    if (
      x >= display.bounds.x &&
      x <= display.bounds.x + display.bounds.width &&
      y >= display.bounds.y &&
      y <= display.bounds.y + display.bounds.height
    ) {
      captureWin.focus();
    } else {
      captureWin.blur();
    }

//     captureWin.openDevTools()

    captureWin.on("closed", () => {
      let index = captureWins.indexOf(captureWin);
      if (index !== -1) {
        captureWins.splice(index, 1);
      }
      captureWins.forEach((win) => win.close());
      showAllWindow();
    });
    return captureWin;
  });
};

const initScreenshots = () => {
  globalShortcut.register("Esc", () => {
    if (captureWins) {
      captureWins.forEach((win) => win.close());
      captureWins = [];
    }
  });

  globalShortcut.register("CmdOrCtrl+Shift+A", captureScreen);

  ipcMain.on("take-screenshots", (e, { type = "start", screenId, url, path } = {}) => {
    if (type === "start") {
      captureScreen();
    } else if (type === "complete") {
    console.log(url)
    console.log(11111)
    console.log(path)
      // nothing
    } else if (type === "select") {
      captureWins.forEach((win) =>
        win.webContents.send("take-screenshots", { type: "select", screenId })
      );
    } else if (type === "cancel") {
//        console.log(222);
//        console.log(url);
    } else if (type === 'console') {
        console.log(path);
    } else if (type === 'showImage') {
const { width, height } = screen.getPrimaryDisplay().workAreaSize
        const win = new BrowserWindow({
            width: width - 300,
            height: height - 150,
            skipTaskbar: true,
            webPreferences: {
              nodeIntegration: true,
            },
          });

//           console.log(111);
//           console.log(url);
//           console.log(111);
           const path = require("path");
           ipcMain.once('get-image-url', (event) => {
                event.returnValue = url;
           })
//           win.webContents.openDevTools();
          win.loadFile(path.join(__dirname, "../render/showImage.html"));
    } else if (type === 'share') {
        console.log(333333)
        console.log(path)
        console.log(333333)
        clipboard.writeText(`http://${IPAddress}:${config.port}/ishare/screenShot.png`, 'selection')
    }
  });
};

exports.initScreenshots = initScreenshots;
exports.captureSceen = captureScreen;
