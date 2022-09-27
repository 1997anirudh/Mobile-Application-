const express = require('express')
// const fileupload = require('express-fileupload')
const path = require('path');
const url = require("url");
const fs = require("fs");
const multer = require("multer");

const server = express()
const fileLoc = '/public/uploads';
const upload = multer({ dest: __dirname + fileLoc });
server.set('view engine','ejs')
// server.use(fileupload())

//Referenece Link:
// https://stackoverflow.com/questions/72544409/unexpected-end-of-form-error-when-using-multer
server.get("/",async(req,res,next)=>{
  res.status(200)
  res.sendFile("/Users/anirudhsrinivasan/Downloads/MCServer/public/index.html")

})


server.post('/imageupload', upload.single('myfile'), function (request, respond) {
  console.log(url.parse(request.url, true).query.type)
    if (request.file) console.log(request.file);
    fs.rename(__dirname + fileLoc + '/' + request.file.filename, __dirname + fileLoc + '/'+ url.parse(request.url, true).query.type + '/'+ "image" +new Date().toISOString() +".jpeg", function (err) {
        if (err) console.log(err);
        respond.sendFile('/Users/anirudhsrinivasan/Downloads/MCServer/public/index.html')
    })
});



server.listen(8080||process.env.IP||process.env.OPENSHIFT_NODEJS_IP||80,()=>{
  console.log("working")
})
