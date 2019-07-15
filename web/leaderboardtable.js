 var table = "<table id='leaderboard'>";
 table += "<tr>" +
	"<th>Place</th>" +
	"<th>Player</th>" +
	"<th>Score</th>" +
	"</tr>";
 var url = "http://chp-games.de/getBestPlayers";
 var xmlHttpRequest = new XMLHttpRequest();
 xmlHttpRequest.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        var myArr = JSON.parse(this.responseText);
	        parseJSON(myArr);
	    }
	};
 xmlHttpRequest.open("GET", url, true);
 xmlHttpRequest.send();
 //var myArr = JSON.parse('{"list":[{"score":1,"nickname":"EmAS","placment":1},{"score":0,"nickname":"Applebaum007","placment":2},{"score":0,"nickname":"cat","placment":2},{"score":0,"nickname":"GOTT","placment":2},{"score":0,"nickname":"MeLila","placment":2}]}');
 //parseJSON(myArr);
 function parseJSON(arr) {
	 var length = arr.list.length;
	 for(var i = 0; i < length; i++) {
		 console.log(arr.list[i].nickname);
		 table += "<tr>" +
		 		"<th>" + arr.list[i].placment + "</th>" +
		 		"<th>" + arr.list[i].nickname + "</th>" +
		 		"<th>" + arr.list[i].score + "</th>" +
		 		"</tr>";
	 }
	 table += "</table>";
	 var d2 = document.getElementById('d2').innerHTML = table;
 }