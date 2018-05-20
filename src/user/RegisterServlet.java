package user;

 
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.*;

import md5.*;
import DbDao.DbDao;;


public class  RegisterServlet extends HttpServlet
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
		String phoneNumber = request.getParameter("phoneNumber");
		String pass = request.getParameter("password");
				
		System.out.println("�û��������룺"+phoneNumber+pass);
				
		if( phoneNumber==null||pass==null||phoneNumber.equals("")||pass.equals("")){
			response.sendRedirect("register.jsp");
			return;
		}
		
		else {
			try{
				DbDao dd = new DbDao();
					// ��ѯ�����
				ResultSet rs = dd.query("select pwdhash from passengerInfo"
						+ " where phoneNumber = ?", phoneNumber);
				if (rs.next()){
						flag="false"; //�û���(�ֻ����Ѵ���)								
				}
				//�û���������ʱ
				else{
					pass=MD5.parseStrToMd5L32(pass); //��ü��ܺ������
					String sql="insert into PassengerInfo(phoneNumber,pwdhash) values(?,?)";//��ֹsqlע�빥��				
					
					if(dd.insert(sql,phoneNumber,pass)){ //�������ɹ�
						//response.sendRedirect("reg_success.html");
						flag="true";
					}
					else flag="false"; //�������ʧ��
				}
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
