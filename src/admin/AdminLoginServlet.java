package admin;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.*;

import md5.*;
import DbDao.DbDao;;
public class AdminLoginServlet extends HttpServlet
{
	// ��Ӧ�ͻ�������ķ���
	public void service(HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException,java.io.IOException
	{
		//request.setCharacterEncoding("UTF-8");
		
		response.setContentType("text/html;charset=utf-8"); 
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter() ; //����������ǰ̨response
		 
		String flag="false";
		
		// ��ȡ�������
		String username = request.getParameter("username");
		String pass = request.getParameter("password");
		
		System.out.println("����Ա�˺ź����룺"+username+pass);
				
		if(username==null||pass==null||username.equals("")||pass.equals("")){
			flag="false";			
		}
		//��֤�û����Ƿ���ں������Ƿ���ȷ
		else {
			try{
				DbDao dd = new DbDao();	
				// ��ѯ�����
				ResultSet rs = dd.query("select pwdhash from admin"
						+ " where adminID = ?", username);
				if (rs.next()){
					pass=MD5.parseStrToMd5L32(pass); //��ü��ܺ������
					//System.out.println(pass);
					if (rs.getString("pwdhash").equals(pass)){
						// ��ȡsession����
						HttpSession session = request.getSession(true);
						// ����session���ԣ������û��Ự״̬
						session.setAttribute("username" , username);
						flag="true";
					}
					else flag="false"; //�������
				}
				else flag="nouser";//�û���������
				dd.closeConn(); //�ر����ݿ�����
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		out.write(flag);		
		out.flush();
		out.close();
		
	}
}
