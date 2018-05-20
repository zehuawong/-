package order;

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

public class CancelOrderServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response
		String ticketID = request.getParameter("ticketID");
		JSONObject json = null;
		json = new JSONObject();
		String cancel_result = "false";
		if (ticketID == null || ticketID.equals("")) {
			cancel_result = "false";
		} else {
			DbDao dd = null;		
			try {		
				dd = new DbDao();
				String sql ="";
				sql+="update ticket set status='N' where ticketID=?";
				if(dd.insert(sql,ticketID)==true){
					cancel_result="true";
				}
					
			}catch (Exception e) {
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
			json.put("cancel_result",cancel_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		out.print(json); // 向前端写json数据
		System.out.println(json); //
		out.flush();
		out.close();

	}

}
