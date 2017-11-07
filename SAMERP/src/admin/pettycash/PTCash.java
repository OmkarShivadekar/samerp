package admin.pettycash;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.General.GenericDAO;

@WebServlet("/PTCash")
public class PTCash extends HttpServlet {
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			GenericDAO gd=new GenericDAO();
						
			int status=0;
			
					
			if(request.getParameter("handloanbtn")!=null)
			{
				
				//Handloan master
				int status1=0;		
				
				
				String name=request.getParameter("name");				
				String mobileNo=request.getParameter("mobileno");	
				System.out.println("mobile is:"+mobileNo);
				String aliasname="HL_"+name.replace(" ", "_")+"_"+mobileNo;				
				String HL_MasterDetails="INSERT INTO handloan_master(name,mob_no,alias_name) VALUES('"+name+"','"+mobileNo+"','"+aliasname+"')";
				status=gd.executeCommand(HL_MasterDetails);
				
				if(status==0)
				{
					System.out.println("Inserted Successfully");
					request.setAttribute("status", "Inserted Successfully");
				}
				
					
				
				//debtor master 
				
				String debtor_master="insert into `debtor_master`(`type`) values('"+aliasname+"')";
				gd.executeCommand(debtor_master);
				
				String maxid="select MAX(id) from handloan_master";
				String handloan_id=gd.getData(maxid).get(0).toString();
				
						
				String date=request.getParameter("date");				
				String amount=request.getParameter("paidAmt");				
				String paymode=request.getParameter("payMode");
				String chequeNo=request.getParameter("chequeNo");
				String bank_name=request.getParameter("bankInfo");
				
			String q=handloan_id+","+date+","+amount+","+paymode+","+chequeNo+","+bank_name;
				System.out.println(q);
				
				if(paymode.equals("Cash"))
				{
					String insert_bank="INSERT INTO bank_account_details(date,credit,particulars,balance) VALUES('"+date+"','"+amount+"','"+paymode+"','"+amount+"');";
					gd.executeCommand(insert_bank);
					
					String insert_query1="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+amount+"')";
					System.out.println("cash:"+insert_query1);
					status1=gd.executeCommand(insert_query1);
					
				}else if(paymode.equals("Cheque"))
				{
					
					String insert_bank1="INSERT INTO bank_account_details(date,credit,particulars,balance) VALUES('"+date+"','"+amount+"','"+paymode+"','"+amount+"');";
					gd.executeCommand(insert_bank1);
					
					String insert_query2="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`particulars`,`description`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+chequeNo+"','"+bank_name+"','"+amount+"')";
					System.out.println("cheque:"+insert_query2);
					status1=gd.executeCommand(insert_query2);
					
				}else if(paymode.equals("Transfer"))
				{
					
					String insert_bank2="INSERT INTO bank_account_details(date,credit,particulars,balance) VALUES('"+date+"','"+amount+"','"+paymode+"','"+amount+"');";
					gd.executeCommand(insert_bank2);
					
					String insert_query3="insert into `handloan_details`(`handloan_id`,`date`,`credit`,`mode`,`description`,`balance`) values('"+handloan_id+"','"+date+"','"+amount+"','"+paymode+"','"+bank_name+"','"+amount+"')";
					System.out.println("transfer:"+insert_query3);
					status1=gd.executeCommand(insert_query3);
				}
				
				
				if(status1==0)
				{
					System.out.println("inserted Successfully");
					request.setAttribute("status", "Insertion Successfully");
				}else
				{
					System.out.println("plz Try Again");
					request.setAttribute("status", "Insertion Fail");
				}
				
				RequestDispatcher rq=request.getRequestDispatcher("jsp/admin/PTCash/ptcash.jsp");
				rq.forward(request, response);
				
			}
			
			if(request.getParameter("findName")!=null)
			{
							
				String name=request.getParameter("findName");
				String query="SELECT handloan_master.id,handloan_master.name FROM handloan_master WHERE handloan_master.name LIKE '%"+name+"%'";
				
				List getName=gd.getData(query);
				Iterator itr=getName.iterator();
				while(itr.hasNext())
				{
					itr.next();
					out.print("<option>"+itr.next()+"</option>");
				}	
				
			}
			
			
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
