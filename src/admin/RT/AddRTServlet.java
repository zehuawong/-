package admin.RT;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class AddRTServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		JSONObject json = null;
		json = new JSONObject();

		String RTstr[] = new String[8];
		String routeID = request.getParameter("routeID");
		String busnum = request.getParameter("busnum");
		String ddate = request.getParameter("ddate");
		String dtime = request.getParameter("dtime");
		String dur = request.getParameter("dur");
		String ssum = request.getParameter("ssum");
		//String sleft = request.getParameter("sleft");
		String sleft=ssum; //余票等于总票数
		String price = request.getParameter("price");

		RTstr[0] = ddate;
		RTstr[1] = dtime;
		RTstr[2] = busnum;
		RTstr[3] = dur;
		RTstr[4] = ssum;
		RTstr[5] = sleft;
		RTstr[6] = price;
		RTstr[7] = routeID;

		String addRT_result = "false";
		boolean flag = true;
		for (int i = 0; i < 8; i++) {
			if (RTstr[i] == null || RTstr[i].equals("")) {
				flag = false;
				break;
			}
		}
		if (flag == false) {
			addRT_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "select busID from bus where busnum=?";
				ResultSet rs=dd.query(sql, busnum);
				String busID=null;
				if(rs.next()){
					busID=rs.getString("busID");

					sql = "insert into routeticket "
							+ " (routeID,busID,departuredate,departuretime,duration,seatssum,seatsleft,price)"
							+ "  values(?,?,?,?,?,?,?,?)";

					if (dd.insert(sql,routeID,busID,ddate, dtime, dur, ssum, sleft, price) == true)
						addRT_result = "true";
					else
						addRT_result = "false";
					
				}
				else addRT_result = "false";

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
			json.put("addRT_result", addRT_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
