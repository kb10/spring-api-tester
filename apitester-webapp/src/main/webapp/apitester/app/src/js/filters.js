apitester.filter('markdown',  function($sce){
	return function(value) {
		value = value || "***empty***";
		return $sce.trustAsHtml(markdown.toHTML(value));
	}
});