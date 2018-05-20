//管理员管理班次信息
//全局变量：
var RTsum=0;
var RTtotalPages=0;
var RTPerPage=8; //每页显示8条记录
var RTArray; //班次查询结果数组
var ddate; //出发日期


function validateRTForm(){
	
	var startcity=$("#adminRT_sc_ID").val();
	var endcity=$("#adminRT_ec_ID").val();
	var departuredate=$("#adminRT_ddate_ID").val();
	
	var flag=true;
	if(startcity==null||startcity==""){
		$("#adminRT_sc_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(endcity==null||endcity==""){
		$("#adminRT_ec_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	
	if(flag==false){
		return false;
	}
	//alert(startcity);
	
	ddate=departuredate;
	//设置表格标题
	$("#adminRT_table_caption_ID").html(startcity+"-->"+endcity+"&nbsp;"+departuredate+"&nbsp;&nbsp;班次信息详情");
	
	selectRT(startcity,endcity,departuredate);
	
	return false;
	

}




//处理管理员点击的编辑按钮
function editRT_btn_listener()
{
	$(".editRT_btn").click(function() {
		var RTID=$(this).val();
		var tr=$(this).parent().parent();
		
		var td;var tdval;
		td=tr.children("td").get(3); 
		tdval=td.innerHTML;
		td.innerHTML="<input style='width:80px' value='"+tdval+"' />";		
		td=tr.children("td").get(4); 
		tdval=td.innerHTML;
		td.innerHTML="<input type='time' style='width: 95px' value='"+tdval+"' />";
		td=tr.children("td").get(5); 
		tdval=td.innerHTML;
		td.innerHTML="<input type='time' style='width: 95px' value='"+tdval+"' />";		
		for(var i=6;i<=8;i++){
			td=tr.children("td").get(i); 
			tdval=td.innerHTML;
			td.innerHTML="<input type='number' min='0' style='width: 60px' value='"+tdval+"' />";
		}
		
		addInputListener();
		toggleToSaveRT(tr,RTID);
		//alert("切换结束");
		
	});
}

//处理管理员点击的保存按钮
function saveRT_btn_listener()
{
	$(".saveRT_btn").click(function() {	
		// alert("点击了保存按钮");
		var tr=$(this).parent().parent();
		var RTID=$(this).val();
		var flag=true;
		var RTInfo=new Array(8); //数组
		for(var i=3;i<=8;i++){
			var tdinput=tr.children("td").children().get(i-3);
			RTInfo[i-3]=tdinput.value;
			if(RTInfo[i-3]==null||RTInfo[i-3]==""){		//验证表单，是否为空
				tdinput.style.border = "2px solid #ff0000";
				flag = false;
				
			}
		}		
		for(var i=3;i<=5;i++){
			RTInfo[i]=parseInt(RTInfo[i]);//转换为数字比较
			if(RTInfo[i]<0){
				tr.children("td").children().get(i).style.border = "2px solid #ff0000";	
				flag=false;
			}
			
		}
 	
		if(RTInfo[4]>RTInfo[3]) 
		{
			tr.children("td").children().get(4).style.border = "2px solid #ff0000";	
			flag=false;
		}
		
		if(flag==false){
			return false;
		}
		RTInfo[6]=RTID;
		RTInfo[7]=ddate;	
		// $.ajax();
		UpdateRT(tr,RTInfo); //更新班次信息
		
		
	});
}




// 点击编辑按钮之后切换到了保存按钮 ,


function toggleToSaveRT(tr,RTID){
	tr.children("td").get(9).innerHTML="<button class='saveRT_btn save_btn' value='"+RTID+"'>保存</button>";	
	// alert("切换到了toggle函数");
	saveRT_btn_listener();
	
}

// 点击了保存按钮之后切换到编辑按钮
function toggleToEditRT(tr,RTID){
	tr.children("td").get(9).innerHTML="<button class='editRT_btn edit_btn' value='"+RTID+"'>编辑</button>";	
	for(var i=3;i<=8;i++){
		tr.children("td").get(i).innerHTML=tr.children("td").children().get(0).value;  
	}
	
	editRT_btn_listener();

}

//查询班次
function selectRT(startcity,endcity,departuredate)
{
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../selectRTservlet",
		data :{startcity:startcity,endcity:endcity,departuredate:departuredate},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {				
			if (data.selectRT_result == "true") { //查询成功			
				 //生成表格	 
				RTArray=data.RT;
				RTsum=data.RTsum;				
				RTtotalPages=RTsum%RTPerPage==0 ? RTsum/RTPerPage : RTsum/RTPerPage+1;				
				RTtotalPages=parseInt(RTtotalPages);								
				
				add_RTPage_btn_listener(); //分页按钮监听	

			}else {
				$("#adminRT_table_tbody_ID").html("<tr><td colspan='9'>无此线路班次信息</td></tr>");
				
				RTsum=0;
				RTtotalPages=0;	
				add_RTPage_btn_listener(); //分页按钮

			}
									
		}										
		
	}); //$.ajax结束
	
	
}

function showRTPageOf(num)
{
	if(num>=RTtotalPages)
		return;
	var startIndex=num*RTPerPage;	
	//计算每一页实际要显示的记录条数，防止记录不足RPerPage，
	var numPerPage=0; //每一页实际要显示的记录条数
	if(startIndex+RTPerPage>RTsum){
		numPerPage=RTsum-startIndex;		
	}else numPerPage=RTPerPage;
	
	//alert("开始索引："+startIndex);
	RT_html="";
	for(var i=startIndex;i<startIndex+numPerPage;i++){	
		RT_html+="<tr>"+"<td>"+RTArray[i].RTID+"</td>"
				+"<td>"+RTArray[i].s+"</td>"				
				+"<td>"+RTArray[i].t+"</td>"
				+"<td>"+RTArray[i].busnum+"</td>"
				+"<td>"+RTArray[i].dtime+"</td>"
				+"<td>"+RTArray[i].dur+"</td>"
				+"<td>"+RTArray[i].ssum+"</td>"
				+"<td>"+RTArray[i].sleft+"</td>"
				+"<td>"+RTArray[i].price+"</td>";		
		RT_html+="<td><button class='editRT_btn edit_btn' value='"+RTArray[i].RTID+"'>编辑</button></td>";
		RT_html+="</tr>";	
		
	}
	
	//alert(ticket_html);
	$("#adminRT_table_tbody_ID").html(RT_html);	
	
	editRT_btn_listener();
	
	
	
}


//分页按钮添加点击事件监听
function add_RTPage_btn_listener()
{
	//alert("分页RT");
	var setTotalCount =RTsum;
	$('#RTpages_box').paging({
		initPageNo : 1, // 初始页码
		totalPages : RTtotalPages, //总页数
		totalCount : '合计' + setTotalCount + '条数据', // 条目总数
		slideSpeed : 600, // 缓动速度。单位毫秒
		jump : true, //是否支持跳转
		callback : function(page) { // 回调函数
			console.log(page);
			//alert("当前页："+page);
			showRTPageOf(page-1);
			
		}
	})
}


//保存更新班次信息
function UpdateRT(tr,RTInfo){
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../updateRTservlet",
		data :{busnum:RTInfo[0],dtime:RTInfo[1],dur:RTInfo[2],ssum:RTInfo[3],
			sleft:RTInfo[4],price:RTInfo[5],RTID:RTInfo[6],ddate:RTInfo[7]},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.updateRT_result == "true") { //添加成功			
				 toggleToEditRT(tr,RTInfo[6]);
				  				
			}else {
				var txt="修改失败，请检查班次是否重复，或者车牌号信息有误";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}
									
		}										
		
	}); //$.ajax结束
	

	
}


