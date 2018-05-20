package admin;

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
import md5.MD5;

public class ChpwdServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		HttpSession session = request.getSession(true);
		Object adminID = session.getAttribute("username");
		System.out.println("修改密码："+oldPassword+newPassword);
		JSONObject json = null;
		json = new JSONObject();
		String chpwd_result = "false";
		if (oldPassword == null || oldPassword.equals("") || newPassword == null || newPassword.equals("")) {
			chpwd_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";
				oldPassword = MD5.parseStrToMd5L32(oldPassword);

				sql = "select pwdhash from admin" + " where adminID = ?";
				ResultSet rs = dd.query(sql, adminID);
				if (rs.next()) {
					if (oldPassword.equals(rs.getString("pwdhash"))) { // 如果旧密码验证成功
						newPassword = MD5.parseStrToMd5L32(newPassword); // 获得加密后的密码
						sql = "update admin set pwdhash=? where adminID=?";
						dd.modify(sql, newPassword, adminID);
						chpwd_result = "success";
					} else
						chpwd_result = "oldpwderror";

				} else {
					chpwd_result = "false";
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
			json.put("chpwd_result", chpwd_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
