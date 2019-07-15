function hashSubmit()
{
    document.getElementById('pw').value = hex_sha512(document.getElementById('nopassword').value);
    document.getElementById('nopassword').value = "#";
    return true;
}

function createGame(gametype)
{
    var req = new XMLHttpRequest();
    req.open('GET','/gameController?mode=createGame&gameType='+gametype, true);

    req.onreadystatechange = function receive() {
        if(req.readyState === 4){
            window.location = req.responseText;
        }
    };

    req.send();
}

function joinGame()
{
    var gameID = prompt("Bitte Spiel ID eingeben:", "");

    if (gameID != null) {
        if(gameID.length !== 0) {
            var req = new XMLHttpRequest();
            req.open('GET','/gameController?mode=gameExists&gameID='+ gameID, true);

            req.onreadystatechange = function receive() {
                if(req.readyState === 4) {
                    var request = req.responseText;

                    if (IsJsonString(request)) {
                        var obj = JSON.parse(request);

                        if (obj.req === "true") {
                            window.location = '/' + obj.type + '/' + gameID;
                        } else {
                            alert('Spiel ' + gameID + ' wurde nicht gefunden!');
                        }
                    }
                }
            };

            req.send();
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
