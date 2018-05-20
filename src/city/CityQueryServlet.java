package city;

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

public class CityQueryServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // 输入流，对前台response
		
		String cityname = request.getParameter("cityname");
		String cityvalue = request.getParameter("cityvalue");
		System.out.println("查询城市："+cityname+cityvalue);
		JSONObject json=null;
		json=new JSONObject();
		if (cityname==null||cityname.equals("")||cityvalue==null||cityvalue.equals("")) {
			try {
				json.put("result","info_error");
			} catch (JSONException e) {				
				e.printStackTrace();
			}					 
		}else{
			DbDao dd = null;						
			try{				
				dd = new DbDao();
				String sql = "";
				sql="select distinct "+cityname+" from route where "+cityname+" like ?";			
				ResultSet rs=dd.query(sql,"%"+cityvalue+"%");
				
				int count=0;
				String jsoncitystr="";
				while(rs.next()){	
					count++;
					jsoncitystr+="'"+rs.getString(cityname)+"',"; //json格式字符串					
				}
				if(count==0){
					json.put("result","nocity");
				}
				else{
					JSONObject jsoncity=new JSONObject("{'value':["+jsoncitystr+"]}");
					json.put("result", "yescity");
					json.put("citysum",count);
					json.put("city",jsoncity);
					
				}
				rs.close();	
							
			}catch (JSONException e1) {			
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
		System.out.println(json); //
		out.print(json);	//向前端写json数据
		out.flush();
		out.close();
	}

}
