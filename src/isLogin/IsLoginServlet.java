package isLogin;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*���servlet���������û��鿴�Ƿ��Ѿ���¼�������ע����¼���ܵ�����*/

public class IsLoginServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); //��ǰ̨response

		// ��ȡ�������
		String req_log = request.getParameter("req_log");
		System.out.println("�������:" + req_log);
		if (req_log == null) {
			out.print("false");
		}
		else {
			// ��ȡsession����
			HttpSession session = request.getSession(true);
			//����ǲ鿴�Ƿ��¼����
			if (req_log.equals("islogin")) {

				// ��ȡ�û������鿴�Ƿ��¼
				Object username = session.getAttribute("username");
				if (username == null) {
					out.print("false");
				} else {
					out.print(username.toString());
				}
			} 
			//�����ע�� ����
			else if (req_log.equals("logout")) {
				session.removeAttribute("username");
				out.print("true");
			}
		}

		out.close();
	}
}
