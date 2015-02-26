

	$(document).ready(function() {
						$(".fulfill").click(
										function() {
											var thisElement = $(this);
											var url = thisElement.attr("deliverySlip");
											//$.get(url, function(data) {
        									bootbox.dialog({
														message : "<iframe frameBorder='0' seamless width='100%' height='500px' border=0 id='deliverySlip' name='deliverySlip' src="+url+"></iframe>",
														title : "Confirm order fulfilment",
														className: "large-modal",
														buttons: {
															success: {
																label : "Print",
																className : "btn-success",
																callback : function() {
																	window.frames["deliverySlip"].focus();
																	window.frames["deliverySlip"].print();
																}
															},
															danger: {
																label : "Revoke",
																className : "btn-danger",
																callback : function() {
																
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

						$(".Unverified")
								.click(
										function() {

											var deliverydetails = "";

											if ($(this).attr('delivery') != "Click and Collect") {
												deliverydetails += $(this)
														.attr('deliverydetails');
											}
											var details = "<div class='panel panel-default'>"
													+ "<div class='panel-heading'>"
													+ "<h3 class='panel-title'><b>"
													+ $(this).attr('header')
													+ "</b> for &pound;"
													+ $(this).attr('cost')
													+ "</h3>"
													+ "</div>"
													+ "<div class='panel-body'>"
													+ $(this).attr('size')
													+

													"</div></div>"
													+ "<div class='well well-lg'>"
													+ $(this).attr('delivery')
													+ "<br/>"
													+ deliverydetails
													+ "</div>";

											var thisElement = $(this);
         										bootbox.dialog({
														message : details,
														title : "Confirm order fulfilment",
														buttons: {
															success: {
																label : "Confirm",
																className : "btn-success",
																callback : function() {
																	verify(
																			thisElement
																					.attr('orderid'),
																			thisElement
																					.attr('sellersite'),
																			thisElement
																					.attr('buyerid'),
																			thisElement
																					.attr('header'));
																	thisElement
																			.toggleClass('Unverified');
																	thisElement
																			.toggleClass('Verified');
																}
															},
																	danger: {
																label : "Revoke",
																className : "btn-danger",
																callback : function() {
																	revoke(
																			thisElement
																					.attr('orderid'),
																			thisElement
																					.attr('sellersite'),
																			thisElement
																					.attr('buyerid'),
																			thisElement
																					.attr('header'));
																	thisElement
																			.toggleClass('Unverified');
																	thisElement
																			.toggleClass('Revoked');
																}
															}
														}
													});
										});

						$('#myTable').dataTable({
						//  "sDom": '<"toolbar">frtip'
						});

						$("div.toolbar")
								.html(
										'<b>Select: </b><a href="#" class="btn btn-default" role="button">Unverified</a> <a href="#" class="btn btn-default" role="button">Out of stock</a>');

					});


	$('#connect').pulsate({
		speed : 2500,
		color : "#000"
	});
	$('.checkButton').click(function() {

		var id = $(this).attr('id');
		var code = document.getElementById("i-" + id).value;
		checkCode(id, code);
	});
	function verify(id, site, buyer, item) {
		check(id, site, buyer, item);
		var el = "v-" + id;

	}

	function checkCode(id, code) {
		jsRoutes.controllers.Account.checkCollectionCode().ajax({
			data : {
				idVal : id,
				codeVal : code
			},
			type : "POST",

			success : function(data) {
				var el = "r-" + id;
				$("#" + el).addClass("Revoked");
				$("#" + id).remove();
			},
			error : function(err) {
				bootbox.alert("Incorrect collection code entered.");

			}

		});
	}

	function check(id, site, from, item) {

		var html = "<h3 class='page-header'>Send a custom message to the buyer!</h3>";

		html += "<form name='verify-form' action='@routes.Account.sendConfirmation()' id='verify-form' method='POST' enctype='multipart/form-data'>";
		html += "<label for='subject'>Subject:</label><div class='input-group'><input type='text' name='subject' id='subject' class='form-control' size='70' value='LASU Purchase Confirmation - "+item+"'></div>";
		html += "<label for='des'>Content:</label><div class='input-group'><textarea name='des' id='des' class='form-control' rows='4' cols='70'>Thanks for your purchase from "
				+ site + "</textarea></div>";
		html += "<input type='hidden' name='from' value='"+from+"'>";
		html += "<input type='hidden' id='v' name='v' value='"+id+"'>";
		html += "</form>";
		bootbox
				.dialog({
					message : html,
					buttons : {
						success : {
							label : "Send Message",
							type : "submit",
							className : "btn-success",
							callback : function() {

								jsRoutes.controllers.Account
										.sendConfirmation()
										.ajax(
												{
													data : $("#verify-form")
															.serialize(),
													success : function(data) {

														var el = "v-" + id;
														var i = document
																.getElementById(el);
														i.innerHTML = '<i class="glyphicon glyphicon-ok"></i>';

														el = "revoke-" + id;
														var i = document
																.getElementById(el);
														i.style.visibility = "hidden";
														el = "verify-" + id;
														var i = document
																.getElementById(el);
														i.style.visibility = "hidden";

														var collectButton = "e-"
																+ id;
														var cb = document
																.getElementById(collectButton);

														var newdiv = document
																.createElement('div');

														newdiv.setAttribute(
																'class',
																'input-group');

														newdiv.innerHTML = "<input id='i-"+id+"' type='text' class='form-control' maxlength='4'><span class='input-group-btn'><button id='"+id+"' class='checkButton btn btn-default' type='button'>Go!</button></span>";

														cb.appendChild(newdiv);

													},
													error : function(err) {
														alert("Error - Have you connected with Stripe?");

													}
												});
							}
						}
					}
				});

	}
	function revoke(id, site, from, item) {

		var html = "<h3 class='page-header'>Why can't you fulfil this order?</h3>";

		html += "<form name='deny-form' action='@routes.Account.sendRefutation()' id='deny-form' method='POST' enctype='multipart/form-data'>";
		html += "<label for='subject'>Subject:</label><div class='input-group'><input type='text' name='subject' id='subject' class='form-control' size='70' value='LASU Order could not be fulfilled - "+item+"'></div>";
		html += "<label for='des'>Content:</label><div class='input-group'><textarea name='des' id='des' class='form-control' rows='4' cols='70'>Unfortunately we cannot fulfil your order from "
				+ site + "</textarea></div>";
		html += "<input type='hidden' name='from' value='"+from+"'>";
		html += "<input type='hidden' name='v' value='"+id+"'>";
		html += "</form>";
		bootbox
				.dialog({
					message : html,
					buttons : {
						success : {
							label : "Send Message",
							type : "submit",
							className : "btn-success",
							callback : function() {

								jsRoutes.controllers.Account
										.sendRefutation()
										.ajax(
												{
													data : $("#deny-form")
															.serialize(),
													success : function(data) {

														var el = "v-" + id;
														var i = document
																.getElementById(el);
														i.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';

														el = "revoke-" + id;
														var i = document
																.getElementById(el);
														i.style.visibility = "hidden";
														el = "verify-" + id;
														var i = document
																.getElementById(el);
														i.style.visibility = "hidden";

													},
													error : function(err) {
														alert("error");

													}
												});
							}
						}
					}
				});

	}