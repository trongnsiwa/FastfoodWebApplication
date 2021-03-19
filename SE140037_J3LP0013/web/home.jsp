<%-- 
    Document   : home
    Created on : Jan 4, 2021, 10:42:22 PM
    Author     : TrongNS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
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
        <title>Hana Shop - Home Page</title>
    </head>
    <body>
        <!-- css part -->
        <style>
            .table-responsive {
                display: table;
            }

            .table td, th {
                text-align: center;
                vertical-align: middle;
            }

            .price-wrap {
                line-height: 1.2;
            }

            img {
                width: 170px;
                height: 170px;
                object-fit: cover;
            }

            #collapseTable {
                overflow-x: scroll;
            }
        </style>
        <!-- navnbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <c:url value="LoadHomeController" var="homeLink"/>
            <!-- Brand Name -->
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <!-- Left navbar -->
            <div class="collapse navbar-collapse">
                <div class="navbar-nav">
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                    <c:if test="${sessionScope.ROLE eq 'Admin'}">
                        <c:url value="DispatchController" var="createProductLink">
                            <c:param name="action" value="Create New Product"/>
                        </c:url>
                        <a class="bg-light ml-1 nav-link nav-item text-body" href="${createProductLink}">Create New Product</a>
                    </c:if>
                </div>
            </div>
            <!-- right navbar -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav">
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <!-- View Cart -->
                        <li class="nav-item"><a class="nav-link" href="cart.jsp">View Cart <c:if test="${sessionScope.CART != null}">
                                    <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                                </c:if></a></li>
                            </c:if>
                    <!-- Login user part -->
                    <c:if test="${sessionScope.USER != null}">
                        <li class="nav-item dropdown">
                            <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                            <ul class="dropdown-menu">
                                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                    <!-- History -->
                                    <c:url var="historyLink" value="DispatchController">
                                        <c:param name="action" value="History"/>
                                    </c:url>
                                    <li><a class="dropdown-item" href="${historyLink}">History</a></li>
                                    </c:if>
                                <!-- Logout -->
                                <c:url var="logoutLink" value="DispatchController">
                                    <c:param name="action" value="Logout"/>
                                </c:url>
                                <li><a class="dropdown-item text-danger" href="${logoutLink}">Logout</a></li>
                            </ul>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.USER == null}">
                        <!-- Login -->
                        <c:url var="loginLink" value="login.jsp">
                            <c:param name="goBackPage" value="home"/>
                            <c:if test="${not empty param.txtSearchProduct 
                                          || not empty param.cbCategory 
                                          || not empty param.txtFromPrice 
                                          || not empty param.txtToPrice}">
                                <c:param name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                <c:if test="${not empty requestScope.pageNo}">
                                    <c:param name="pageNo" value="${requestScope.pageNo}" />
                                </c:if>
                                <c:param name="cbCategory" value="${param.cbCategory}" />
                                <c:param name="txtFromPrice" value="${param.txtFromPrice}" />
                                <c:param name="txtToPrice" value="${param.txtToPrice}" />
                            </c:if>
                        </c:url>
                        <li class="nav-item"><a class="nav-link mr-4 " href="${loginLink}">Login</a></li>
                        </c:if>
                </ul> 
            </div>
        </nav>
        <!-- container -->
        <div class="container" style="padding-top: 80px;">
            <!-- Welcome part -->
            <c:if test="${sessionScope.FULLNAME != null}">
                <h3 class="mb-4 text-success">
                    Welcome, ${sessionScope.FULLNAME}
                </h3>
            </c:if>
            <!-- Search product part --> 
            <form action="DispatchController" class="d-flex flex-row w-100">
                <div class="w-100 align-items-center justify-content-between">
                    <!-- search bar -->
                    <div class="form-group w-100 mb-1">
                        <i class="fa fa-search position-absolute" style="padding: 10px; min-width: 40px; "></i>
                        <input type="text" name="txtSearchProduct" value="${param.txtSearchProduct}" class="form-control w-100 align-content-center" style="padding-left: 35px;" placeholder="Search product name" />
                    </div>
                    <div class="w-100 mt-2 w-50 d-flex flex-row">
                        <!-- range of money -->
                        <div class="form-group text-center">
                            <p class="small mr-2 text-muted d-inline-block">Range Of Money: </p>
                            <input class="text-center form-control d-inline-block" type="text" name="txtFromPrice" value="${param.txtFromPrice}" pattern="^[0-9]*[.]?[0-9]*$" placeholder="$ From" style="width: 100px;" oninvalid="setCustomValidity('Please enter only number value')" oninput="setCustomValidity('')"/>
                            <input class="text-center form-control ml-2 d-inline-block" type="text" name="txtToPrice" value="${param.txtToPrice}" pattern="^[0-9]*[.]?[0-9]*$" placeholder="$ To" style="width: 100px;" oninvalid="setCustomValidity('Please enter only number value')" oninput="setCustomValidity('')"/>
                        </div>
                        <!-- category -->
                        <div class="form-group ml-2">
                            <select name="cbCategory" class="form-control">
                                <option value="">Select category</option>
                                <c:forEach items="${requestScope.categoryList}" var="dto">
                                    <option value="${dto.id}" <c:if test="${param.cbCategory == dto.id}">selected</c:if>>
                                        ${dto.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <!-- Search & reset button -->
                <div class="ml-2 align-items-center justify-content-between">
                    <input type="submit" class="btn btn-outline-success mr-1" value="Search" name="action" />
                    <input type="reset" class="btn btn-outline-secondary mt-1" value="Reset" />
                </div>
            </form>
            <c:if test="${not empty requestScope.RANGE_ERROR}">
                <font color="red">
                ${requestScope.RANGE_ERROR}
                </font>
            </c:if>
        </div>
        <!-- container part -->
        <div class="container-fluid">
            <!-- errors -->
            <c:if test="${not empty requestScope.productDeleteMsg}">
                <c:set var="msg" value="${requestScope.productDeleteMsg}"/>
                <c:if test="${fn:contains(msg, 'successfully')}">
                    <font color="green">
                    ${msg}
                    </font>
                </c:if>
                <c:if test="${fn:contains(msg, 'fail')}">
                    <font color="red">
                    ${msg}
                    </font>
                </c:if>
            </c:if>
            <c:if test="${not empty requestScope.createProductMsg}">
                <c:set var="msg" value="${requestScope.createProductMsg}"/>
                <c:if test="${fn:contains(msg, 'successfully')}">
                    <font color="green">
                    ${msg}
                    </font>
                </c:if>
                <c:if test="${fn:contains(msg, 'fail')}">
                    <font color="red">
                    ${msg}
                    </font>
                </c:if>
            </c:if>
            <!-- list product table -->
            <c:if test="${requestScope.listProducts != null}">
                <c:if test="${not empty requestScope.listProducts}">
                    <div class="row mt-4">
                        <div class="col-md-12">
                            <div id="collapseTable">
                                <table class="table" border="1">
                                    <thead>
                                        <tr>
                                            <th>No.</th>
                                            <th>Product Image</th>
                                            <th>Product Name</th>
                                            <th>Description</th>
                                            <th>Quantity</th>
                                            <th>Price</th>
                                            <th>Created Date</th>
                                            <th>Category</th>
                                                <c:if test="${sessionScope.ROLE eq 'Admin'}">
                                                <th>Status</th>
                                                <th>Update</th>
                                                <th>Delete</th>
                                                </c:if>
                                                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                                <th>Add To Cart</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <!-- Admin part -->
                                        <c:if test="${sessionScope.ROLE eq 'Admin'}">
                                        <form action="DispatchController" method="POST" id="submit-form">
                                            <tr>
                                                <td colspan="10"></td>
                                                <td>
                                                    <!-- Delete -->
                                                    <input type="hidden" name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                                    <c:if test="${not empty requestScope.pageNo}">
                                                        <input type="hidden" name="pageNo" value="${requestScope.pageNo}" />
                                                    </c:if>
                                                    <input type="hidden" name="cbCategory" value="${param.cbCategory}"/>
                                                    <input type="hidden" name="txtFromPrice" value="${param.txtFromPrice}"/>
                                                    <input type="hidden" name="txtToPrice" value="${param.txtToPrice}"/>
                                                    <input type="hidden" name="action" value="Delete"/>
                                                    <button type="button" class="btn btn-block btn-success" data-toggle="modal" id="btn-delete" data-targer="#confirm-delete">Delete</button>
                                                </td>
                                            </tr>
                                            <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title w-100 text-center text-danger">Confirm delete product</h5>
                                                        </div>
                                                        <div class="modal-body text-center">
                                                            Are you sure want to delete this product?
                                                        </div>
                                                        <div class="modal-footer d-flex justify-content-center">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                            <a id="btn-ok" class="btn btn-danger">Remove</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <c:forEach items="${requestScope.listProducts}" var="dto" varStatus="counter">
                                                <tr>
                                                    <td>
                                                        ${counter.count}
                                                    </td>
                                                    <td>
                                                        <c:url var="img" value="${dto.imageLink}"/>
                                                        <img src="${img}" class="img-fluid img-thumbnail" alt="${dto.imageLink}">
                                                    </td>
                                                    <td>
                                                        ${dto.name}
                                                    </td>
                                                    <td>
                                                        ${dto.description}
                                                    </td>
                                                    <td>
                                                        ${dto.quantity}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" class="tmpPrice" value="${dto.price}" />
                                                        <div class="price-wrap"><span class="price">${dto.price}</span></div>
                                                    </td>
                                                    <td>
                                                        ${dto.createdDate}
                                                    </td>
                                                    <td>
                                                        ${dto.category}
                                                    </td>
                                                    <td>
                                                        <c:if test="${dto.active}">Active</c:if>
                                                        <c:if test="${not dto.active}">Inactive</c:if>
                                                        </td>
                                                        <td>
                                                            <!-- Update link -->
                                                        <c:url var="updateLink" value="DispatchController">
                                                            <c:param name="txtProductId" value="${dto.id}" />
                                                            <c:param name="action" value="Load Updated Product" />
                                                            <c:param name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                                            <c:if test="${not empty requestScope.pageNo}">
                                                                <c:param name="pageNo" value="${requestScope.pageNo}" />
                                                            </c:if>
                                                            <c:param name="cbCategory" value="${param.cbCategory}" />
                                                            <c:param name="txtFromPrice" value="${param.txtFromPrice}" />
                                                            <c:param name="txtToPrice" value="${param.txtToPrice}" />
                                                        </c:url>
                                                        <a href="${updateLink}" class="btn btn-block btn-success">Update</a>
                                                    </td>
                                                    <td align="center">
                                                        <input type="checkbox" name="chkItem" class="chk-item" value="${dto.id}" <c:if test="${not dto.active}">onclick="return false;"</c:if>/>
                                                        </td>
                                                    </tr>
                                            </c:forEach>
                                        </form>
                                    </c:if>
                                    <!-- Customer part -->
                                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                        <c:forEach items="${requestScope.listProducts}" var="dto" varStatus="counter">
                                            <form action="DispatchController" method="POST">
                                                <tr>
                                                    <td>
                                                        ${counter.count}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtImageLink" value="${dto.imageLink}" />
                                                        <c:url var="img" value="${dto.imageLink}"/>
                                                        <img src="${img}" class="img-fluid img-thumbnail" alt="${dto.imageLink}">
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtProductId" value="${dto.id}" />
                                                        <input type="hidden" name="txtProductName" value="${dto.name}" />
                                                        ${dto.name}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtDescription" value="${dto.description}" />
                                                        ${dto.description}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtQuantity" value="${dto.quantity}" />
                                                        ${dto.quantity}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtPrice" value="${dto.price}" />
                                                        <input type="hidden" class="tmpPrice" value="${dto.price}" />
                                                        <div class="price-wrap"><span class="price">${dto.price}</span></div>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtCreatedDate" value="${dto.createdDate}" />
                                                        ${dto.createdDate}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtCategory" value="${dto.category}" />
                                                        ${dto.category}
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="txtSearchProduct" value="${param.txtSearchProduct}" />
                                                        <c:if test="${not empty requestScope.pageNo}">
                                                            <input type="hidden" name="pageNo" value="${requestScope.pageNo}" />
                                                        </c:if>
                                                        <input type="hidden" name="cbCategory" value="${param.cbCategory}"/>
                                                        <input type="hidden" name="txtFromPrice" value="${param.txtFromPrice}"/>
                                                        <input type="hidden" name="txtToPrice" value="${param.txtToPrice}"/>
                                                        <!-- Add to cart -->
                                                        <input type="submit" class="btn btn-block btn-success" value="Add To Cart" name="action" />
                                                    </td>
                                                </tr>
                                            </form>
                                        </c:forEach> 
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- Paging -->
                        <div class="d-flex w-100 justify-content-center mt-3 mb-4">
                            <c:if test="${not empty requestScope.pageNo}">
                                <c:set var="pageNo" value="${requestScope.pageNo}" />
                                <c:if test="${pageNo != 1}">
                                    <c:url var="prevLink" value="SearchProductController">
                                        <c:param name="txtSearchProduct" value="${param.txtSearchProduct}"/>
                                        <c:param name="pageNo" value="${pageNo}"/>
                                        <c:param name="cbCategory" value="${param.cbCategory}"/>
                                        <c:param name="txtFromPrice" value="${param.txtFromPrice}"/>
                                        <c:param name="txtToPrice" value="${param.txtToPrice}"/>
                                        <c:param name="action" value="Prev" />
                                    </c:url>
                                    <a href="${prevLink}" class="btn btn-primary">Prev</a>
                                </c:if>
                                <input type="number" class="form-control m-0 mx-2 text-center" min="1" value="${pageNo}" style="width: 50px;" readonly="readonly"/>
                                <c:if test="${empty requestScope.isLastList}">
                                    <c:url var="nextLink" value="SearchProductController">
                                        <c:param name="txtSearchProduct" value="${param.txtSearchProduct}"/>
                                        <c:param name="pageNo" value="${pageNo}"/>
                                        <c:param name="txtFromPrice" value="${param.txtFromPrice}"/>
                                        <c:param name="txtToPrice" value="${param.txtToPrice}"/>
                                        <c:param name="action" value="Next" />
                                    </c:url>
                                    <a href="${nextLink}" class="btn btn-primary">Next</a>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </c:if>
            <!-- No results -->
            <div class="d-flex justify-content-center">
                <c:if test="${requestScope.listProducts == null}">
                    <p class="m-0 my-2">No Results Was Found</p>
                </c:if>
            </div>
        </div>
        <!-- js -->
        <script type="text/javascript" src="js/my-js.js"></script>
        <script type="text/javascript">
                                                            window.onload = function () {
                                                                formatPrice();
                                                            };

                                                            $(document).ready(function () {
                                                                $('#btn-delete').click(function () {
                                                                    var checkedItems = document.getElementsByClassName("chk-item");
                                                                    var checked = false;
                                                                    for (var i = 0; i < checkedItems.length; i++) {
                                                                        if (checkedItems[i].checked) {
                                                                            checked = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (checked) {
                                                                        $('#confirm-delete').modal("show");
                                                                    } else {
                                                                        alert("You didn't select any product to delete!!");
                                                                    }
                                                                });

                                                                $('#btn-ok').click(function () {
                                                                    $('#submit-form').submit();
                                                                });

                                                            });
        </script>
    </body>
</html>
<footer class="footer bg-white text-center text-lg-start">
    <!-- Copyright -->
    <div class="text-center p-3">
        © 2020 Copyright:
        <c:url var="homeLink" value="${DispatchController}"/>
        <a class="text-info" href="${homeLink}">Hana Shop</a>
    </div>
    <!-- Copyright -->
</footer>
