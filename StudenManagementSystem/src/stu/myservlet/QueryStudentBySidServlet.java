package stu.myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stu.mybean.Student;
import stu.service.StudentService;

@SuppressWarnings({ "unused", "serial" })
public class QueryStudentBySidServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			int id = Integer.parseInt(request.getParameter("sid"));
			StudentService service = new StudentService();
			Student student = service.queryStudentBySid(id);
			System.out.println(student);
			//�����˵�����ͨ��ǰ̨jsp��ʾstudentInfo
			request.setAttribute("student",student);//����ѯ����ѧ���ŵ�request����
			//���request����û�����ݣ�ʹ���ض�����תresponse.sendRedirect();
			//���request����û�����ݣ�request.setAttribute();����ʹ������ת����ת
			request.getRequestDispatcher("studentInfo.jsp").forward(request, response);
			
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request,response);
	}

}
