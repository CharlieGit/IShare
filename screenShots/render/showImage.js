const { ipcRenderer, remote, clipboard, nativeImage } = require('electron');
const fs = require("fs");
const path = require("path");

const url = ipcRenderer.sendSync('get-image-url');

var canvas = document.getElementById('canvas');
var ctx = canvas.getContext('2d');

    document.getElementById('clear-button').addEventListener('click', () => {
//        const img = new Image();
//        img.src = url;
//        img.onload = () => {
//            canvas.width = background.naturalWidth;
//            canvas.height = background.naturalHeight;
//            ctx.drawImage(background, 0, 0);
//        }


        ctx.clearRect(0,0,canvas.width,canvas.height);
        ctx.drawImage(background, 0, 0);
//        canvas.width = background.naturalWidth;
//        canvas.height = background.naturalHeight;
        ctx.strokeStyle=""+ "red" +"";
        ctx.beginPath();
    })

    document.getElementById('save-button').addEventListener('click', () => {
        remote.dialog.showSaveDialog({ filters: [{ name: "Images", extensions: ["png", "jpg", "gif"] }] })
            .then(result => {
               const path = result.filePath;
               var dataURL = canvas.toDataURL();
               fs.writeFile(path, new Buffer.from(dataURL.replace("data:image/png;base64,", ""), "base64"),
                           () => { ipcRenderer.send("take-screenshots", { type: "complete", url: dataURL, path }); }
                         );
                         window.close();
             }).catch(err => {
               console.log(err)
             })
    })

    document.getElementById('share-button').addEventListener('click', () => {
        const fileSavedPath = path.join(__dirname, "saveFile.png")
        var dataURL = canvas.toDataURL();
        fs.writeFile(fileSavedPath, new Buffer.from(dataURL.replace("data:image/png;base64,", ""), "base64"),
           () => {
             ipcRenderer.send("take-screenshots", { type: "share", path: fileSavedPath });
             window.close();
           }
         );
        })

        document.getElementById('copy-button').addEventListener('click', () => {
            var dataURL = canvas.toDataURL();
            clipboard.writeImage(nativeImage.createFromDataURL(dataURL))
            window.close();
        });


//    canvas.width = 800;
//    canvas.height = 600;

    var background = new Image();
    background.src = url;
    background.onload = () => {
        console.log(background.naturalWidth)
        canvas.width = background.naturalWidth;
        canvas.height = background.naturalHeight;
        ctx.drawImage(background, 0, 0);

        ctx.strokeStyle=""+ "red" +"";
            ctx.beginPath();
    }

    ctx.strokeStyle=""+ "red" +"";
    ctx.beginPath();

    console.log(55555);

    canvas.addEventListener('mousedown', (e) => {
    console.log(1111, window.isClear)
    ctx.moveTo(e.clientX-canvas.offsetLeft,e.clientY-canvas.offsetTop);
       document.onmousemove=function(e) {
            ctx.lineTo(e.clientX- canvas.offsetLeft, e.clientY- canvas.offsetTop);
            ctx.stroke();
           document.onmouseup=function(e){
           console.log(33333);
               document.onmousedown=null;
               document.onmousemove=null;
            };
        };

    })