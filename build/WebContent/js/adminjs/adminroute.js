//管理员管理班次信息
//全局变量：
var Rsum=0;
var RtotalPages=0;
var RPerPage=8; //每页显示8条记录
var RArray; //线路查询结果数组



$(document).ready(function(){
	$("#admin_route_btn_ID").click(function(){		
		$(".admin-RT-content").hide();
		$(".admin-route-content").show();
	});
	$("#admin_RT_btn_ID").click(function(){		
		$(".admin-route-content").hide();
		$(".admin-RT-content").show();
		
	});
	
});

 
function validateRForm()
{
	var startcity=$("#route_sc_ID").val();
	var endcity=$("#route_ec_ID").val();
	var flag=true;
	if(startcity==null||startcity==""){
		$("#route_sc_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(endcity==null||endcity==""){
		$("#route_ec_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(flag==false){
		return false;
	}
	//设置表格标题
	$("#route_table_caption_ID").html(startcity+"-->"+endcity+"&nbsp;&nbsp;线路信息详情");
	
	selectR(startcity,endcity);
	
	return false;
	
}


//开通线路按钮
function startR_btn_listener()
{
	$(".startR_btn").click(function() {
		var routeID=$(this).val();		
		//发送开通指令
		$.ajax({
			dataType : "json", //数据类型为json格式
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "POST",
			url : "../startrouteservlet",
			data :{routeID:routeID},
			statusCode : {
				404 : function() {
					alert('page not found');
				}
			},
			success : function(data, status) {	
				if (data.startR_result == "true") { //删除成功		
					//刷新一遍结果表格		
					validateRForm();				
				}else {
					var txt="开通线路失败，请检查";
					window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
				}
										
			}										
			
		}); //$.ajax结束	
		
		 
		
	});
}
 
 
//停运线路信息按钮
function stopR_btn_listener()
{
	$(".stopR_btn").click(function(){
		var tr=$(this).parent().parent();
		var routeID=tr.children("td").children("button").get(0).value;
		var txt=  "您确定停运该线路吗？";
		var option = {
			title: "信息",
			btn: parseInt("0011",2),
			onOk: function(){
				console.log("确认停运线路啦");				
				if(routeID==null||routeID=="")//未提交到服务器保存
				{
					alert("routeID为空")
					return;
				}
	
				else {
					stopR(routeID);
				}
					
			}
		}
		window.wxc.xcConfirm(txt, "custom", option);
	});
	
}


//发送给服务器请求停运线路
function stopR(routeID){	
	//获得线路编号
	console.log("停运routeID"+routeID);
	if(routeID==null||routeID=="")//未提交到服务器
	{
		alert("routeID为空")
		return;
	}
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../stoprouteservlet",
		data :{routeID:routeID},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.stopR_result == "true") { //停运成功		
				//刷新一遍结果表格
				validateRForm();				
			}else {
				var txt="停运该线路失败";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}
									
		}										
		
	}); //$.ajax结束	
	
	
}



//查询线路
function selectR(startcity,endcity)
{
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../selectrouteservlet",
		data :{startcity:startcity,endcity:endcity},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {				
			if (data.selectR_result == "true") { //查询成功			
				 //生成表格	 
				RArray=data.route;
				Rsum=data.routesum;				
				RtotalPages=Rsum%RPerPage==0 ? Rsum/RPerPage : Rsum/RPerPage+1;												
				RtotalPages=parseInt(RtotalPages);	
				add_RPage_btn_listener();	
				
			}else {
				$("#route_table_tbody_ID").html("<tr><td colspan='5'>该线路暂未开通</td></tr>");
				Rsum=0;
				RtotalPages=0;	
				add_RPage_btn_listener();

			}
									
		}										
		
	}); //$.ajax结束
	
		
}

function showRPageOf(num)
{
	if(num>=RtotalPages)
		return;
	var startIndex=num*RPerPage;	
	//计算每一页实际要显示的记录条数，防止记录不足RPerPage，
	var numPerPage=0; //每一页实际要显示的记录条数
	if(startIndex+RPerPage>Rsum){
		numPerPage=Rsum-startIndex;		
	}else numPerPage=RPerPage;
	
	//alert("开始索引："+startIndex);
	R_html="";
	for(var i=startIndex;i<startIndex+numPerPage;i++){	
		R_html+="<tr>"+"<td>"+RArray[i].routeID+"</td>"
				+"<td>"+RArray[i].s+"</td>"
				+"<td>"+RArray[i].t+"</td>";
		
		if(RArray[i].status=='Y'){
			R_html+="<td>运营中</td>";
			R_html+="<td><button class='stopR_btn delete_btn' value='"+RArray[i].routeID+"'>停运</button></td>";
		}
		else{
			R_html+="<td>已停运</td>";
			R_html+="<td><button class='startR_btn edit_btn' value='"+RArray[i].routeID+"'>开通</button></td>";
		}
		
		
		R_html+="</tr>";
		
	}
	
	//alert(ticket_html);
	$("#route_table_tbody_ID").html(R_html);	
	
	stopR_btn_listener();
	startR_btn_listener();
	
}



//分页按钮添加点击事件监听
function add_RPage_btn_listener()
{
	var setTotalCount =Rsum;
	$('#Rpages_box').paging({
		initPageNo : 1, // 初始页码
		totalPages : RtotalPages, //总页数
		totalCount : '合计' + setTotalCount + '条数据', // 条目总数
		slideSpeed : 600, // 缓动速度。单位毫秒
		jump : true, //是否支持跳转
		callback : function(page) { // 回调函数
			console.log(page);
			//alert("当前页："+page);
			showRPageOf(page-1);
			
		}
	})
}




