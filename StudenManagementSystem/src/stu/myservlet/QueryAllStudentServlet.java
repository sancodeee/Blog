package stu.myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stu.mybean.Student;
import stu.service.StudentService;

@SuppressWarnings({ "unused", "serial" })
public class QueryAllStudentServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		int currPage = 1;//��ǰҳ��
	
		StudentService service = new StudentService();
		
		List<Student> students = service.queryAllStudents(currPage);//��ѯ����ѧ����Ϣ
		
		System.out.println(students);
		
		request.setAttribute("students",students);
		//��Ϊrequest���������ݣ������Ҫͨ������ת���ķ�ʽ��ת���ض���ᶪʧrequest��
		//pageContext<request<session<application
		int pages;//��ҳ��
		int count = service.findCount(); //��ѯ�ܼ�¼��
		if(count % Student.PAGE_SIZE==0){ //������ҳ��
			pages = count / Student.PAGE_SIZE;//����ҳ����ֵ
		}else{
			pages = count / Student.PAGE_SIZE+1;//����ҳ����ֵ
		}
		
		StringBuffer sb = new StringBuffer();  //ʵ����StringBuffer
		for(int i=1;i<=pages;i++){
			if(i==currPage){
				sb.append("["+i+"]");
			}else{
				sb.append("<a href='QueryAllStudentServlet?page="+i+"'>"+i+"</a>");//������ҳ��
			}
			sb.append("  ");
		}
		request.setAttribute("bar", sb.toString());//����ҳ�����ַ����ŵ�request���У�
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);

	}

}
