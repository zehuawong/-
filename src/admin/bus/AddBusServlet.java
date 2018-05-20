package admin.bus;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class AddBusServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response		

		JSONObject json = null;
		json = new JSONObject();
		 
		String busnum=request.getParameter("busnum");
		String bussize=request.getParameter("bussize");
		String bustype=request.getParameter("bustype");
		String busdriver=request.getParameter("busdriver");
		String phonenumber=request.getParameter("phonenumber");
		
		String addbus_result = "false";
		if (busnum==null||busnum.equals("")||bussize==null||
				bussize.equals("")||bustype==null||bustype.equals("")||busdriver==null||
				busdriver.equals("")||phonenumber==null||phonenumber.equals("")) {
			addbus_result = "false";
		} else {
			DbDao dd = null;
			try {
				dd = new DbDao();
				String sql = "insert into bus(busnum,bussize,bustype,busdriver, phonenumber)"
						+ " values(?,?,?,?,?)";
				if(dd.insert(sql,busnum,bussize,bustype,busdriver, phonenumber)){
					addbus_result = "true";
					sql="select busID from bus where busnum=?";
					ResultSet rs=dd.query(sql, busnum);
					if(rs.next()){
						json.put("busID",rs.getString("busID"));
					}
					
				}
				else{
					addbus_result = "false";
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
			json.put("addbus_result", addbus_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
