apitester.filter('markdown',  function($sce){
	return function(value) {
		value = value || "***empty***";
		return $sce.trustAsHtml(markdown.toHTML(value));
	}
});

apitester.filter('shorttype', function(){
	return function(input) {
		if(input.indexOf(".") > 0) {
			var arr = input.split(".");
			return arr[arr.length - 1];
		}
		else {
			return input;
		}
	}
} );