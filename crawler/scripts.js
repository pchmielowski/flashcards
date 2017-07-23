var start = new Date();


var verbs = () => throw "Choose language";
// greek
//var verbs = () => Array.from(window.document.getElementsByClassName('commonverb'));
// italian
//var verbs = () => Array.from(window.document.getElementsByClassName('wordcloud')[0].getElementsByTagName('a')).map(a => a.href)

var verbAsString = () => Array.from(window.document.getElementById('verb').contentWindow.document.getElementById('present').nextSibling.tBodies[0].rows)
				.map(it => [ it.cells[1].innerHTML, it.cells[2].innerHTML])
				.toString();
document.body.innerHTML += '<iframe id=verb></iframe>'



var iframe = document.getElementById('verb');
iframe.addEventListener("load", () => {
	try {
	    words.push(verbAsString());
	    console.log(words.length);
	} catch (error) {
		console.log(error);
	}
	if (verbsStack.length === 0) {
		document.body.innerHTML = words.join('<br>');
        console.log((new Date().getTime() - start.getTime()) / 1000)
		return;
	}
	iframe.src = verbsStack.pop();
});


words = []
verbsStack = verbs();
iframe.src = verbsStack.pop();

