
$(document).ready(function(){
	addInputListener();
});

//文档就绪事件,在文档加载完成后发送一次请求验证是否已登录

$(document).ready(function() {	
	$("#loginNav_login").hide();
	$("#loginNav_unlogin").show();	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../adminisloginservlet",
		data :{req_log : "islogin"},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {
			//alert("message:"+data);	
			if (data.log_result == "false") {
				$("#loginNav_login").hide();
				$("#loginNav_unlogin").show();
			} else {
				$("#loginNav_unlogin").hide();
				$("#loginNav_login").show();
				//alert(data.username);
				$("#username").text(data.username);
			}
									
		}										
		
	}); //$.ajax结束

});
 

//处理注销点击事件
$(document).ready(function() {
	$("#logout").click(function() {
		//alert("点击了注销");
		$.ajax({
			dataType : "json", //数据类型为json格式
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			type : "POST",
			url : "../adminisloginservlet",
			data :{req_log : "logout"},
			statusCode : {
				404 : function() {
					alert('page not found');
				}
			},
			success : function(data, status) {	
				if (data.log_result == "true") {
					$("#loginNav_login").hide();
					$("#loginNav_unlogin").show();					
				}
										
			}										
			
		}); //$.ajax结束
		
	});
});

function addInputListener() {
	// 输入框获得焦点和失去焦点事件
	$("input").focus(function() {
		$(this).css("background-color", "#ddd");
		$(this).css({
			"border" : "1px solid green"
		});
	});

	$("input").blur(function() {
		$(this).css("background-color", "#fff");

	});

}

//导航栏按钮--切换右边的content

$(document).ready(function(){
	$(".admin_bus_nav").click(function(){
		$(".admin-content").children().hide();
		$(".bus-content").show();		
	});
	$("#add_bus_nav").click(function(){
		$(".admin-content").children().hide();
		$(".addbus-content").show();		
	});
	
	$(".admin_route_nav").click(function(){
		$(".admin-content").children().hide();
		$(".route-content").show();		
	});
	
	$("#add_route_nav").click(function(){
		$(".admin-content").children().hide();
		$(".addroute-content").show();		
	});
	
	$("#add_RT_nav").click(function(){
		$(".admin-content").children().hide();
		$(".addRT-content").show();		
	});
	
	$(".admin_ticket_nav").click(function(){
		$(".admin-content").children().hide();
		$(".ticket-content").show();		
	});
	$("#chpwd_nav").click(function(){
		$(".admin-content").children().hide();
		$(".adminInfo").show();

	});

});


//文档就绪事件,在文档加载完成后设置出发日期为当前日期值
$(document).ready(function() {
	// 开始写 jQuery 代码...	
	initDate();
});

function initDate()
{
	var d=new Date();
	var day=d.getDate();
	if(day<10){
		day="0"+day;
	}
	var month=d.getMonth();
	if(month<10){
		month="0"+(month+1);
	}
	var year=d.getFullYear();
	var date=year+"-"+month+"-"+day;
	$('input[type="date"]').val(date);
	$('#departureDateID').attr("min",date);//设置最小日期，最早出发时间
}














