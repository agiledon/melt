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
	    <h2>Items</h2>
	    <table>
	        <tr>
                <th>Price</th>
                <th>Edit</th>
                <th>Delete</th>
	        </tr>
        <#list items as item>
             <tr>
                <td>${item.price}</td>
                <td><a href="">Edit</a></td>
                <td><a href="">Delete</a></td>
                <td><a href="">Items</a></td>
             </tr>
        </#list>
        </table>
        <a href="/customer/${customer.id}/order/add">Add New Order</a>
        <a href="/index.html">Back</a>

	</section>
	<footer class="container">
	</footer>
  </body>
</html>