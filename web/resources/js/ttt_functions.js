
var pathSplit = window.location.pathname.split("/");
var gameID = pathSplit[pathSplit.length-1];

var socket;

if(window.location.port === "8080") socket = new WebSocket("ws://" + window.location.host + "/play/ttt/" + gameID);
else socket = new WebSocket("ws://" + window.location.host.split(":")[0] + ":8080/play/ttt/" + gameID);


socket.onopen = function ()
{

};

socket.onclose = function ()
{
    console.log('Verbindung getrennt!');
};

socket.onmessage = function (ev)
{
    console.log('MSG from Server: ' + ev.data);

    if(IsJsonString(ev.data))
    {
        var obj = JSON.parse(ev.data);

        if(obj.hasOwnProperty('cmd'))
        {
            switch(obj.cmd)
            {
                case 'field':
                    console.log(obj.fieldData);
                    updateFieldData(obj.fieldData);
                    break;
                case 'msg':
                    document.getElementById('msgBox').innerHTML  = obj.content;
                    break;
                case 'enableReset':
                    document.getElementById('reset').disabled = false;
                    break;
                default:
                    console.log('Command "' + obj.command + '" is unknown');
                    break;
            }
        }
    }
};

function fieldClick(fieldNum) {
    socket.send('{"cmd":"click","fieldNum":'+fieldNum+'}');
}

function updateFieldData(fieldData){
    for(var i = 0; i < 9; i++)
    {
        if(fieldData[i] === 1)
        {
        	document.getElementById('field' + (i+1)).style.background = "url('/resources/img/ttt_circle.png')";
            //document.getElementById('field' + (i+1)).style.backgroundColor = 'red';
        }
        else if(fieldData[i] === 2)
        {
        	document.getElementById('field' + (i+1)).style.background = "url('/resources/img/ttt_cross.png')";
            //document.getElementById('field' + (i+1)).style.backgroundColor = 'green';
        }
        else
        {
        	document.getElementById('field' + (i+1)).style.background = "none";
            //document.getElementById('field' + (i+1)).style.backgroundColor = 'white';
        }

    }
}

function restartGame() {
    socket.send('{"cmd":"reset"}');
    document.getElementById('reset').disabled = true;
}

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}