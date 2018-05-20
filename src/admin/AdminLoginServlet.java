package admin;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.*;

import md5.*;
import DbDao.DbDao;;
public class AdminLoginServlet extends HttpServlet
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
		String username = request.getParameter("username");
		String pass = request.getParameter("password");
		
		System.out.println("管理员账号和密码："+username+pass);
				
		if(username==null||pass==null||username.equals("")||pass.equals("")){
			flag="false";			
		}
		//验证用户名是否存在和密码是否正确
		else {
			try{
				DbDao dd = new DbDao();	
				// 查询结果集
				ResultSet rs = dd.query("select pwdhash from admin"
						+ " where adminID = ?", username);
				if (rs.next()){
					pass=MD5.parseStrToMd5L32(pass); //获得加密后的密码
					//System.out.println(pass);
					if (rs.getString("pwdhash").equals(pass)){
						// 获取session对象
						HttpSession session = request.getSession(true);
						// 设置session属性，跟踪用户会话状态
						session.setAttribute("username" , username);
						flag="true";
					}
					else flag="false"; //密码错误
				}
				else flag="nouser";//用户名不存在
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
