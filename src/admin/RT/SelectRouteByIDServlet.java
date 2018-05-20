package admin.RT;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class SelectRouteByIDServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response
		JSONObject json = null;
		json = new JSONObject();

		String routeID = request.getParameter("routeID");

		String selectR_result = "false";
		if (routeID == null || routeID.equals("")) {
			selectR_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";
				sql="select startcity,endcity,startpoint,destination"
						+ " from route where routeID=?";				
				ResultSet rs = dd.query(sql, routeID);
				if (rs.next()) {
					json.put("startcity",rs.getString("startcity"));
					json.put("endcity",rs.getString("endcity"));
					json.put("startpoint",rs.getString("startpoint"));
					json.put("destination",rs.getString("destination"));
					
					selectR_result = "true";
				}else{
					selectR_result = "false";
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
			json.put("selectR_result", selectR_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
