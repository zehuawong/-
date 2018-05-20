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


public class GetUserInfoServlet  extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		HttpSession session = request.getSession(true);
		Object phoneNumber = session.getAttribute("username");
		System.out.println("用户尝试修改资料："+phoneNumber);
		JSONObject json = null;
		json = new JSONObject();
		String userinfo_result = "false";
		if (phoneNumber==null||phoneNumber.equals("")) {
			userinfo_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";
				sql = "select name,nickname,sex,ID,emailAddress,isStudent from passengerinfo"
						+" where phoneNumber=?";
				
				ResultSet rs = dd.query(sql, phoneNumber);
				if (rs.next()) {
					json.put("name",rs.getString("name"));
					json.put("nickname",rs.getString("nickname"));
					json.put("sex",rs.getString("sex"));
					json.put("ID",rs.getString("ID"));
					json.put("emailAddress",rs.getString("emailAddress"));
					json.put("isStudent",rs.getString("isStudent"));
					
					userinfo_result = "true";

				} else {
					userinfo_result = "false";
				}

			} catch (Exception e) {
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
			json.put("userinfo_result", userinfo_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}