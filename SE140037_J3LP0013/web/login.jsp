<%-- 
    Document   : login
    Created on : Jan 4, 2021, 10:31:08 PM
    Author     : TrongNS
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hana Shop - Login Page</title>
        <style type="text/css">
            body {
                background-image: url(./image/background-login.jpeg);
                background-color: #fff;
                background-position: center;
                background-repeat: no-repeat;
                background-size: cover;
            }
        </style>
    </head>
    <body>
        <!-- Redirect if admin or already login -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.USER != null}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <div class="collapse navbar-collapse ">
                <div class="navbar-nav">
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                </div>
            </div>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav">
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <c:url var="cartLink" value="cart.jsp">
                            <c:param name="txtSearchProduct" value="${param.txtSearchProduct}"/>
                            <c:param name="currentPage" value="${requestScope.currentPage}"/>
                        </c:url>
                        <li class="nav-item"><a class="nav-link" href="${cartLink}">View Cart <c:if test="${sessionScope.CART != null}">
                                    <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                                </c:if></a></li>
                            </c:if>
                            <c:if test="${sessionScope.USER == null}">
                                <c:url var="loginLink" value="login.jsp">
                                    <c:param name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                    <c:if test="${not empty requestScope.pageNo}">
                                        <c:param name="pageNo" value="${requestScope.pageNo}" />
                                    </c:if>
                                    <c:param name="cbCategory" value="${param.cbCategory}" />
                                    <c:param name="txtFromPrice" value="${param.txtFromPrice}" />
                                    <c:param name="txtToPrice" value="${param.txtToPrice}" />
                                </c:url>
                        <li class="nav-item"><a class="nav-link mr-4 " href="${loginLink}">Login</a></li>
                        </c:if>
                </ul> 
            </div>
        </nav>
        <!-- container -->
        <div class="container w-40 d-flex flex-column align-items-center" style="padding-top: 80px;">
            <div class="card bg-white shadow-lg w-50 mx-auto">
                <div class="card-header">
                    <h4 class="text-center">Login</h4>
                </div>
                <!-- error -->
                <c:if test="${requestScope.INVALID != null}">
                    <div class="ml-2">
                        <font color="red">
                        ${requestScope.INVALID}
                        </font>
                    </div>
                </c:if>
                <!-- login card -->
                <div class="card-body">
                    <form action="DispatchController" method="POST">
                        <div class="form-group">
                            <label for="txtUsername">
                                Username:
                            </label>
                            <input class="form-control" type="text" name="txtUsername" minlength="1" maxlength="30" value="${param.txtUsername}" placeholder="Username" required="required"/>
                        </div>
                        <div class="form-group">
                            <label for="txtPassword">
                                Password:
                            </label>
                            <input class="form-control" type="password" minlength="1" maxlength="50" name="txtPassword" value="${param.txtPassword}" placeholder="Password" required="required"/>
                        </div>
                        <div class="d-flex justify-content-center">
                            <c:set var="goBackPage" value="${param.goBackPage}"/>
                            <c:if test="${goBackPage eq 'home'}">
                                <input type="hidden" name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                <c:if test="${not empty param.pageNo}">
                                    <input type="hidden" name="pageNo" value="${requestScope.pageNo}" />
                                </c:if>
                                <input type="hidden" name="cbCategory" value="${param.cbCategory}" />
                                <input type="hidden" name="txtFromPrice" value="${param.txtFromPrice}" />
                                <input type="hidden" name="txtToPrice" value="${param.txtToPrice}" />
                            </c:if>
                            <input type="hidden" name="goBackPage" value="${goBackPage}"/>
                            <input type="submit" value="Login" name="action" class="btn btn-primary mb-3 mr-1" />
                            <input type="reset" class="btn btn-secondary mb-3" value="Reset" />
                        </div>
                    </form>
                    <hr class="mt-1 mb-4">
                    <!-- Login with google -->
                    <a href="https://accounts.google.com/o/oauth2/auth?scope=https://www.googleapis.com/auth/userinfo.profile&redirect_uri=http://localhost:8084/SE140037_J3LP0013/LoginGoogleController&response_type=code
                       &client_id=963258687346-hku1b3ha2rlohiji95q2uk11e7hi0q02.apps.googleusercontent.com&approval_prompt=force" class="btn btn-block" style="background-color: #dd4b37; color: white;">
                        <i class="fa fa-google"></i>
                        Log in with Google   
                    </a>
                </div>
            </div>
        </div
    </body>
</html>
<footer class="footer text-center text-lg-start fixed-bottom">
    <!-- Copyright -->
    <div class="text-center p-3">
        © 2020 Copyright:
        <c:url var="homeLink" value="${DispatchController}"/>
        <a class="text-info" href="${homeLink}">Hana Shop</a>
    </div>
    <!-- Copyright -->
</footer>
