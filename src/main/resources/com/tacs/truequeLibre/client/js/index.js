
	function getMyTrueques(){
		$.get("truequeLibre/trueques", function( data ) {
		  $( "#mainTitle" ).html( "Mis Trueques" );
		  $( "#mainData" ).load( "trueques.html" );
		});
	}

