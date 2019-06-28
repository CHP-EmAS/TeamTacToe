var socket;

if(window.location.port === "8080") socket = new WebSocket("ws://" + window.location.host + "/play");
else socket = new WebSocket("ws://" + window.location.host.split(":")[0] + ":8080/play");

socket.onopen = function ()
{

};

socket.onmessage = function(ev)
{
    if(IsJsonString(ev.data))
    {
        var obj = JSON.parse(ev.data);

        if(obj.hasOwnProperty('cmd'))
        {
            switch(obj.cmd)
            {
                case 'game_created':
                    window.location.href = window.location.protocol + "//" + window.location.host + '/' + obj.gameType + '/' + obj.gameID;
                    break;
                default:
                    console.log('Command "' + obj.command + '" is unknown');
                    break;
            }
        }
    }
};

function createGame(gametype)
{
    socket.send('{"cmd":"createNewGame","type":"'+gametype+'"}');
}

function login(nickname, password)
{
    socket.send('{"cmd":"login","nickname":'+nickname+',"password":'+password+'}');
}

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}