package admin.bus;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class DeleteBusServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response		

		JSONObject json = null;
		json = new JSONObject();
		String busID=request.getParameter("busID"); 
			
		String deletebus_result = "false";
		if (busID==null||busID.equals("")) {
			deletebus_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "update bus set status='N' where busID=?";
				if(dd.update(sql, busID)){
					deletebus_result = "true";
				}else{
					deletebus_result = "false";
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
			json.put("deletebus_result", deletebus_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}