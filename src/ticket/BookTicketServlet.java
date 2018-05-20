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
		PrintWriter out = response.getWriter(); // ��ǰ̨response

		String routeTicketID = request.getParameter("routeTicketID");
		JSONObject json = null;
		json = new JSONObject();
		try {
			json.put("result", "false"); // ����Ĭ��ֵ
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		if (routeTicketID == null || routeTicketID.equals("")) { // ��������ļ��
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
				if (username == null || username.equals("")) { // ���username(phoneNumber)����session���û���δ��¼
					json.put("result", "false");
				} else {
					// ��Ʊ֮ǰ�鿴���ݿ�Ʊ���Ƿ���ʣ��Ʊ��
					sql = "select seatsLeft from RouteTicket where routeTicketID=? and status='Y'";
					rs = dd.query(sql, routeTicketID);
					if (rs.next()) {
						int seatsLeft = rs.getInt("seatsLeft");//���Ʊ��
						if (seatsLeft >= 1) { // �����ʣ��Ʊ��
							sql = "select passengerID from passengerinfo where phoneNumber=?";
							rs = dd.query(sql, phoneNumber);
							if (rs.next()) {
								String passengerID = rs.getString("passengerID");
								sql="update RouteTicket set seatsLeft=seatsLeft-1 where routeTicketID=?";
								dd.modify(sql,routeTicketID); //Ʊ����һ								
								
								sql = "insert into ticket(ticketID,passengerID,routeticketID,status) values(null,?,?,'Y')";
								if (dd.insert(sql, passengerID, routeTicketID) == true) { //�򶩵���ticket����һ����Ʊ��¼
									json.put("result", "true");																		
									
								} else
									json.put("result", "false");

							} else {
								json.put("result", "false");
							}

						} else { // Ʊ������
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
		out.print(json); // ��ǰ��дjson����
		out.flush();
		out.close();

	}

}
