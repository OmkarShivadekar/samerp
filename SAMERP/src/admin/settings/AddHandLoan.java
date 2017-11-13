package admin.settings;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.General.GenericDAO;
import utility.RequireData;

@WebServlet("/AddHandLoan")
public class AddHandLoan extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		GenericDAO gd=new GenericDAO();
		
		if(request.getParameter("handloanbtn")!=null)
		{
			
			//Handloan master
			int status=0;		
			String name=request.getParameter("name");				
			String mobileNo=request.getParameter("mobileno");	
			System.out.println("mobileNo");
			String aliasname="HL_"+name.replace(" ", "_")+"_"+mobileNo;				
			String date=request.getParameter("date");				
			String amount=request.getParameter("paidAmt");				
			String paymode=request.getParameter("payMode");
			String chequeNo=request.getParameter("chequeNo");
			String bank_name=request.getParameter("bankInfo");
			
			
			String HL_MasterDetails="INSERT INTO handloan_master(name,mob_no,alias_name) VALUES('"+name+"','"+mobileNo+"','"+aliasname+"')";
			status=gd.executeCommand(HL_MasterDetails);
			
			//debtor master 
			
			String debtor_master="insert into `debtor_master`(`type`) values('"+aliasname+"')";
			gd.executeCommand(debtor_master);
			
			String maxid="select MAX(id) from handloan_master";
			String handloan_id=gd.getData(maxid).get(0).toString();
			
					
			if(paymode.equals("Cash"))
			{
				String insert_query1="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+amount+"')";
				System.out.println("cash:"+insert_query1);
				status=gd.executeCommand(insert_query1);
				
			}else if(paymode.equals("Cheque"))
			{
				
				String insert_bank1="INSERT INTO bank_account_details(date,credit,particulars,balance) VALUES('"+date+"','"+amount+"','"+paymode+"','"+amount+"');";
				gd.executeCommand(insert_bank1);
				
				String insert_query2="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`particulars`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+chequeNo+"','"+amount+"')";
				System.out.println("cheque:"+insert_query2);
				status=gd.executeCommand(insert_query2);
				
			}else if(paymode.equals("Transfer"))
			{
				
				String insert_bank2="INSERT INTO bank_account_details(date,credit,particulars,balance) VALUES('"+date+"','"+amount+"','"+paymode+"','"+amount+"');";
				gd.executeCommand(insert_bank2);
				
				String insert_query3="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+amount+"')";
				System.out.println("transfer:"+insert_query3);
				status=gd.executeCommand(insert_query3);
			}
			
			
			if(status>0)
			{
				System.out.println("inserted Successfully");
				request.setAttribute("hName", name);
				request.setAttribute("amt", amount);
				request.setAttribute("status", "Handloan Details Added");
			}
			else
			{
				System.out.println("plz Try Again");
				request.setAttribute("status", "Handloan Insertion Fail");
			}
			
			RequestDispatcher rq=request.getRequestDispatcher("jsp/admin/settings/addHandLoan.jsp");
			rq.forward(request, response);
			
		}
		
		if(request.getParameter("deleteId")!=null)
		{
			String id=request.getParameter("deleteId");
			String delete_query="";
		}
		
		if(request.getParameter("updateHandLoanDetails")!=null)
		{
			String updateId=request.getParameter("updateHandLoanDetails");
			System.out.println("up:"+updateId);
			RequireData rq=new RequireData();
			List list=rq.selectHandLoanDetails(updateId);
			System.out.println("Data List:"+list);
			Iterator itr=list.iterator();
			while(itr.hasNext())
			{
				out.println(itr.next()+",");
			}
					
		}
		
		if(request.getParameter("handloan")!=null)
		{ 
			int status=0;
				
			String name=request.getParameter("name");				
			String mobileNo=request.getParameter("mobileno");							
			String date=request.getParameter("requiredate");	
			String amount=request.getParameter("paidAmt");				
			String paymode=request.getParameter("payMode");
			String chequeNo=request.getParameter("chequeNo");
			String aliasname="HL_"+name.replace(" ", "_")+"_"+mobileNo;	

			String updateId=request.getParameter("Updateid");
			System.out.println("update id:"+updateId);
			
			String update_query="UPDATE handloan_master,handloan_details SET handloan_master.name='"+name+"',handloan_master.mob_no='"+mobileNo+"', handloan_details.date='"+date+"',handloan_details.credit='"+amount+"',handloan_details.mode='"+paymode+"',handloan_details.particulars='"+chequeNo+"' WHERE handloan_master.id=handloan_details.handloan_id AND handloan_details.handloan_id='"+updateId+"'";
			System.out.println("update:"+update_query);
			 status=gd.executeCommand(update_query);
			if(status==0)
			{
				System.out.println("Updated Successfully ");
				request.setAttribute("status", "Updated  Successfully");
			}
			RequestDispatcher rd=request.getRequestDispatcher("jsp/admin/settings/addHandLoan.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
