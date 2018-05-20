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

public class UpdateNewRouteServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response		

		JSONObject json = null;
		json = new JSONObject();
		String routeID=request.getParameter("routeID");
		String startcity=request.getParameter("startcity"); 
		String endcity=request.getParameter("endcity"); 	
		String startpoint=request.getParameter("startpoint"); 
		String destination=request.getParameter("destination"); 
		
		
		String updateNR_result = "false";
		if (startcity == null || startcity.equals("")||endcity==null||endcity.equals("") 
			||startpoint == null || startpoint.equals("")||destination==null||destination.equals("")
			||routeID==null||routeID.equals("")) {
			updateNR_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";		
				sql="update route"
						+ " set startcity=?,endcity=?,startpoint=?,destination=?"
						+ " where routeID=?";
				
				if(dd.update(sql,startcity,endcity,startpoint,destination,routeID)==true)
					updateNR_result = "true";
				else updateNR_result = "false";
				
				 							
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
			json.put("updateNR_result", updateNR_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
