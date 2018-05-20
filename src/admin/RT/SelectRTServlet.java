package admin.RT;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class SelectRTServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response
		JSONObject json = null;
		json = new JSONObject();

		String startcity = request.getParameter("startcity");
		String endcity = request.getParameter("endcity");
		String departuredate = request.getParameter("departuredate");
		
		
		String selectRT_result = "false";
		if (startcity == null || startcity.equals("") || endcity == null || endcity.equals("")
				||departuredate==null||departuredate.equals("")) {
			selectRT_result = "false";
		} else {
			DbDao dd = null;
			JSONArray json_array = null;
			try {
				dd = new DbDao();
				json_array = new JSONArray();
				String sql = "";	
				sql = "select routeticketID,startpoint,destination,busnum,departuretime,duration,seatssum,seatsleft,price"
						+ " from Bus,Route,RouteTicket"
						+ " where RouteTicket.routeID=Route.routeID and RouteTicket.busID=Bus.busID"
						+ " and RouteTicket.status='Y'"
						+ " and Route.startCity=? and Route.endCity=? and departureDate=? ";
						
				Date nowdate = new Date();//获得系统当前时间：				
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
				String datestr=ft.format(nowdate);
				if(datestr.equals(departuredate)){ //如果日期相等
					sql+=" and RouteTicket.departuretime>=current_time()";
				}
				sql+=" order by departuretime"; 

				ResultSet rs = dd.query(sql, startcity, endcity,departuredate);
				int count = 0;
				while (rs.next()) {
					JSONObject tempjson = new JSONObject();
					tempjson.put("RTID", rs.getString("routeticketID"));
					tempjson.put("s", rs.getString("startpoint"));
					tempjson.put("t", rs.getString("destination"));
					tempjson.put("busnum", rs.getString("busnum"));
					tempjson.put("dtime", rs.getString("departuretime"));
					tempjson.put("dur", rs.getString("duration"));
					tempjson.put("ssum", rs.getString("seatssum"));
					tempjson.put("sleft", rs.getString("seatsleft"));
					tempjson.put("price", rs.getString("price"));
					
					json_array.put(tempjson);
					count++;

				}
				if (count == 0) {
					selectRT_result = "false";
				} else {
					selectRT_result = "true";
					json.put("RTsum", count);
					json.put("RT", json_array);

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
			json.put("selectRT_result", selectRT_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
