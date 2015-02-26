$(document).ready(function() {	
	
$(".refund").click(function() {
	
	var id = $(this).parent().parent().attr('orderid');
	var buyer = $(this).parent().parent().attr('buyer');
	var header = $(this).parent().parent().attr('header');
	var cost = $(this).parent().parent().attr('cost');
	
	var data = "";
	data +=  "<p class='text-left'>Buyer: "+buyer+"</p>";
	data += "<p class='text-left'>Item: "+header+"</p>";						
	data += "<hr>";
	data +="<h3 class='text-left'>Total Amount: "+cost+"</h3>";

	bootbox.dialog({
		message : data,
		className: "large-modal",
		buttons: {
			success: {
				label : "Refund",
				className : "btn-success",
				callback : function() {
					jsRoutes.controllers.Account
					.createRefund()
					.post(id, function(data) {
						alert(data);
					});
					
				}
			},
			danger: {
				label : "Cancel",
				className : "btn-danger",
				callback : function() {
				
				}
			}
		}

	});
});

$(".fulfill").click(
										function() {
											var thisElement = $(this);
											var url = thisElement.attr("deliverySlip");
											//$.get(url, function(data) {
        									bootbox.dialog({
														message : "<iframe frameBorder='0' seamless width='100%' height='500px' border=0 id='deliverySlip' name='deliverySlip' src="+url+"></iframe>",
														className: "large-modal",
														buttons: {
															success: {
																label : "Print",
																className : "btn-success",
																callback : function() {
																	window.frames["deliverySlip"].focus();
																	window.frames["deliverySlip"].print();
																}
															}
														}
        									
		        									});
        									var frame = $('iframe').get(0);
        								    if (frame != null) {
        								        var frmHead = $(frame).contents().find('head');
        								        if (frmHead != null) {
        								            frmHead.append($('style, link[rel=stylesheet]').clone()); // clone existing css link
        								            //frmHead.append($("<link/>", { rel: "stylesheet", href: "/styles/style.css", type: "text/css" })); // or create css link yourself
        								        }
        								    }   
												//});
											});
});