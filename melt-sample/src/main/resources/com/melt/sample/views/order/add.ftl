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
	    <h2>Add Order</h2>
        <form id="form" action="/customer/${customerId}/order/add" method="POST">
            <label for="count">Count:</label>
            <input type="text" name="count" id="count"/>
            <br/>
            <label for="discount">Discount:</label>
            <input type="text" name="discount" id="discount"/>
            <br/>
            <label for="sent">Sent:</label>
            <input type="radio" name="sent" value="true"/>Yes
            <input type="radio" name="sent" value="false"/>No
            <br/>
            <label for="orderAddress">Order Address:</label>
            <input type="text" name="orderAddress" id="orderAddress"/>
            <br/>
            <label for="itemPrices">Item Prices:</label>
            <input type="text" name="itemPrices" id="itemPrices"/>
            <br/>
            <input type="submit" value="Add"/>
        </form>
        <br/>
        <a href="/customer/orders/${customerId}">Back</a>
	</section>
	<footer class="container">
	</footer>
  </body>
</html>