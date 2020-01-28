(function() {
	const params = new URLSearchParams(window.location.search);

	if (params.has("event") && params.has("map")) {
		const sevent = params.get("event");
		const smap = params.get("map");

		$("#loading").style.display = "block";

		fetchEventInfo().then((eventinfo) => {
			$("#loading").style.display = "none";

			const event = eventinfo.events.find(c => c.id === sevent);
			const map = event.maps.find(c => c.id === smap);

			$("#map").innerText = map.name;
			$("#event").innerText = event.name;
			$("#location").innerText = "/tp " + map.location.join(" ");
			$("#mapdiv").style.display = "block";

			if (event.id === "crr" && map.locations) {
				$("#supports_command").style.display = "block";
				$("#mapid").innerText = map.id;
			}

			if (map.authors) {
				$("#authors").style.display = "block";
				$("#authors1").innerHTML = `<b>${map.authors.join("</b>, <b>")}</b>`;
			}

			if (map.image) {
				$("html").style.background = `url("${map.image}") no-repeat center center fixed`;
				$("html").style["background-size"] = "cover";
				$("body").style.background = "none";
				$(".markdown-body").style.background = "rgba(255, 255, 255, 0.85)";
				$(".markdown-body").style["border-radius"] = "10px";
				$(".markdown-body").style.padding = "20px";
			}
		});
	} else {
		// redirect back to website home
		window.location.replace("../");
	}
}());
