var currentHall = localStorage["currentHall"];
var currentScreening = localStorage["currentScreening"];
var currentMovie = localStorage["currentMovie"];



$(function() {
	 redirectUnauthorized();
	 
	//LOGNETE se predi da go probvate, oste nqma non login stuff
	requestAllSeats();

});

function getNextTicketID(){
	$.ajax({
        type: "GET",
        url: 'rest/ticket/max',
        dataType: "text",
        success: function(data) {
            return data;
        },
        error: function() {
            alert('Error occured');
        }
    });
}

function requestAllSeats() {
	var bookedSeats = [];
	var bookedSeatsIds = []
	 $.ajax({
		url : "rest/ticket/byScreeningId/" + currentScreening,
		type : "GET",
		dataType : "json",
		statusCode : {
			200 : function(tickets) {
				console.log(tickets);
				for (i = 0; i < tickets.length; i++) {
					
					console.log(tickets[i].seatNumber);
					bookedSeats.push(tickets[i].seatNumber);
					bookedSeatsIds.push(tickets[i].id);
					
					//console.log(bookedSeats);
				}

				var settings = {
					rows : 12,
					cols : 20,
					rowCssPrefix : 'row-',
					colCssPrefix : 'col-',
					seatWidth : ($($('#place').parent()[0]).width()-$($('#place').parent()[0]).width()/20) / 20,
					seatHeight :  ($($('#place').parent()[0]).height()-$($('#place').parent()[0]).height()/12) / 12,
					seatCss : 'seat',
					selectedSeatCss : 'selectedSeat',
					selectingSeatCss : 'selectingSeat'
				};

				console.log("currenly-here");
				console.log($($('#place').parent()[0]).width());
				console.log($($('#place').parent()[0]).height());
				console.log("currenly-here");
				
				var init = function(reservedSeat) {
					var str = [], seatNo, className,k = 1;
					for (i = 0; i < settings.rows; i++) {
						for (j = 0; j < settings.cols; j++) {
							if (j == 0 || j == 1) {
								continue;
							}
							if (i == settings.rows / 2) {

								continue;
							}
							seatNo = (i + j * settings.rows + 1);
							className = settings.seatCss + ' '
									+ settings.rowCssPrefix + i.toString()
									+ ' ' + settings.colCssPrefix
									+ j.toString();
							if ($.isArray(reservedSeat)
									&& $.inArray(seatNo, reservedSeat) != -1) {
								className += ' ' + settings.selectedSeatCss;
							}
							str.push('<li class="' + className + '"'
									+ 'style="top:'
									+ (i * settings.seatHeight).toString()
									+ 'px;left:'
									+ (j * settings.seatWidth).toString()
									+ 'px">' + '<a title="' + seatNo + '">'
									+ seatNo + '</a>' + '</li>');
						}
					}
					$('#place').html(str.join(''));
				};
				init(bookedSeats);

				$('.' + settings.seatCss).click(function() {
					console.log(this);
					if ($(this).hasClass(settings.selectedSeatCss)) {
						alert('This seat is already reserved');
					} else {
						$(this).toggleClass(settings.selectingSeatCss);
					}
				});

//				$('#btnShow').click(
//						function() {
//							var str = [];
//							$.each($('#place li.' + settings.selectedSeatCss
//									+ ' a, #place li.'
//									+ settings.selectingSeatCss + ' a'),
//									function(index, value) {
//										str.push($(this).attr('title'));
//									});
//							alert(str.join(','));
//						})
//
//				$('#btnShowNew').click(
//						function() {
//							var str = [], item;
//							$.each($('#place li.' + settings.selectingSeatCss
//									+ ' a'), function(index, value) {
//								item = $(this).attr('title');
//								str.push(item);
//							});
//						})
						
				
				$('#btnBuyTickets').click(
							            	    
						function() {
							var str = [], item, item2;
							$.each($('#place li.' + settings.selectingSeatCss
									+ ' a'), function(index, value) {
								item = $(this).attr('title');
								
								str.push(item);
								buyTicket(item);
							});
							
							$.each($('#place li.' + settings.selectingSeatCss
								), function(index, value) {
								$(this).toggleClass(settings.selectingSeatCss);
								$(this).toggleClass(settings.selectedSeatCss);
								//console.log(this);
							});
							//console.log(str);
							//console.log(bookedSeatsIds);
							if(undefined != item){
								alert("Buy tickets successful!");
							}
							else {
								alert("No seats are currently selected.");
							}
						});	
				
				$('#btnAddReservations').click(
	            	    
						function() {
							var item;
							$.each($('#place li.' + settings.selectingSeatCss
									+ ' a'), function(index, value) {
								item = $(this).attr('title');
								reserveTicket(item);
							});
							
							$.each($('#place li.' + settings.selectingSeatCss
								), function(index, value) {
								$(this).toggleClass(settings.selectingSeatCss);
								$(this).toggleClass(settings.selectedSeatCss);
								console.log(this);
							});		

							console.log(item);
							if(undefined != item){
								alert("Success! Please confirm your reservation within 10 minutes or it will be deleted!");
							}
							else {
								alert("No seats are currently selected.");
							}
						});	
				

				$('#btnConfirm').click(
						function() {
							$.ajax({
							 	url : 'rest/ticket/confirmReservation',
							    type: "POST",
							    contentType: "application/json;charset=UTF-8",
							    statusCode: {
									304 : function(data) {
										alert("Nothing to confirm.");
									},
									200 : function(data){
										alert("You've successfully cofirmed your pending reservations.");
									}
								}
							})
							
								
						
							.fail(function(data) {
							   alert("Something went wrong.");

							})		
						});
				

				$('#btnCancel').click(
						function() {
							$.ajax({
							 	url : 'rest/ticket/cancelMyReservations',
							    type: "DELETE",
							    contentType: "application/json;charset=UTF-8",
							    statusCode: {
									304 : function(data) {
										alert("Nothing to cancel.");
									},
									200 : function(data){
																		
											var tempo = [];
											for(var i = 0; i < data.ticket.length; i++){
												tempo.push(data.ticket[i].seatNumber);
											}
														
										$.each($('#place li.' + settings.selectedSeatCss
												+ ' a'), function(index, value) {
											item = $(this).attr('title');
											console.log(tempo);
											console.log(item);
											console.log(tempo.indexOf(item));
											var that = this;
											for(var i = 0; i < tempo.length; i ++){
												if(tempo[i] == item){
													console.log("heyyoyoyo");
													$($(that).parent()[0]).toggleClass(settings.selectedSeatCss);
												}
											}	
											
										});
										if(tempo.length === 0){
											alert("You have no pending reservations. Nothing to cancel.")
										}
										else {
										alert("You've successfully canceled all your pending reservations.");
										}
									}
								}
							})
					});
			}
		}
	});
	
	function reserveTicket(number){
		var next = getNextTicketID();
		var tId = parseInt(next);
		 var data = {
				 	"id" : tId,
	    	        "screening": {
	    	            "id": currentScreening
	    	        },
	    	        "user": {
	    	            "username": "admin",
	    	            "password": "admin"
	    	        },
	    	        "seatNumber": number,
	    	        "status": "Available"
		    	};
		 console.log(JSON.stringify(data));
		 $.ajax({
			 	url : 'rest/ticket/addAndReserve',
			    type: "POST",
			    contentType: "application/json;charset=UTF-8",
			    async: false,
			    data: JSON.stringify(data)
			})
			.fail(function(data) {
				
			   alert("Something went wrong");
			})
		 
		 
	}
	
	function buyTicket(number) {
		var next = getNextTicketID();
		var tId = parseInt(next);
	    var data = {
	    		"id" : tId,
    	        "screening": {
    	            "id": currentScreening
    	        },
    	        "user": {
    	            "username": "admin",
    	            "password": "admin"
    	        },
    	        "seatNumber": number,
    	        "status": "Available"
	    	}
	   console.log(JSON.stringify(data));
	    $.ajax({
		 	url : 'rest/ticket/addAndBuy',
		    type: "POST",
		    contentType: "application/json;charset=UTF-8",
		    async: false,
		    data: JSON.stringify(data),
		
			success : function(){
			},
		
			fail : (function(data) {
				alert("Something went wrong");
			})
	    });
	    
//	    $.ajax({
//		 	url : 'rest/ticket/test/5',
//		    type: "GET",
//		    contentType: "application/json;charset=UTF-8",
//		    async: false,
//		  //  data: JSON.stringify(data)
//		    statusCode: {
//				200 : function(screenings) {
//					console.log("helllo");
//					console.log(screenings);
//				}
//			}
//		});
		   
//		$.ajax({
//		 	url : 'rest/ticket/buyTicket?ticketId=' + ticketId,
//		    type: "PUT",
//		    contentType: "application/json;charset=UTF-8",
//		    async: false
//		   // data: JSON.stringify(data)
//		})
//		.success(function(data) {
//		    console.log(data);
//		})
//		.fail(function(data) {
//		   alert("Something went wrong");
//
//		})
    }

}