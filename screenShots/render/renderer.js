const { ipcRenderer, clipboard, nativeImage, remote } = require("electron");

const fs = require("fs");
const { getScreenSources } = require("./takeScreenShots");
const { CaptureEditor } = require("./editor");
const { getCurrentScreen } = require("./utils");

const $canvas = document.getElementById("js-canvas");
const $bg = document.getElementById("js-bg");
const $sizeInfo = document.getElementById("js-size-info");
const $toolbar = document.getElementById("js-toolbar");

const $btnClose = document.getElementById("js-tool-close");
const $btnOk = document.getElementById("js-tool-ok");
const $btnSave = document.getElementById("js-tool-save");
const $btnReset = document.getElementById("js-tool-reset");

const audio = new Audio();
audio.src = "./assets/audio/capture.mp3";

const currentScreen = getCurrentScreen();


document.body.addEventListener(
  "mousedown",
  (e) => {
    if (e.button === 2) {
      window.close();
    }
  },
  true
);


getScreenSources().then((stream) => {

  let capture = new CaptureEditor($canvas, $bg, stream);

  let onDrag = (selectRect) => {
    $toolbar.style.display = "none";
    $sizeInfo.style.display = "block";
    $sizeInfo.innerText = `${selectRect.w} * ${selectRect.h}`;
    if (selectRect.y > 35) {
      $sizeInfo.style.top = `${selectRect.y - 30}px`;
    } else {
      $sizeInfo.style.top = `${selectRect.y + 10}px`;
    }
    $sizeInfo.style.left = `${selectRect.x}px`;
  };
  capture.on("start-dragging", onDrag);
  capture.on("dragging", onDrag);

  let onDragEnd = () => {
    if (capture.selectRect) {
      ipcRenderer.send("take-screenshots", {
        type: "select",
        screenId: currentScreen.id,
      });
      const { r, b } = capture.selectRect;
      let top = b + 15;
      const height = 44;
      const margin = 8;
      if (top + height >= currentScreen.bounds.height) {
        top = b - height - margin;
      }
      $toolbar.style.display = "flex";
      $toolbar.style.top = `${top}px`;
      $toolbar.style.right = `${window.screen.width - r}px`;
    }
  };
  capture.on("end-dragging", onDragEnd);

  ipcRenderer.on("take-screenshots", (e, { type, screenId }) => {
    if (type === "select") {
      if (screenId && screenId !== currentScreen.id) {
        capture.disable();
      }
    }
  });

  capture.on("reset", () => {
    $toolbar.style.display = "none";
    $sizeInfo.style.display = "none";
    document.body.style.cursor = "auto";
  });

  $btnClose.addEventListener("click", () => {
    ipcRenderer.send("take-screenshots", {
      type: "close",
    });
    window.close();
  });

  $btnReset.addEventListener("click", () => {
  share();
//    capture.reset();
  });

  let share = () => {
    if (!capture.selectRect) {
      return;
    }

    let url = capture.getImageUrl();

    remote.getCurrentWindow().hide();

    const path = require("path");

    const fileSavedPath = path.join(__dirname, "saveFile.png")

    fs.writeFile(
       fileSavedPath,
       new Buffer.from(url.replace("data:image/png;base64,", ""), "base64"),
       () => {
         ipcRenderer.send("take-screenshots", {
           type: "share",
           path: fileSavedPath,
         });
         window.close();
       }
     );
  }

  let selectCapture = () => {
    if (!capture.selectRect) {
      return;
    }
    let url = capture.getImageUrl();
    remote.getCurrentWindow().hide();

    audio.play();
    audio.onended = () => {
      window.close();
    };
    clipboard.writeImage(nativeImage.createFromDataURL(url));
    ipcRenderer.send("take-screenshots", {
      type: "showImage",
      url,
    });
  };
  $btnOk.addEventListener("click", selectCapture);

  $btnSave.addEventListener("click", () => {
    let url = capture.getImageUrl();

    remote.getCurrentWindow().hide();
    remote.dialog.showSaveDialog({ filters: [{ name: "Images", extensions: ["png", "jpg", "gif"] }] })
    .then(result => {
       console.log(result.canceled)
       console.log(result.filePaths)
       const path = result.filePath;

             ipcRenderer.send("take-screenshots", {
                         type: "console",
                         path: path,
                       });

       fs.writeFile(
                   path,
                   new Buffer.from(url.replace("data:image/png;base64,", ""), "base64"),
                   () => {
                     ipcRenderer.send("take-screenshots", {
                       type: "complete",
                       url,
                       path,
                     });
                     window.close();
                   }
                 );
     }).catch(err => {
       console.log(err)
     })
  });

  window.addEventListener("keypress", (e) => {
    if (e.code === "Enter") {
      share();
    }
  });
});
