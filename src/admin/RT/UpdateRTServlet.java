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

public class UpdateRTServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		JSONObject json = null;
		json = new JSONObject();

		String RTstr[] = new String[8];
		String RTID = request.getParameter("RTID");
		String busnum = request.getParameter("busnum");
		String ddate = request.getParameter("ddate");
		String dtime = request.getParameter("dtime");
		String dur = request.getParameter("dur");
		String ssum = request.getParameter("ssum");
		String sleft = request.getParameter("sleft");
		String price = request.getParameter("price");
		RTstr[0] = RTID;
		RTstr[1] = busnum;
		RTstr[2] = ddate;
		RTstr[3] = dtime;
		RTstr[4] = dur;
		RTstr[5] = ssum;
		RTstr[6] = sleft;
		RTstr[7] = price;
		String updateRT_result = "false";
		boolean flag = true;
		for (int i = 0; i < 8; i++) {
			if (RTstr[i] == null || RTstr[i].equals("")) {
				flag = false;
				break;
			}
		}
		if (flag == false) {
			updateRT_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "";

				sql = "update RouteTicket" + " set busID=(select busID from Bus where busnum=?)"
						+ " ,departuredate=? , departuretime=?, duration=?"
						+ " ,seatssum=?, seatsleft=?, price=?" 
						+ " where routeticketID=?";

				if (dd.update(sql,busnum,ddate,dtime,dur,ssum,sleft,price,RTID) == true)
					updateRT_result = "true";
				else
					updateRT_result = "false";

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
			json.put("updateRT_result", updateRT_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
