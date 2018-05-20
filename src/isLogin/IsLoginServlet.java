package isLogin;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*这个servlet用来处理用户查看是否已经登录的请求和注销登录功能的请求*/

public class IsLoginServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); //对前台response

		// 获取请求参数
		String req_log = request.getParameter("req_log");
		System.out.println("请求参数:" + req_log);
		if (req_log == null) {
			out.print("false");
		}
		else {
			// 获取session对象
			HttpSession session = request.getSession(true);
			//如果是查看是否登录参数
			if (req_log.equals("islogin")) {

				// 获取用户名，查看是否登录
				Object username = session.getAttribute("username");
				if (username == null) {
					out.print("false");
				} else {
					out.print(username.toString());
				}
			} 
			//如果是注销 请求
			else if (req_log.equals("logout")) {
				session.removeAttribute("username");
				out.print("true");
			}
		}

		out.close();
	}
}
