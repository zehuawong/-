package admin.order;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class HisOrderQueryServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		JSONObject json = null;
		json = new JSONObject();
		String reqkey= request.getParameter("reqkey");
		String reqvalue=request.getParameter("reqvalue");

		String selectorder_status = "false";
		if (reqkey==null||reqkey.equals("")||reqvalue==null||reqvalue.equals(""))
			selectorder_status = "false";
		else {
			DbDao dd = null;
			JSONArray json_array = null;
			try {
				json_array = new JSONArray();
				dd = new DbDao();
				String sql = "select ticketID,departureDate,departureTime,startPoint,destination,price,orderdateTime,ticket.status"
						   		+" from ticket,route,routeticket,passengerinfo"
						   		+" where ticket.passengerID=passengerinfo.passengerID"
						   		+" and ticket.routeTicketID=routeticket.routeTicketID"
						   		+" and routeticket.routeID=route.routeID and ticket.status='Y'"
						   		+" and "+reqkey+"='"+reqvalue+"'";
				
				sql+=" order by departureDate,departureTime asc"; //根据出发日期,时间排序
				
				// 查询结果集
				ResultSet rs = dd.query(sql);
				int count = 0;
				Date nowdate = new Date();//获得系统当前时间：
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
				Date t;	String departureDate="";String departureTime="";String status="";
				String timestr="";
				while (rs.next()) {
					JSONObject tempJsonObj = new JSONObject();
					tempJsonObj.put("ticketID", rs.getString("ticketID"));
					departureDate=rs.getString("departureDate");
					departureTime=rs.getString("departureTime");
					timestr=departureDate+" "+departureTime;
					tempJsonObj.put("departDateTime",timestr);	
					tempJsonObj.put("startPoint", rs.getString("startPoint"));
					tempJsonObj.put("destination", rs.getString("destination"));
					tempJsonObj.put("price", rs.getString("price"));		
					tempJsonObj.put("orderdateTime",rs.getString("orderdateTime"));
					status=rs.getString("status");	 //订单状态：已确认Y，已取消N
					t=ft.parse(timestr);
					if(t.before(nowdate))	//如果车票时间在系统时间之前且status=Y	
						status="O";	//订单状态：已乘车O																
					tempJsonObj.put("status",status);
					json_array.put(tempJsonObj); // 相当于放入一行数据到json数组
					count++;
				}
				if (count == 0) {		
					selectorder_status = "no_hisOrder";
				} else {
					selectorder_status = "true";
					json.put("ordersum", count);
					json.put("order", json_array);
				}
				rs.close();
			} catch (JSONException e1) {
				e1.printStackTrace();
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
			json.put("selectorder_status", selectorder_status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(json); // 向前端写json数据
		System.out.println(json); //
		out.flush();
		out.close();

	}

}
