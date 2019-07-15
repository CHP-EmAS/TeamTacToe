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
                    if(obj.type === "little") updateSmallFieldData(obj.fieldData);
                    else if(obj.type === "big") updateBigFieldData(obj.fieldData,obj.currentfield,obj.bigFieldData);
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
                case 'roundPromt':
                    var roundstr = prompt("In wie vielen Inceptionebenen willst du eintauchen? (Rundenanzahl 1-50)", "5");
                    var rounds = 5;

                    if (roundstr != null || roundstr != "") {
                        rounds = parseInt(roundstr, 10);
                        if(!isNaN(rounds))
                        {
                            socket.send('{"forward":"' + gameID + '","cmd":"setRounds","amount":'+rounds+'}');
                        }
                    }
                    break;
                case 'alert':
                    alert(obj.msg);
                    break;
                case 'changeToSmall':
                    loadSmallField();
                    break;
                case 'changeToBig':
                    loadBigField();
                    break;
                default:
                    console.log('Command "' + obj.cmd + '" is unknown');
                    break;
            }
        }
    }
};

function updateSmallFieldData(fieldData){
    for(var i = 0; i < 9; i++)
    {
        if(fieldData[i] === 1)
        {
            document.getElementById('field' + i).style.backgroundColor = 'red';
        }
        else if(fieldData[i] === 2)
        {
            document.getElementById('field' + i).style.backgroundColor = 'green';
        }
        else
        {
            document.getElementById('field' + i).style.backgroundColor = 'white';
        }

    }
}

function updateBigFieldData(fieldData,currentfield,bigFieldData){

    for(var i = 0; i < 81; i++) {
        if (fieldData[i] === 1) {
            document.getElementById('field' + (i)).style.background = 'red';
        } else if (fieldData[i] === 2) {
            document.getElementById('field' + (i)).style.background = 'green';
        } else {
            document.getElementById('field' + (i)).style.background = 'white';
        }
    }

    for(var bi = 0; bi < 9; bi++)
    {
        var x = parseInt(bi)%3;
        var y = parseInt(bi/3);

        var elements;
        var disabled = false;

        if(bigFieldData[bi] === 1)
        {
            document.getElementById('subTable_' + y + '_' + x).style.background = 'red';
            disabled = true;
        }
        else if(bigFieldData[bi] === 2)
        {
            document.getElementById('subTable_' + y + '_' + x).style.background = 'green';
            disabled = true;
        }
        else if(bigFieldData[bi] === -1)
        {
            document.getElementById('subTable_' + y + '_' + x).style.background = 'grey';
            disabled = true;
        }
        else
        {
            if(bi === currentfield)
            {
                document.getElementById('subTable_' + y + '_' + x).style.background = 'yellow';
                disabled = false;
            }
            else
            {
                document.getElementById('subTable_' + y + '_' + x).style.background = 'white';
                disabled = true;
            }
        }

        elements = document.getElementsByClassName('buttonGroup_' + y + '_' + x);
        for (var e = 0; e < elements.length; e++) {
            elements[e].disabled = disabled;
        }
    }
}

function loadBigField()
{
    var table = "<table id='bigBoy'>";
    var y_big;
    var x_big;
    var c = 0;
    for(y_big = 0; y_big < 3; y_big++) {
        table += "<tr>"
        for(x_big = 0; x_big < 3; x_big++) {
            table += "<td><table class='subTable' id=subTable_" + y_big + "_" + x_big + ">"
            var y, x;
            for(y = 0; y < 3; y++) {
                table += "<tr>";
                for(x = 0; x < 3; x++) {
                    table += "<td>";
                    table += "<button class='fieldButton buttonGroup_"+ y_big + "_" + x_big + "' id='field" + c + "' onclick='fieldClick(" + c + ")'>";
                    table += "</td>";
                    c++;
                }
                table += "</tr>";
            }
            table += "</td></table>";
        }
        table += "</tr>";
    }
    table += "</table>";
    table += "<h1 id='msgBox'></h1>";
    table += "<button id='reset' disabled onclick='restartGame()'>Nochmal!</button>";
    document.getElementById('d2').innerHTML = table;
}

function loadSmallField()
{
    var table = "<table id='bigBoy'>";
    var y_big;
    var x_big;
    var c = 0;

    table += "<tr>"
    table += "<td><table class='subTable' id=subTable_" + 0 + "_" + 0 + ">"

    var y, x;
    for(y = 0; y < 3; y++)
    {
        table += "<tr>";
        for(x = 0; x < 3; x++)
        {
            table += "<td>";
            table += "<button class='fieldButton buttonGroup_"+ 0 + "_" + 0 + "' id='field" + c + "' onclick='fieldClick(" + c + ")'>";
            table += "</td>";
            c++;
        }
        table += "</tr>";
    }

    table += "</td></table>";
    table += "</tr>";

    table += "</table>";
    table += "<h1 id='msgBox'></h1>";
    table += "<button id='reset' disabled onclick='restartGame()'>Nochmal!</button>";
    document.getElementById('d2').innerHTML = table;
}

window.onload = function(e){
    loadSmallField();
};

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}