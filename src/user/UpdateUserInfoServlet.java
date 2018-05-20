package user;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class UpdateUserInfoServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		HttpSession session = request.getSession(true);
		Object phoneNumber = session.getAttribute("username");
		JSONObject json = null;
		json = new JSONObject();
		String chinfo_result = "false";
		if (phoneNumber == null || phoneNumber.equals("")) {
			chinfo_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String name= request.getParameter("name");
				String nickname= request.getParameter("nickname");
				String sex= request.getParameter("sex");
				String ID= request.getParameter("ID");
				String emailAddress= request.getParameter("emailAddress");
				String isStudent= request.getParameter("isStudent");
				//System.out.println("sex:"+sex+isStudent); 做一个检查，是否为""
				if(name==null||name.equals(""))
					name=null;
				if(nickname==null||nickname.equals(""))
					nickname=null;
				if(sex==null||sex.equals(""))
					sex=null;
				if(ID==null||ID.equals(""))
					ID=null;
				if(emailAddress==null||emailAddress.equals(""))
					emailAddress=null;
				if(isStudent==null||isStudent.equals(""))
					isStudent=null;
				
				String sql = "";
				sql = "update passengerinfo"
						+" set name=?,nickname=?,sex=?,ID=?,emailAddress=?,isStudent=?"
						+" where phoneNumber=?";
				dd.modify(sql,name,nickname,sex,ID,emailAddress,isStudent,phoneNumber); //修改密码
				chinfo_result = "true";						

			} catch (Exception e) {
				chinfo_result = "false";
				e.printStackTrace();
			} finally {
				try {
					dd.closeConn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		try {
			json.put("chinfo_result", chinfo_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
