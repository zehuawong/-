package admin.route;

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

public class SelectRouteServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response
		JSONObject json = null;
		json = new JSONObject();
		
		String startcity=request.getParameter("startcity"); 
		String endcity=request.getParameter("endcity"); 
		
		String selectR_result = "false";
		if (startcity == null || startcity.equals("")||endcity==null||endcity.equals("")) {
			selectR_result = "false";
		} else {
			DbDao dd = null;
			JSONArray json_array=null;
			try {
				dd = new DbDao();
				json_array=new JSONArray();
				String sql = "";
				sql="select routeID,startpoint,destination,status from route"
						+ " where startcity=? and endcity=? "
						+ " order by routeID";

				 ResultSet rs=dd.query(sql, startcity,endcity);
				 int count=0;
				 while(rs.next()){
					 JSONObject tempJsonObj=new JSONObject();
					 tempJsonObj.put("routeID",rs.getString("routeID"));
					 tempJsonObj.put("s",rs.getString("startpoint"));
					 tempJsonObj.put("t",rs.getString("destination"));
					 tempJsonObj.put("status",rs.getString("status"));
					 json_array.put(tempJsonObj);
					 count++;
					 
				 }
				 if(count==0){
					 selectR_result = "false";
				 }else{
					 selectR_result = "true";
					 json.put("routesum",count);
					 json.put("route",json_array);
					 
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
