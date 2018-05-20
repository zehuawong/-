//全局变量
var ordersum=0; 
var orderArray=null;
var ordertotalPages;//总页数
var ordersPerPage=8;//每一页显示的订单数目

function validateorderForm()
{
	var reqkey=$("#searchorder_key_ID").val();
	var reqvalue=$("#searchorder_value_ID").val();
	var flag=true;
	if(reqkey==null||reqkey==""){
		$("#searchorder_key_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(reqvalue==null||reqvalue==""){
		$("#searchorder_value_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(flag==false){
		return false;
	}
	
	getOrder(reqkey,reqvalue);
	 
	return false;

}






//获取订单数
function getOrder(reqkey,reqvalue)
{

	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../adminhisorderqueryservlet",
		data : {reqkey:reqkey,reqvalue:reqvalue},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if(data.selectorder_status=="true"){
				//alert("message:"+"查询历史订单成功!");
				ordersum=data.ordersum;
				orderArray=data.order;	
				ordertotalPages=ordersum%ordersPerPage==0 ? ordersum/ordersPerPage : ordersum/ordersPerPage+1;
				ordertotalPages=parseInt(ordertotalPages);										
				add_orderPage_btn_listener();//给分页链接按钮组添加点击事件监听							
				
			}						
			else if(data.selectorder_status=="false")					
				window.wxc.xcConfirm("参数错误！", window.wxc.xcConfirm.typeEnum.error);							
			else if(data.selectorder_status=="no_hisOrder"){ //还没有历史订单
				ordersum=0;
				ordertotalPages=0;				
				var noorder_txt="没有历史订单!";				 
				var order_html="<tr><td colspan='8'>"+noorder_txt+"</td></tr>";
				$("#hisorder_table_tbody_ID").html(order_html); //显示订单列表								 
				add_orderPage_btn_listener(); ///给分页链接按钮组添加点击事件监听	
			}
		}
	});//$.ajax结束
	
}


///分页按钮添加点击事件监听
function add_orderPage_btn_listener()
{
	var setTotalCount =ordersum;
	$('#orderpages_box').paging({
		initPageNo : 1, // 初始页码
		totalPages : ordertotalPages, //总页数
		totalCount : '合计' + setTotalCount + '条订单', // 条目总数
		slideSpeed : 600, // 缓动速度。单位毫秒
		jump : true, //是否支持跳转
		callback : function(page) { // 回调函数
			console.log(page);
			//alert("当前页："+page);
			showOrder_PageOf(page-1);
		}
	})
}


function showOrder_PageOf(num)
{
	if(num>=ordersum)
		return;
	var startIndex=num*ordersPerPage;
	var numPerPage=0; //每一页实际要显示的记录条数
	if(startIndex+ordersPerPage>ordersum){
		numPerPage=ordersum-startIndex;
	}else numPerPage=ordersPerPage;
	var order_html=""; var orderdateTime="";
	for(var i=startIndex;i<startIndex+numPerPage;i++){
		order_html+="<tr>"+"<td>"+orderArray[i].ticketID+"</td>"
					+"<td>"+orderArray[i].departDateTime+"</td>"
					+"<td>"+orderArray[i].startPoint+"</td>"
					+"<td>"+orderArray[i].destination+"</td>"
					+"<td>"+orderArray[i].price+"</td>";
		orderdateTime=orderArray[i].orderdateTime.substring(0,orderArray[i].orderdateTime.indexOf('.')); //去掉.0		
		order_html+="<td>"+orderdateTime+"</td>";
		if(orderArray[i].status=="O"){
			order_html+="<td style='color: #1ab394;'>已乘车</td>";
		}
		else if(orderArray[i].status=="Y"){
			order_html+="<td style='color:blue;'>已确认</td>";
		}
		 
		order_html+="</tr>";				
	}
	
	$("#hisorder_table_tbody_ID").html(order_html); //显示订单列表
	

}








