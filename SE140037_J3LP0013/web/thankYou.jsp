<%-- 
    Document   : thankYou
    Created on : Jan 12, 2021, 11:23:22 PM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hana Shop - Thank you to you</title>
    </head>
    <body>
        <!-- redirect if admin -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.ROLE eq 'Admin'}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <c:if test="${sessionScope.ORDER_INFO == null}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <!-- navbar brand -->
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <!-- left navbar -->
            <div class="collapse navbar-collapse ">
                <div class="navbar-nav">
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                </div>
            </div>
            <!-- right navbar -->
            <<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav">
                    <!-- customer part -->
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <!-- view cart -->
                        <li class="nav-item"><a class="nav-link" href="cart.jsp">View Cart <c:if test="${sessionScope.CART != null}">
                                    <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                                </c:if></a></li>
                    </c:if>
                    <!-- USER mean login user -->
                    <c:if test="${sessionScope.USER != null}">
                        <li class="nav-item dropdown">
                            <!-- show fullname -->
                            <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                            <!-- dropdown menu -->
                            <ul class="dropdown-menu">
                                <!-- customer part -->
                                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                    <!-- history -->
                                    <c:url var="historyLink" value="DispatchController">
                                        <c:param name="action" value="History"/>
                                    </c:url>
                                    <li><a class="dropdown-item" href="${historyLink}">History</a></li>
                                </c:if>
                                    <!-- logout -->
                                    <c:url var="logoutLink" value="DispatchController">
                                        <c:param name="action" value="Logout"/>
                                    </c:url>
                                <li><a class="dropdown-item text-danger" href="${logoutLink}">Logout</a></li>
                            </ul>
                        </li>
                    </c:if>
                    <!-- login -->
                    <c:if test="${sessionScope.USER == null}">
                        <li class="nav-item"><a class="nav-link mr-4 " href="login.jsp">Login</a></li>
                    </c:if>
                </ul> 
            </div>
        </nav>
        <!-- container -->
        <div class="container-fluid" style="padding-top: 80px;">
            <div class="text-center">
                <h1>Thank you for your order!</h1>
                <p class="lead">Thank you. Your order has been received.</p>
            </div>
            <!-- show order info -->
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-5">
                        <div class="card d-flex flex-column border-2 border-secondary bg-light">
                            <div class="card-body">
                                <c:set value="${sessionScope.ORDER_INFO}" var="order"/>
                                <div class="mt-2">
                                    <p class="font-weight-light text-dark">${sessionScope.FULLNAME} - ${order.phone} - ${order.address} </p>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">DATE:</p>
                                    <p class="font-weight-bold">${order.createdDate}</p>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">TOTAL:</p>
                                    <p class="font-weight-bold">$${order.totalPrice}</p>
                                </div>
                                <hr class="my-2">
                                <div>
                                    <p class="text-muted" style="font-size: 10px;">PAYMENT METHOD:</p>
                                    <p class="font-weight-bold">${order.paymentMethod}</p>
                                </div>
                            </div>
                        </div>
                        <p class="lead justify-content-center mt-4">
                            <c:url var="homeLink" value="LoadHomeController"/>
                            <a class="btn btn-primary w-100" href="${homeLink}" role="button">Continue to homepage</a>
                        </p>    
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<!-- footer -->
<footer class="footer text-center text-lg-start">
    <!-- Copyright -->
    <div class="text-center p-3">
        © 2020 Copyright:
        <c:url var="homeLink" value="${DispatchController}"/>
        <a class="text-info" href="${homeLink}">Hana Shop</a>
    </div>
    <!-- Copyright -->
</footer>
