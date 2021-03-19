<%-- 
    Document   : createNewProduct
    Created on : Jan 7, 2021, 11:37:18 AM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <script src="bootstrap/js/popper.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hana Shop - Create New Product</title>
    </head>
    <body>
        <!-- redirect if not admin -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.ROLE ne 'Admin'}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <!-- navbar brand -->
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <!-- left navbar -->
            <div class="collapse navbar-collapse ">
                <div class="navbar-nav">
                    <!-- home -->
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                    <c:url value="DispatchController" var="createProductLink">
                        <c:param name="action" value="Create New Product"/>
                    </c:url>
                    <a class="bg-light ml-1 nav-link nav-item text-body" href="${createProductLink}">Create New Product</a>
                </div>
            </div>
            <!-- right navbar -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav">
                    <!-- Login user part -->
                    <li class="nav-item dropdown">
                        <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                        <ul class="dropdown-menu">
                            <!-- Logout -->
                            <c:url var="logoutLink" value="DispatchController">
                                <c:param name="action" value="Logout"/>
                            </c:url>
                            <li><a class="dropdown-item text-danger" href="${logoutLink}">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
        <!-- container -->
        <div class="container mt-4 w-40 d-flex flex-column align-items-center" style="padding-top: 80px;">
            <div class="card">
                <!-- header -->
                <div class="card-header text-center">
                    <h4>Create New Product</h4>
                </div>
                <!-- footer -->
                <div class="card-body">
                    <!-- error -->
                    <c:if test="${not empty requestScope.createProductMsg}">
                        <font color="red">
                        ${requestScope.createProductMsg}
                        </font>
                    </c:if>
                    <!-- form -->
                    <form action="CreateNewProductController" method="POST" enctype="multipart/form-data">
                        <c:set var="errors" value="${requestScope.createProductErrors}"/>
                        <!-- product id -->
                        <div class="form-group">
                            Product ID: <font color="red">*</font> <input class="form-control" type="text" minlength="1" maxlength="15" name="txtProductId" value="${requestScope.txtProductId}" placeholder="Product ID" required="required"/>
                            <c:if test="${not empty errors.duplicateProductId}">
                                <font color="red">
                                ${errors.duplicateProductId}
                                </font>
                            </c:if>
                        </div>
                        <!-- product name -->
                        <div class="form-group">
                            Product name: <font color="red">*</font> <input class="form-control" type="text" minlength="1" maxlength="100" name="txtProductName" value="${requestScope.txtProductName}" placeholder="Product name" required="required"/>
                        </div>
                        <!-- image -->
                        <div class="form-group">
                            Select Image: <font color="red">*</font> <input type="file" class="form-control form-control-file" name="imageFileUp" accept="image/*" value=""/>
                            <input type="text" class="form-control" name="txtImageLink" value="${requestScope.txtImageLink}" placeholder="Image link"/>
                            <c:if test="${not empty errors.invalidImageLink}">
                                <font color="red">
                                ${errors.invalidImageLink}
                                </font>
                            </c:if>
                        </div>
                        <!-- quantity -->
                        <div class="form-group">
                            Quantity: <font color="red">*</font> <input class="form-control" type="text" name="txtQuantity" value="${requestScope.txtQuantity}" placeholder="Quantity" pattern="\d+" required="required" oninvalid="setCustomValidity('Please enter only positive number value')" oninput="setCustomValidity('')"/>
                        </div>
                        <!-- description -->
                        <div class="form-group">
                            Description: <textarea class="form-control" name="txtDescription" placeholder="Description">${requestScope.txtDescription}</textarea>
                        </div>
                        <!-- price -->
                        <div class="form-group">
                            Price: <font color="red">*</font> <input class="form-control" type="text" name="txtPrice" value="${requestScope.txtPrice}" placeholder="$ Price" pattern="^[0-9]*[.]?[0-9]*$" required="required" oninvalid="setCustomValidity('Please enter only positive number value for price')" oninput="setCustomValidity('')"/>
                        </div>
                        <!-- category -->
                        <div class="form-group">
                            Category: <select name="cbCategory" class="form-control">
                                <c:forEach items="${requestScope.categoryList}" var="ctg">
                                    <option value="${ctg.name}" <c:if test="${ctg.name == requestScope.cbCategory}">selected</c:if>>${ctg.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <hr>
                        <div class="d-flex flex-column justify-content-center w-100 mt-3">
                            <input type="submit" value="Create New Product" name="action" class="btn btn-primary mb-3 mr-1" />
                            <input type="reset" class="btn btn-secondary mb-3" value="Reset" />
                        </div>
                    </form>
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
