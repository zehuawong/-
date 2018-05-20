package admin;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

/*���servlet�����������Ա�鿴�Ƿ��Ѿ���¼�������ע����¼���ܵ�����*/

public class IsLoginServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); //��ǰ̨response
		JSONObject json = null;
		json = new JSONObject();
		String log_result="false";
		// ��ȡ�������
		String req_log = request.getParameter("req_log");
		System.out.println("�������:" + req_log);
		if (req_log == null) {
			log_result="false";		
		}
		else {
			// ��ȡsession����
			HttpSession session = request.getSession(true);
			//����ǲ鿴�Ƿ��¼����
			if (req_log.equals("islogin")) {

				// ��ȡ�û������鿴�Ƿ��¼
				Object username = session.getAttribute("username");
				if (username == null) {
					log_result="false";
				} else {
					try {
						log_result="true";
						json.put("username", username.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} 
			//�����ע�� ����
			else if (req_log.equals("logout")) {
				session.removeAttribute("username");
				log_result="true";
			}
		}
		
		try {
			json.put("log_result", log_result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		out.print(json); // ��ǰ��дjson����
		System.out.println(json);
		out.flush();
		out.close();
	}
}
