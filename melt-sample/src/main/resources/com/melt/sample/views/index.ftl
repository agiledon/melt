<!DOCTYPE html>
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Melt - Sample</title>
	<link rel="stylesheet" href="/assets/css/blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="/assets/css/blueprint/print.css" type="text/css" media="print">
    <link rel="stylesheet" href="/assets/css/jquery-ui.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="/assets/css/custom.css">
    <script src="/assets/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="/assets/js/jquery-ui.js" type="text/javascript"></script>
    <script src="/assets/js/jquery.tmpl.min.js" type="text/javascript"></script>
  </head>
  <body>
    <div class="header-wrapper">
        <header class="container clearfix">
            <h1 class="logo">Melt</h1>
            <span class="description">Melt Sample</span>
        </header>
	</div>
	<section class="container">
	    <h2>Customers</h2>
	    <table>
	        <tr>
                <th>Name</th>
                <th>Age</th>
                <th>Type</th>
                <th>Edit</th>
                <th>Delete</th>
                <th>Orders</th>
	        </tr>
        <#list customers as customer>
             <tr>
                <td>${customer.name}</td>
                <td>${customer.age}</td>
                <td><#if customer.customerType??> ${customer.customerType} </#if></td>
                <td><a href="/customer/${customer.id}">Edit</a></td>
                <td><a href="/customer/delete/${customer.id}">Delete</a></td>
                <td><a href="/customer/orders/${customer.id}">Orders</a></td>
             </tr>
        </#list>
        </table>
        <a href="/customer/add">Add New Customer</a>
	</section>
	<footer class="container">
	</footer>
  </body>
</html>