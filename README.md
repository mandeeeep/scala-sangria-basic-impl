# scala-sangria-basic-impl
Basic sangria implementation

---------


{
	"variables":{
		"a":true
	},
	"operationNames":["Some","Somen"],
	"query":" query Some($a: Boolean!){foos_check(y: $a){y @export(as: \"vr\")}} query Somen{bars_check(x:$vr){x}} "
}
