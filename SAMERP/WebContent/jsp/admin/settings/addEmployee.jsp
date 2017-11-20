<%@page import="utility.SysDate"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="utility.RequireData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>SAMERP PROJECT</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="/SAMERP/config/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="/SAMERP/config/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="/SAMERP/config/css/fullcalendar.css" />
<link rel="stylesheet" href="/SAMERP/config/css/matrix-style.css" />
<link rel="stylesheet" href="/SAMERP/config/css/matrix-media.css" />
<link href="/SAMERP/config/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="/SAMERP/config/css/jquery.gritter.css" />
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700,800' rel='stylesheet' type='text/css'>
<link rel="icon" href="/SAMERP/config/img/icons/favicon.ico" type="image/x-icon">

<style>
#snackbar {
    visibility: hidden;
    min-width: 250px;
    margin-left: -125px;
    background-color: #333;
    color: #fff;
    text-align: center;
    border-radius: 2px;
    padding: 16px;
    position: fixed;
    z-index: 1;
    left: 50%;
    top: 50px;
    font-size: 15px;
    border-radius:50px 50px;
}

#snackbar.show {
    visibility: visible;
    -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
    animation: fadein 0.5s, fadeout 0.5s 2.5s;
}

@-webkit-keyframes fadein {
    from {top: 0; opacity: 0;} 
    to {top: 50px; opacity: 1;}
}

@keyframes fadein {
    from {top: 0; opacity: 0;}
    to {top: 50px; opacity: 1;}
}

@-webkit-keyframes fadeout {
    from {top: 50px; opacity: 1;} 
    to {top: 0; opacity: 0;}
}

@keyframes fadeout {
    from {top: 50px; opacity: 1;}
    to {top: 0; opacity: 0;}
}
.table td {
   text-align: center;   
}
</style>
</head>

<body onload="setFocusToTextBox()">

	<!--Header-part-->
	<div id="header">
		<h1>
			<a href="dashboard.html">Matrix Admin</a>
		</h1>
	</div>


	<!--start-top-serch-->
	<div id="search">
		<% if(request.getAttribute("status")!=null){ %>
		<div id="snackbar"><%=request.getAttribute("status")%></div>
		<%} %>

		<button type="submit" class="tip-bottom" style="margin-top: -1px;">LOGOUT</button>
	</div>
	<!--close-top-serch-->
	<!--sidebar-menu-->
	<jsp:include page="../common/left_navbar.jsp"></jsp:include>
	<!--sidebar-menu-->

	<!--main-container-part-->
	<div id="content">
		<!--breadcrumbs-->
		<div id="content-header">
			<div id="breadcrumb">
				<a href="index.html" class="tip-bottom"
					data-original-title="Go to Home"><i class="icon-home"> </i>
					Home</a> <a href="#" class="current">Add Employee</a>
			</div>
		</div>
		<!--End-breadcrumbs-->
		<div class="container-fluid">
			<hr>
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon"> <i class="icon-align-justify"></i>
							</span>
							<h5>Add Employee</h5>
						</div>
						<%
							RequireData rq = new RequireData();
						%>
						<div class="widget-content nopadding">
							<form action="/SAMERP/AddEmployee" method="post"
								class="form-horizontal">

								<div class="control-group">
									<label class="control-label">Date :</label>
									<div class="controls">
										<%
											RequireData rd = new RequireData();
											SysDate sd = new SysDate();
											String[] sdDemo = sd.todayDate().split("-");
										%>
										<input name="date" type="date"	value="<%=sdDemo[2] + "-" + sdDemo[1] + "-" + sdDemo[0]%>" class="span5">
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label"><span style="color: red;"></span>
										Employee Name :</label>
									<div class="controls">
										<input type="text" name="employee_name" id="emp_name" class="span5" placeholder="Employee Name"
											onkeyup="this.value=this.value.toUpperCase()" pattern="[a-z A-Z]*" required />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span style="color: red;"></span>Contact
										No. :</label>
									<div class="controls">
										<input type="text" name="contact_no" class="span5"
											placeholder="Contact Number"
											onkeypress="return isNumber(event)" pattern="[0-9]*" maxlength="10" required />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Designation: </label>
									<div class="controls">
										
											<select name="designation" class="span3" style="width: 356px;" >
											<option value="Driver">Driver</option>
											<option value="Helper">Helper</option>
											<option value="Home Worker">Home Worker</option>
											<option valu="Laber">Laber</option>
											
											</select>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label"><span style="color: red;"></span>Work
										With:</label>

									<div class="controls">
										<select name="contractorVehicle_name" class="span5">

											<%
												List details = rq.getContractorVehicle();
												if (details != null) {
													Iterator itr = details.iterator();
													while (itr.hasNext()) {
														String id = itr.next().toString();
														String alias_name = itr.next().toString();
											%>
											<option value="<%=id%>"><%=alias_name%></option>
											<%
												}
											%>
										</select>
									</div>
									<%
										}
									%>
								</div>

								<div class="form-actions">								
									<button type="submit" name="submit"	class="btn btn-success"	style="position: relative; right: 685px; float: right;">Submit</button>
									<a href="/SAMERP/dashboard.jsp"><button type="button" class="btn btn-danger "
											style="position: relative; right: 550px; float: right;">Exit</button></a>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>


			<div class="widget-box">
				<div class="widget-title">
					<span class="icon"> <i class="icon-th"></i>
					</span>
					<h5>Employee Details</h5>						
				</div>
				<div class="widget-content nopadding">
					<table class="table table-bordered data-table">
						<thead>
							<tr>
								<th>Sr.No.</th>
								<th>Date</th>
								<th>Employee Name</th>
								<th>Contact No</th>
								<th>Debtor_Id</th>
								<th>Designation</th>
								<th>AliasName</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
						<%
						int id=0,count=1;
						List getEmployeeList=rq.getEmployeeData();
						if(getEmployeeList!=null)
						{
							Iterator itr=getEmployeeList.iterator();
							while(itr.hasNext())
							{
								
								String empid=itr.next().toString();
														
						%>
						<tr>
							<td id="<%=empid%>"><%=count %></td>
							<td ><%=itr.next()%></td>
							<td ><%=itr.next()%></td>
							<td ><%=itr.next() %></td>							
							<td ><%=itr.next() %></td>							
							<td><%=itr.next() %></td>
							<td ><%=itr.next() %></td>
							<td><a href="#update_employee"
										data-toggle="modal" onclick="searchEmpolyee(<%=empid%>)"><i class="icon-pencil"></i></a>
									</td>
							
						</tr>
						<%
							count++;
							}
						}
						%>
							
						</tbody>
					</table>
				</div>
			</div>
			</div>
		</div>
	
		<div class="modal hide fade" id="update_employee" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Update Employee Details</h4>
				</div>
				<div class="modal-body">
					<form action="/SAMERP/AddEmployee" method="post" class="form-horizontal">

						<div class="control-group">
							<label class="control-label">Date :</label>
							<div class="controls">
								<%
									RequireData rd1 = new RequireData();
									SysDate sd1 = new SysDate();
									String[] sdDem = sd1.todayDate().split("-");
								%>
								<input name="date" id="dateid" type="date"	value="<%=sdDem[2] + "-" + sdDem[1] + "-" + sdDem[0]%>" class="span3">
							</div>
						</div>
						<div class="control-group">									
									<label class="control-label"><span style="color: red;">*</span>
										Employee Name :</label>
									<div class="controls">
										<input type="hidden" id="Updateid" name="Updateid" />
									<input type="text" name="employee_name" id="employeename" class="span3" autofocus placeholder="Employee Name"
											onkeyup="this.value=this.value.toUpperCase()" pattern="[a-z A-Z]*" required />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label"><span style="color: red;">*</span>Contact
										No. :</label>
									<div class="controls">
										<input type="text" name="contact_no" id="contactno" class="span3" 
											placeholder="Contact Number"
											onkeypress="return isNumber(event)" pattern="[0-9]*" maxlength="10" required />
									</div>
								</div>
								
									<div class="control-group">
									<label class="control-label">Designation: </label>
									<div class="controls">
										
											<select name="designation" style="width: 271px;" id="designationid" >
											<option value="Driver">Driver</option>
											<option value="Helper">Helper</option>
											<option value="Home Worker">Home Worker</option>
											<option valu="Laber">Laber</option>
																					
											</select>
									</div>
								</div>
									
									<div class="control-group">
									<label class="control-label"><span style="color: red;">*</span>Work
										With:</label>

									<div class="controls">
										<select name="contractorVehicle_alias" id="contractor_vehicle" class="span3">

											<%
												List detail = rq.getContractorVehicle();
												if (details != null) {
													Iterator itr = detail.iterator();
													while (itr.hasNext()) {
														String id1 = itr.next().toString();
														String alias_name = itr.next().toString();
											%>
											<option value="<%=id1%>"><%=alias_name%></option>
											<%
												}
											%>
										</select><input type="hidden" id="old_contractor_vehicle" name="old_contractor_vehicle">
									</div>
									<%
										}
									%>
								</div>
								
								<div class="control-group">
									<label class="control-label">Designation: </label>
									<div class="controls">
										
											<select name="designation" style="width: 271px;" id="designationid" >
											<option value="Driver">Driver</option>
											<option value="Helper">Helper</option>
											<option value="Home Worker">Home Worker</option>
											<option valu="Laber">Laber</option>
																					
											</select>
									</div>
								</div>
							
								<div class='modal-footer' >
							<button type="submit" name="update" class="btn btn-success">Update</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
						</div>
							</form>
				</div>

			</div>
		</div>
	</div>



	<!--Footer-part-->

	<div class="row-fluid">
		<div id="footer" class="span12">
			2017 &copy; Vertical Software. <a
				href="http://verticalsoftware.co.in">www.verticalsoftware.in</a>
		</div>
	</div>

	<!--end-Footer-part-->

<script>
function myFunction() {
    var x = document.getElementById("snackbar")
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}


function setFocusToTextBox() {
	document.getElementById("emp_name").focus();
	myFunction();
	showModal();
}

function showModal(){
	var someVarName = localStorage.getItem("someVarName");
		
	if(someVarName>0)
		{
			$('#myModal').modal('show');
		}
	localStorage.setItem('someVarName', null);
}

function searchEmpolyee(id) {
	
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			
			var demoStr = this.responseText.split(",");
			document.getElementById("Updateid").value = demoStr[0];
			document.getElementById("dateid").value = demoStr[1];
			document.getElementById("employeename").value = demoStr[2];
			//alert(demoStr[1]);
			document.getElementById("contactno").value = demoStr[3];
			document.getElementById("contractor_vehicle").value = demoStr[4];
			document.getElementById("old_contractor_vehicle").value = demoStr[5];
			document.getElementById("designationid").value = demoStr[6];	
			
			var desig=document.getElementById("designationid");
		
			for (var i = 0; i < desig.options.length; i++) {
			    if (desig.options[i].text === demoStr[5]) {
			    	desig.selectedIndex = i;
			        //alert(demoStr[4]);
			        getSetSelect('s2id_updatecontractorname', demoStr[5]);
			        break;
			    }
			}
			
			}
		};
	xhttp.open("POST","/SAMERP/AddEmployee?employeeid="+id, true);
	xhttp.send();
}

function getSetSelect(id,value)
{
	var x=document.getElementById(id).children;
	var xx=x[0].children;
	xx[0].innerHTML=value;
}
</script>
	<script src="/SAMERP/config/js/excanvas.min.js"></script>
	<script src="/SAMERP/config/js/jquery.min.js"></script>
	<script src="/SAMERP/config/js/jquery.ui.custom.js"></script>
	<script src="/SAMERP/config/js/bootstrap.min.js"></script>
	<script src="/SAMERP/config/js/jquery.flot.min.js"></script>
	<script src="/SAMERP/config/js/jquery.flot.resize.min.js"></script>
	<script src="/SAMERP/config/js/jquery.peity.min.js"></script>
	<script src="/SAMERP/config/js/fullcalendar.min.js"></script>
	<script src="/SAMERP/config/js/matrix.js"></script>
	<script src="/SAMERP/config/js/matrix.dashboard.js"></script>
	<script src="/SAMERP/config/js/jquery.gritter.min.js"></script>
	<script src="/SAMERP/config//SAMERP/config/js/matrix.interface.js"></script>
	<script src="/SAMERP/config/js/matrix.chat.js"></script>
	<script src="/SAMERP/config/js/jquery.validate.js"></script>
	<script src="/SAMERP/config/js/matrix.form_validation.js"></script>
	<script src="/SAMERP/config/js/jquery.wizard.js"></script>
	<script src="/SAMERP/config/js/jquery.uniform.js"></script>
	<script src="/SAMERP/config/js/select2.min.js"></script>
	<script src="/SAMERP/config/js/matrix.popover.js"></script>
	<script src="/SAMERP/config/js/jquery.dataTables.min.js"></script>
	<script src="/SAMERP/config/js/matrix.tables.js"></script>
</body>
</html>
