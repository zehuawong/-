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

public class AddRouteServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response		

		JSONObject json = null;
		json = new JSONObject();
		String startcity=request.getParameter("startcity"); 
		String endcity=request.getParameter("endcity"); 	
		String startpoint=request.getParameter("startpoint"); 
		String destination=request.getParameter("destination"); 
		
		
		String addR_result = "false";
		if (startcity == null || startcity.equals("")||endcity==null||endcity.equals("") 
			||startpoint == null || startpoint.equals("")||destination==null||destination.equals("")) {
			addR_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";		
				//应该做合法性检查，是否重复
				sql="insert into route(startcity,endcity,startpoint,destination)" +"values(?,?,?,?)";
				
				if(dd.insert(sql,startcity,endcity,startpoint,destination)==true){ 
					addR_result = "true";
					//获取routeID
					sql="select routeID from route where startcity=? and endcity=? and "
							+ " startpoint=? and destination=?";
					
					ResultSet rs=dd.query(sql,startcity,endcity,startpoint,destination);
					if(rs.next()){
						String routeID=rs.getString("routeID");
						json.put("routeID", routeID);
						
					}else addR_result = "false";
					
					
				}else{  //添加失败，请检查是否重复
					addR_result = "false";
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
			json.put("addR_result", addR_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
