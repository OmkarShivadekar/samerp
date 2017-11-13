package admin.pettycash;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.General.GenericDAO;

//@WebServlet("/PTCash")
public class PTCash extends HttpServlet {
	
	public static int sum (List<Integer> list) {
	    int sum = 0;
	    for (int i: list) {
	        sum += i;
	    }
	    return sum;
	}
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			GenericDAO gd=new GenericDAO();
						
			int status=0;
			
			
			if(request.getParameter("insertPetty")!=null)
			{
				String pettyDate=request.getParameter("date");
				String previousBalance=request.getParameter("previous_balance");
				String bankName=request.getParameter("bank_name");
				int count=Integer.parseInt(request.getParameter("count"));
				int bankCount=Integer.parseInt(request.getParameter("bankCount"));
				//int cash[]=new int[30];
				ArrayList<Integer> cash=new ArrayList<>();
				for(int i=0;i<=count;i++)
				{
					if(!request.getParameter("hlName,"+i).isEmpty())
					{
						String handLoanName=request.getParameter("hlName,"+i);
						String add="HL_";
						String alias=add+handLoanName;
						
						String handLoanDetails="SELECT handloan_details.handloan_id,handloan_details.balance FROM handloan_details,handloan_master WHERE handloan_details.handloan_id=handloan_master.id AND handloan_master.alias_name='"+alias+"' ORDER BY handloan_details.id DESC LIMIT 1";
						List getDetails=gd.getData(handLoanDetails);
						 
						int addAmt=(int)getDetails.get(1);
						int amt=Integer.parseInt(request.getParameter("hlAmt,"+i));
						cash.add(amt);
						int updateAmt=addAmt+amt;
						out.println("handloan new amt : "+updateAmt+"<br>");
						
						
						String insertHandLoanDetails="INSERT INTO `handloan_details`(`handloan_id`, `date`, `credit`, `debit`, `mode`, `particulars`, `balance`) "
								+ "VALUES ("+getDetails.get(0)+",'"+pettyDate+"',"+amt+","+0+",'Cash','',"+updateAmt+")";
						int insertHandLoanStatus=gd.executeCommand(insertHandLoanDetails);
						if(insertHandLoanStatus>0)
						{
							System.out.println("inserted credit amt and balance in handloanDetails");
						}
						
						
						String getDebtorId="SELECT debtor_master.id FROM debtor_master WHERE debtor_master.type='"+alias+"'";
						List debtorId=gd.getData(getDebtorId);
						
						
						String getLastPettyBalance="SELECT petty_cash_details.id,petty_cash_details.balance FROM petty_cash_details ORDER BY petty_cash_details.id DESC LIMIT 1";
						List lastPettyBalance=gd.getData(getLastPettyBalance);
						
						
						int updatedPettyBalance=amt+(int)lastPettyBalance.get(1);
						
						String insertPetty="INSERT INTO `petty_cash_details`(`date`, `debit`, `credit`, `debtor_id`, `balance`) VALUES ('"+pettyDate+"',"+0+","+amt+","+debtorId.get(0)+","+updatedPettyBalance+")";
						int insertStatus=gd.executeCommand(insertPetty);
						if(insertStatus>0)
						{
							System.out.println("inserted in pettyCash for "+i+" time");
						}
					}
					else
					{
						System.out.println("nothing update in handloan");
						
						//out.print("nothing update in handloan");
					}
					
					
					//out.println("petty balance new"+updatedPettyBalance+"<br>");
					
					
						
					//out.println("Alias : "+alias+" insert amount : "+amt+" new amt : "+updateAmt);
				}
				for(int j=0;j<=bankCount;j++)
				{
					if(!request.getParameter("bName,"+j).isEmpty())
					{
						String bankAlias=request.getParameter("bName,"+j);
						
						String bankDetailsQuery="SELECT bank_account_details.bid,bank_account_details.balance FROM bank_account_details,account_details WHERE bank_account_details.bid=account_details.acc_id AND account_details.acc_aliasname='"+bankAlias+"' ORDER BY bank_account_details.id DESC LIMIT 1";
						List getBankDetails=gd.getData(bankDetailsQuery);
						
						int bankBalance=(int)getBankDetails.get(1);
						int withdrawlAmt=Integer.parseInt(request.getParameter("bAmt,"+j));
						
						if(bankBalance<withdrawlAmt)
						{
							System.out.println("Insufficient balance in your "+bankAlias+" Bank");
							request.setAttribute("status", "Insufficient balance in your "+bankAlias+" Bank");
							RequestDispatcher rq=request.getRequestDispatcher("jsp/admin/PTCash/ptcash.jsp");
							rq.forward(request, response);
						}
						else
						{
							cash.add(withdrawlAmt);
							String getDebtorId="SELECT debtor_master.id FROM debtor_master WHERE debtor_master.type='"+bankAlias+"'";
							List debtorId=gd.getData(getDebtorId);
							
							int newBankBalance=bankBalance-withdrawlAmt;
							String insertBankdetails="INSERT INTO `bank_account_details`(`bid`, `date`, `debit`, `credit`, `particulars`, `debter_id`, `balance`) "
									+ "VALUES ("+getBankDetails.get(0)+",'"+pettyDate+"',"+withdrawlAmt+","+0+",'HandLoan',"+debtorId.get(0)+","+newBankBalance+")";
							int bankDetailsStatus=gd.executeCommand(insertBankdetails);
							if(bankDetailsStatus>0)
							{
								System.out.println("inserted in bank account details");
							}
							else
							{
								System.out.println("bank error");
							}
							
							String getLastPettyBalance="SELECT petty_cash_details.id,petty_cash_details.balance FROM petty_cash_details ORDER BY petty_cash_details.id DESC LIMIT 1";
							List lastPettyBalance=gd.getData(getLastPettyBalance);
							
							int updatedPettyBalance=withdrawlAmt+(int)lastPettyBalance.get(1);
							
							String insertPetty="INSERT INTO `petty_cash_details`(`date`, `debit`, `credit`, `debtor_id`, `balance`) VALUES ('"+pettyDate+"',"+0+","+withdrawlAmt+","+debtorId.get(0)+","+updatedPettyBalance+")";
							int insertStatus=gd.executeCommand(insertPetty);
							if(insertStatus>0)
							{
								System.out.println("inserted in pettyCash for "+j+" time");
							}
							else
							{
								System.out.println("petty error");
							}
						
						}
					}
					else
					{
						System.out.println("nothing update in bank");
						//out.print("nothing update in handloan");
					}
					
				}
				
				
				int sum=PTCash.sum(cash);
				String getLastPettyBalance="SELECT petty_cash_details.id,petty_cash_details.balance FROM petty_cash_details ORDER BY petty_cash_details.id DESC LIMIT 1";
				List lastPettyBalance=gd.getData(getLastPettyBalance);
				request.setAttribute("status", "Rs."+sum+" Petty Cash Added. Total Petty Cash Balance is Rs."+lastPettyBalance.get(1));
				RequestDispatcher rq=request.getRequestDispatcher("jsp/admin/PTCash/ptcash.jsp");
				rq.forward(request, response);
				
				
			}
			
				
					
			if(request.getParameter("handloanbtn")!=null)
			{
				
				//Handloan master
				int status1=0;		
				String name=request.getParameter("name");				
				String mobileNo=request.getParameter("mobileno");	
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
				
				
				if(status1>0)
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
				
				RequestDispatcher rq=request.getRequestDispatcher("jsp/admin/PTCash/ptcash.jsp");
				rq.forward(request, response);
				
			}
			
			if(request.getParameter("findName")!=null)
			{
				String name=request.getParameter("findName");
				String query="SELECT handloan_master.id,handloan_master.alias_name FROM handloan_master";
				
				List getName=gd.getData(query);
				Iterator itr=getName.iterator();
				while(itr.hasNext())
				{
					itr.next();
					out.print("<option>"+itr.next().toString().replace("HL_", "")+"</option>");
				}	
				
			}
			if(request.getParameter("findBankName")!=null)
			{
				String bankName=request.getParameter("findBankName");
				String query="SELECT account_details.acc_id,account_details.acc_aliasname FROM account_details";
				List getBankDetails=gd.getData(query);
				Iterator itr=getBankDetails.iterator();
				while(itr.hasNext())
				{
					itr.next();
					out.print("<option>"+itr.next()+"</option>");
				}
			}
			
			
			if(request.getParameter("eName")!=null)
			{
				String name=request.getParameter("eName");
				String add="HL_";
				String alias=add+name;
				String query="SELECT handloan_details.credit FROM handloan_details,handloan_master WHERE handloan_master.id=handloan_details.handloan_id AND handloan_master.alias_name='"+alias+"'";
				
				List getAmt=gd.getData(query);
				out.print(getAmt.get(0));
			}
			
			
			
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
