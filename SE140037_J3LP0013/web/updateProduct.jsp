<%-- 
    Document   : updateProduct
    Created on : Jan 16, 2021, 2:42:17 PM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="bootstrap/js/jquery-3.4.1.slim.min.js" type="text/javascript"></script>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/myCSS.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <!-- redirect if not admin -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.ROLE ne 'Admin'}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <c:if test="${sessionScope.USER == null}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <c:if test="${requestScope.UPDATED_PRODUCT == null}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <!-- navbar brand -->
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <!-- left navbar -->
            <div class="collapse navbar-collapse">
                <div class="navbar-nav">
                    <!-- home -->
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                    <!-- create new product -->
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
                    <li class="nav-item dropdown">
                        <!-- show fullname -->
                        <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                        <!-- dropdown menu -->
                        <ul class="dropdown-menu">
                            <!-- logout -->
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
        <div class="container-fluid" style="padding-top: 80px;">
            <!-- return to search page -->
            <div class="mb-2 ml-3">
                <c:url var="returnLink" value="SearchProductController">
                    <c:if test="${requestScope.txtSearchProduct != null}">
                        <c:param name="txtSearchProduct" value="${requestScope.txtSearchProduct}" />
                        <c:if test="${not empty requestScope.pageNo}">
                            <c:param name="pageNo" value="${requestScope.pageNo}" />
                        </c:if>
                        <c:param name="cbCategory" value="${requestScope.cbCategory}" />
                        <c:param name="txtFromPrice" value="${requestScope.txtFromPrice}" />
                        <c:param name="txtToPrice" value="${requestScope.txtToPrice}" />
                    </c:if>
                    <c:if test="${requestScope.txtSearchProduct == null}">
                        <c:param name="txtSearchProduct" value="${param.txtSearchProduct}" />
                        <c:if test="${not empty param.pageNo}">
                            <c:param name="pageNo" value="${param.pageNo}" />
                        </c:if>
                        <c:param name="cbCategory" value="${param.cbCategory}" />
                        <c:param name="txtFromPrice" value="${param.txtFromPrice}" />
                        <c:param name="txtToPrice" value="${param.txtToPrice}" />
                    </c:if>
                </c:url>
                <a href="${returnLink}" class="btn btn-outline-secondary" style="width: 100px;">Return</a>
            </div>
            <!-- errors -->
            <div class="my-2">
                <c:if test="${not empty requestScope.invalidImageLink}">
                    <font color="red">
                    ${requestScope.invalidImageLink}
                    </font>
                </c:if>
                <c:if test="${not empty requestScope.updateProductMsg}">
                    <c:set var="msg" value="${requestScope.updateProductMsg}"/>
                    <c:if test="${fn:contains(msg, 'successfully')}">
                        <font color="green">
                        ${requestScope.updateProductMsg}
                        </font>
                    </c:if>
                    <c:if test="${fn:contains(msg, 'fail')}">
                        <font color="red">
                        ${requestScope.updateProductMsg}
                        </font>
                    </c:if>
                </c:if>
            </div>
            <!-- show updated product -->
            <div class="row">
                <div class="col-md-12">
                    <div id="collapseTable">
                        <table class="table" border="1">
                            <thead>
                                <tr>
                                    <th>Product Image</th>
                                    <th>Product Name</th>
                                    <th>Description</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Created Date</th>
                                    <th>Category</th>
                                    <th>Status</th>
                                    <th>Update</th>
                                </tr>
                            </thead>
                            <tbody>
                            <form action="UpdateProductController" method="POST" enctype="multipart/form-data">
                                <c:set var="dto" value="${requestScope.UPDATED_PRODUCT}"/>
                                <tr>
                                    <td> <!-- image -->
                                        <input type="file" class="form-control form-control-file" name="imageFileUp" accept="image/*" value=""/>
                                        <input type="text" class="form-control" name="txtImageLink" value="${dto.imageLink}" placeholder="Image link" required="required"/>
                                    </td>
                                    <td> <!-- product id -->
                                        <input type="hidden" name="txtProductId" value="${dto.id}" />
                                        <!-- product name -->
                                        <input type="text" class="form-control" name="txtProductName" minlength="1" maxlength="100" value="${dto.name}" placeholder="Product name" required="required"/>
                                    </td>
                                    <td>
                                        <!-- description -->
                                        <textarea name="txtDescription" class="form-control">${dto.description}</textarea>
                                    </td>
                                    <td> <!-- quantity -->
                                        <input type="text" name="txtQuantity" value="${dto.quantity}" placeholder="Quantity" pattern="\d+" required="required" oninvalid="setCustomValidity('Please enter only positive number value')" oninput="setCustomValidity('')"/>
                                    </td>
                                    <td> <!-- price -->
                                        <input type="text" name="txtPrice" min="0" max="1000" value="${dto.price}" placeholder="Price" pattern="^[0-9]*[.]?[0-9]*$" required="required" oninvalid="setCustomValidity('Please enter only positive number value for price')" oninput="setCustomValidity('')"/>
                                    </td>
                                    <td> <!-- created date -->
                                        ${dto.createdDate}
                                    </td>
                                    <td> <!-- category -->
                                        <select name="cbCtgory">
                                            <c:forEach items="${requestScope.categoryList}" var="ctg">
                                                <option value="${ctg.name}" <c:if test="${ctg.name == dto.category}">selected</c:if>>${ctg.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td> <!-- status -->
                                        <select name="cbActive">
                                            <option value="Active" <c:if test="${dto.active}">selected</c:if>>Active</option>
                                            <option value="Inactive" <c:if test="${not dto.active}">selected</c:if>>Inactive</option>
                                            </select>
                                        </td>
                                        <td>
                                        <input type="hidden" name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                        <c:if test="${not empty param.pageNo}">
                                            <input type="hidden" name="pageNo" value="${param.pageNo}" />
                                        </c:if>
                                        <input type="hidden" name="cbCategory" value="${param.cbCategory}" />
                                        <input type="hidden" name="txtFromPrice" value="${param.txtFromPrice}" />
                                        <input type="hidden" name="txtToPrice" value="${param.txtToPrice}" />
                                        <input type="submit" name="action" value="Update" class="btn btn-block btn-success" />
                                    </td>
                                </tr>
                            </form>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<!-- footer -->
<footer class="footer text-center text-lg-start fixed-bottom">
    <!-- Copyright -->
    <div class="text-center p-3">
        © 2020 Copyright:
        <c:url var="homeLink" value="${DispatchController}"/>
        <a class="text-info" href="${homeLink}">Hana Shop</a>
    </div>
    <!-- Copyright -->
</footer>

