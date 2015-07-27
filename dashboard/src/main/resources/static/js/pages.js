function collectPages(url, collector, callback, page) {
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	
	page = page ? page : 0;
	
	$.ajax({
		beforeSend: function(xhrObj){
			xhrObj.setRequestHeader("Content-Type","application/json");
	        xhrObj.setRequestHeader(header, token);
	    },
	    crossDomain: 'true',
	    url: url + '?page=' + page + '&size=200',
	    success: function(result) {
	    	console.log("Loaded page: " + page + " from " + url);
	    	collector(result._embedded);
	    	if (page<result.page.totalPages-1) {
	    		collectPages(url, collector, callback, page+1);
	    	} else {
	    		callback()
	    	}
	    },
	    dataType: 'json'
	});
}