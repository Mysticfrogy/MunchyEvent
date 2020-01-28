function fetchEventInfo() {
	return new Promise((resolve, reject) => {
		const req = new XMLHttpRequest();
		req.addEventListener("load", () => {
			resolve(JSON.parse(req.responseText));
		});
		req.open("GET", "https://cdn.hpfxd.nl/misc/eventinfo.json");
		req.send();
	});
}

const $ = (str) => {
	const s = document.querySelectorAll(str);
	return s.length === 1 ? s[0] : s
};