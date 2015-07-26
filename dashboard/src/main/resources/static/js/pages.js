function collectPages(url, callback, page) {
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	
	page = page ? page : 0;
	
	$.ajax({
		beforeSend: function(xhrObj){
			xhrObj.setRequestHeader("Content-Type","application/json");
	        xhrObj.setRequestHeader(header, token);
	    },
	    crossDomain: 'true',
	    url: url + '?page=' + page,
	    success: function(result) {
	    	console.log("Loaded page: " + page + " from " + url);
	    	callback(result._embedded);
	    	if (page<result.page.totalPages) {
	    		collectPages(url, callback, page+1);
	    	}
	    },
	    dataType: 'json'
	});
}