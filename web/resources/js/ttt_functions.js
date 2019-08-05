var pathSplit = window.location.pathname.split("/");
var gameID = pathSplit[pathSplit.length-1];

var socket;

if(window.location.port === "8080") socket = new WebSocket("ws://" + window.location.host + "/play?gameID=" + gameID);
else socket = new WebSocket("ws://" + window.location.host.split(":")[0] + ":8080/play?gameID=" + gameID);


socket.onopen = function ()
{
    socket.send('{"cmd":"connect","gameID":'+gameID+'}');
};

socket.onclose = function ()
{
    console.log('Verbindung getrennt!');
};

function fieldClick(fieldNum) {
    socket.send('{"forward":"' + gameID + '","cmd":"click","fieldNum":'+fieldNum+'}');
}

function restartGame() {
    socket.send('{"forward":"' + gameID + '","cmd":"reset"}');
}

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
                case 'infoMsg':
                    document.getElementById('msgBox').innerHTML  = obj.content;
                    break;
                case 'enableReset':
                    document.getElementById('reset').disabled = false;
                    document.getElementById('reset').visible = true;
                    break;
                case 'disableReset':
                    document.getElementById('reset').disabled = true;
                    document.getElementById('reset').visible = false;
                    break;
                case 'alert':
                    alert(obj.msg);
                    break;
                default:
                    console.log('Command "' + obj.cmd + '" is unknown');
                    break;
            }
        }
    }
};

function updateFieldData(fieldData){
    for(var i = 0; i < 9; i++)
    {
        if(fieldData[i] === 1)
        {
            document.getElementById('field' + (i)).style.background = 'red';
        }
        else if(fieldData[i] === 2)
        {
            document.getElementById('field' + (i)).style.background = 'green';
        }
        else
        {
        	document.getElementById('field' + (i)).style.background = 'white';
        }

    }
}

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}