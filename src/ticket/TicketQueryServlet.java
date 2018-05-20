package ticket;

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

public class TicketQueryServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); //��ǰ̨response

		System.out.println("��Ʊ��ѯ��Ϣ:");
		String startCity = request.getParameter("startCity");
		String endCity = request.getParameter("endCity");
		String departureDate = request.getParameter("departureDate");
		System.out.println("�������У�" + startCity + "������У�" + endCity + "�������ڣ�" + departureDate);	
		 //json�������ŵ���������Ϣ
		JSONObject json=null;
		json=new JSONObject();
		if (startCity == null || endCity == null || departureDate == null) {
			//out.print("false");
			try {
				json.put("result","info_error");
			} catch (JSONException e) {				
				e.printStackTrace();
			}					 
		} else {
			DbDao dd = null;					
			JSONArray json_array=null;		
			
			try {			
				json_array=new JSONArray();					
				dd = new DbDao();
				String sql = "";
				sql = "select routeTicketID,routeTicket.departureTime,Route.startPoint,Route.destination,Bus.busType,RouteTicket.duration,RouteTicket.seatsLeft,RouteTicket.price"
						+ " from Bus,Route,RouteTicket"
						+ " where RouteTicket.routeID=Route.routeID and RouteTicket.busID=Bus.busID"
						+" and bus.status='Y' and Route.status='Y' and RouteTicket.status='Y'"
						+ " and Route.startCity=? and Route.endCity=? and departureDate=?"
						+" order by departureTime";
											
				// ��ѯ�����							
				ResultSet rs=dd.query(sql,startCity,endCity,departureDate);
				int count=0;
				
				Date nowdate = new Date();//���ϵͳ��ǰʱ�䣺
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
				Date t;	String timestr;
				while(rs.next()){				
					JSONObject tempJsonObj=new JSONObject();
					tempJsonObj.put("routeTicketID",rs.getInt("routeTicketID"));
					timestr=rs.getString("departureTime"); //���ʱ��24Сʱ
					tempJsonObj.put("departureTime",timestr);
					tempJsonObj.put("startPoint",rs.getString("startPoint"));
					tempJsonObj.put("destination",rs.getString("destination"));
					tempJsonObj.put("busType",rs.getString("busType"));
					tempJsonObj.put("duration",rs.getString("duration"));
					tempJsonObj.put("seatsLeft",rs.getInt("seatsLeft"));
					tempJsonObj.put("price",rs.getInt("price"));
					t=ft.parse(departureDate+" "+timestr); //��ó�Ʊ��ʱ��
					if(nowdate.before(t)){	//�����Ʊʱ����ϵͳʱ��֮��
						tempJsonObj.put("status","Y"); //��Ʊû�й���
					}else tempJsonObj.put("status","N"); //��Ʊ�ѹ���
					
					json_array.put(tempJsonObj); //�൱�ڷ���һ�����ݵ�json����
					count++;
				}
				if(count==0){
					//out.println("noticket");
					System.out.println("noticket"); //
					json.put("result","noticket");
				}
				else{
					json.put("result", "yesticket");
					json.put("ticketsum",count);
					json.put("ticket",json_array);
				
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
		out.print(json);	//��ǰ��дjson����
		System.out.println(json); //
		out.flush();
		out.close();
	}
}
