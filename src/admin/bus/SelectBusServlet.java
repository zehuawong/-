package admin.bus;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DbDao.DbDao;

public class SelectBusServlet  extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 对前台response		

		JSONObject json = null;
		json = new JSONObject();
		String reqkey=request.getParameter("reqkey"); //查询参数
		String reqvalue=request.getParameter("reqvalue"); //值
		System.out.println("查询车辆参数:"+reqkey+reqvalue);
		//||reqvalue==null||reqvalue.equals("")
		String selectbus_result = "false";
		if (reqkey == null || reqkey.equals("")) {
			selectbus_result = "false";
		} else {
			DbDao dd = null;
			JSONArray json_array=null;
			try {
				dd = new DbDao();
				json_array=new JSONArray();				
				String sql = "";
				if(reqkey.equals("all")){
					sql="select busID,bussize,busnum,bustype,busdriver,phonenumber from bus "
							+ " where status='Y' order by busID";
				}
				else {
					sql="select busID,bussize,busnum,bustype,busdriver,phonenumber from bus "
							+ " where status='Y' and "+reqkey+"="+"'"+reqvalue+"'"+" order by busID";
					
				}
				ResultSet rs=dd.query(sql);
				int count=0;
				while(rs.next()){
					 JSONObject tempjson=new  JSONObject();
					 tempjson.put("busID",rs.getString("busID"));
					 tempjson.put("bussize",rs.getString("bussize"));
					 tempjson.put("busnum",rs.getString("busnum"));
					 tempjson.put("bustype",rs.getString("bustype"));
					 tempjson.put("busdriver",rs.getString("busdriver"));
					 tempjson.put("phonenumber",rs.getString("phonenumber"));
					 json_array.put(tempjson);
					 count++;
				}
				if(count==0){
					selectbus_result = "false";
				}else{
					selectbus_result = "true";
					json.put("bussum", count);
					json.put("bus",json_array);
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
			json.put("selectbus_result", selectbus_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // 向前端写json数据
		System.out.println(json);
		out.flush();
		out.close();

	}

}
