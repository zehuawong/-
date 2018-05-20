package user;

 
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.*;

import md5.*;
import DbDao.DbDao;;


public class  RegisterServlet extends HttpServlet
{
	// 响应客户端请求的方法
	public void service(HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException,java.io.IOException
	{
		//request.setCharacterEncoding("UTF-8");		
		response.setContentType("text/html;charset=utf-8"); 
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter() ; //输入流，对前台response
		 
		String flag="false";
		
		// 获取请求参数
		String phoneNumber = request.getParameter("phoneNumber");
		String pass = request.getParameter("password");
				
		System.out.println("用户名和密码："+phoneNumber+pass);
				
		if( phoneNumber==null||pass==null||phoneNumber.equals("")||pass.equals("")){
			response.sendRedirect("register.jsp");
			return;
		}
		
		else {
			try{
				DbDao dd = new DbDao();
					// 查询结果集
				ResultSet rs = dd.query("select pwdhash from passengerInfo"
						+ " where phoneNumber = ?", phoneNumber);
				if (rs.next()){
						flag="false"; //用户名(手机号已存在)								
				}
				//用户名不存在时
				else{
					pass=MD5.parseStrToMd5L32(pass); //获得加密后的密码
					String sql="insert into PassengerInfo(phoneNumber,pwdhash) values(?,?)";//防止sql注入攻击				
					
					if(dd.insert(sql,phoneNumber,pass)){ //如果插入成功
						//response.sendRedirect("reg_success.html");
						flag="true";
					}
					else flag="false"; //如果插入失败
				}
				dd.closeConn(); //关闭数据库连接
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		out.write(flag);		
		out.flush();
		out.close();
		
	}
}
