const express = require('express');
const app = express();
const path = require('path');
const config = require('./constants');



app.use('/ishare', express.static(path.resolve(__dirname, './screenShots/render')));
app.listen(config.port, function(err){
    console.log(`listening port ${config.port}...`);
})
