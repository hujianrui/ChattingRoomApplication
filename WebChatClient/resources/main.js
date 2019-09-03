let user;
let room;
let msg;
let mySocket;
let element;

function join(){
    let Req = new XMLHttpRequest();
    Req.overrideMimeType('text/plain');
    Req.addEventListener("load", setpage);
    Req.addEventListener("error", error);
    Req.open("GET", "ChatRoom.html");
    Req.send();

    user = document.getElementById('User').value;
    room = document.getElementById('Room').value;

    console.log("user: "+user)
    console.log("room: "+room)

    let send = "join " + room;
    mySocket = new WebSocket("ws://" + location.host);
    mySocket.onopen = function(){
        mySocket.send(send);
    }
    mySocket.onmessage = function(event){
        console.log(event.data);
        let obj = JSON.parse(event.data);
        let para = document.createElement("p");
        para.innerHTML = obj.user + ": " + obj.message;
        element = document.getElementById("div");
        element.appendChild(para);
        document.getElementById("Message").value = "";

        let info = document.getElementById("info");
        info.innerHTML = "User Name: " + user + "<br>" +"Room Name: " + room;

        let chatScroll = document.getElementById("div");
        chatScroll.scrollTop = chatScroll.ScrollHeight;
    }
    mySocket.onerror = function(){
        console.log("error!");
    }

}

function setpage(){
    document.body.innerHTML = this.response;
}

function error(){
    console.log("error");
}


function send(){
    msg = document.getElementById('Message').value;
    let send = user + " " + msg;
    mySocket.send(send);
    console.log(send);
    // mySocket.onmessage = function(event){
    //     console.log(event.data);
    //     let obj = JSON.parse(event.data);
    //     let para = document.createElement("p");
    //     para.innerHTML = obj.user + ": " + obj.message;
    //     element = document.getElementById("div");
    //     element.appendChild(para);
    //     document.getElementById("Message").value = "";

    //     let info = document.getElementById("info");
    //     info.innerHTML = "User Name: " + user + "<br>" +"Room Name: " + room;

    //     let chatScroll = document.getElementById("div");
    //     chatScroll.scrollTop = chatScroll.ScrollHeight;
    // }
}

function exit(){
    let Req = new XMLHttpRequest();
    Req.overrideMimeType('text/plain');
    Req.addEventListener("load", setpage);
    Req.addEventListener("error", error);
    Req.open("GET", "JoinPage.html");
    Req.send();
    mySocket.close();
}