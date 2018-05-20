package admin.route;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class StopRouteServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // ��ǰ̨response
		JSONObject json = null;
		json = new JSONObject();
		String routeID = request.getParameter("routeID");

		String stopR_result = "false";
		if (routeID == null || routeID.equals("")) {
			stopR_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";
				sql = "update route set status='N' where routeID=?"; //ɾ��һ����·������״̬ΪNʧЧ

				if (dd.update(sql,routeID) == true)
					stopR_result = "true";
				else
					stopR_result = "false";

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
			json.put("stopR_result", stopR_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // ��ǰ��дjson����
		System.out.println(json);
		out.flush();
		out.close();

	}

}
