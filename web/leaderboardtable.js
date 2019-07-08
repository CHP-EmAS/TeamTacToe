 var table = "<table id='leaderboard'>";
 table += "<tr>" +
	"<th>Place</th>" +
	"<th>Player</th>" +
	"<th>Score</th>" +
	"</tr>";
 for(var i = 0; i < 10; i++) {
	 table += "<tr>" +
	 		"<th>" + i + "</th>" +
	 		"<th>" + i + "</th>" +
	 		"<th>" + i + "</th>" +
	 		"</tr>";
 }
 table += "</table>";
 var d2 = document.getElementById('d2').innerHTML = table;