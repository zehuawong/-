package ticket;

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

public class BookTicketServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response

		String routeTicketID = request.getParameter("routeTicketID");
		JSONObject json = null;
		json = new JSONObject();
		try {
			json.put("result", "false"); // 设置默认值
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		if (routeTicketID == null || routeTicketID.equals("")) { // 请求参数的检查
			try {
				json.put("result", "false");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			DbDao dd = null;
			ResultSet rs = null;
			String sql = "";
			try {
				dd = new DbDao();

				HttpSession session = request.getSession(true);
				Object username = session.getAttribute("username");
				String phoneNumber = username.toString();
				if (username == null || username.equals("")) { // 如果username(phoneNumber)不在session内用户还未登录
					json.put("result", "false");
				} else {
					// 订票之前查看数据库票数是否有剩余票数
					sql = "select seatsLeft from RouteTicket where routeTicketID=? and status='Y'";
					rs = dd.query(sql, routeTicketID);
					if (rs.next()) {
						int seatsLeft = rs.getInt("seatsLeft");//获得票数
						if (seatsLeft >= 1) { // 如果有剩余票数
							sql = "select passengerID from passengerinfo where phoneNumber=?";
							rs = dd.query(sql, phoneNumber);
							if (rs.next()) {
								String passengerID = rs.getString("passengerID");
								sql="update RouteTicket set seatsLeft=seatsLeft-1 where routeTicketID=?";
								dd.modify(sql,routeTicketID); //票数减一								
								
								sql = "insert into ticket(ticketID,passengerID,routeticketID,status) values(null,?,?,'Y')";
								if (dd.insert(sql, passengerID, routeTicketID) == true) { //向订单表ticket增加一条订票记录
									json.put("result", "true");																		
									
								} else
									json.put("result", "false");

							} else {
								json.put("result", "false");
							}

						} else { // 票已卖完
							json.put("result", "noticket");
						}

					} else {
						json.put("result", "false");
					}

				}

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
		System.out.println(json);
		out.print(json); // 向前端写json数据
		out.flush();
		out.close();

	}

}
