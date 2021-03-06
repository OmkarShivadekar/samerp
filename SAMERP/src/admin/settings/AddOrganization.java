package admin.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import dao.General.GenericDAO;
import dao.dbconnect.DBConnection;
import utility.RequireData;

@MultipartConfig(maxFileSize = 16177215)
public class AddOrganization extends HttpServlet {
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		GenericDAO gd=new GenericDAO();
		
	
		if(request.getParameter("insertorganizer")!=null){
			
			String organizationname=request.getParameter("organizationname");
			String address=request.getParameter("address");
			String contactno1=request.getParameter("contactno1");
			String contactno2=request.getParameter("contactno2");
			String email=request.getParameter("email");		
			String opening_balance=request.getParameter("opening_balance");
			int status=0;
		
			String insertQuery="INSERT INTO organization_details(organization_name, organization_address, organization_contactno1,organization_contactno2, organization_email,organization_op_balance) VALUES ('"+organizationname+"','"+address+"','"+contactno1+"','"+contactno2+"','"+email+"','"+opening_balance+"');";
					
			status=gd.executeCommand(insertQuery);
			
			/*//banck account details
			String opening_bal="INSERT INTO bank_account_details(particulars,balance) VALUES('Opening Balance','"+opening_balance+"');";
			status=gd.executeCommand(opening_bal);*/
		

			if(status==0)
			{
				System.out.println("organizer successfully inserted");
				request.setAttribute("status", "Organization Inserted Successfully");
				
			}
			else{
				System.out.println("Fail to upload");
			}
			RequestDispatcher rd=request.getRequestDispatcher("jsp/admin/settings/addOrganization.jsp");
			rd.forward(request, response);
		}
		
		System.out.println("organization id:"+request.getParameter("deleteId"));
		if(request.getParameter("deleteId")!=null)
		{
			int delstatus=0;
			String deleteQuery="Delete from organization_details where organization_id="+request.getParameter("deleteId");
			delstatus=gd.executeCommand(deleteQuery);
			if(delstatus!=0)
			{
				System.out.println("organization successfully deleted");
				request.setAttribute("status", "Organization Deleted Successfully");
				RequestDispatcher rd=request.getRequestDispatcher("jsp/admin/settings/addOrganization.jsp");
				rd.forward(request, response);
			}
			
		}
		
		if(request.getParameter("organization_id")!=null)
		{
			String RowId=request.getParameter("organization_id");
			RequireData rd=new RequireData();
			List demoList=rd.getOrganizationRowData(RowId);
			Iterator itr=demoList.iterator();
			while(itr.hasNext())
			{
				out.print(itr.next()+",");
			}
		}
		
		if(request.getParameter("org_submitbtn")!=null)
		{

			String organization_id = request.getParameter("organization_id");
			String organization_name = request.getParameter("organization_name");
			String organization_address = request.getParameter("organization_address");
			String contact_no1 = request.getParameter("contact_no1");
			String contact_no2 = request.getParameter("contact_no2");
			String email_id = request.getParameter("email_id");
			String up_opening_balance=request.getParameter("opening_balance");

			String updateorganizationQuery = "update organization_details set organization_name='"+organization_name+"', organization_address='"+organization_address+"',"
					+ " organization_contactno1='"+contact_no1+"', organization_contactno2='"+contact_no2+"', organization_email='"+email_id+"',organization_op_balance='"+up_opening_balance+"'  where organization_id='"+organization_id+"';";
			System.out.println("Q===>"+updateorganizationQuery);
			int updatestatus = gd.executeCommand(updateorganizationQuery);
			
			if(updatestatus!=0){
				request.setAttribute("status", "Organization Updated Successfully");
			}else{
				System.out.println("update fail");
				request.setAttribute("status", "Organization Update Fail");
			}
			RequestDispatcher rd = request.getRequestDispatcher("jsp/admin/settings/addOrganization.jsp");
			rd.forward(request, response);
		}
	}
}