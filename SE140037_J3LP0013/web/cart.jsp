<%-- 
    Document   : cart
    Created on : Jan 8, 2021, 9:01:37 PM
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
        <title>Hana Shop - View Cart</title>
    </head>
    <body>
        <!-- style part -->
        <style>
            .img-sm {
                width: 80px;
                height: 80px;
            }

            .table-cart .price-wrap {
                line-height: 1.2;
            }

            .card {
                min-width: 0;
                word-wrap: break-word;
                background-clip: border-box;
            }
        </style>
        <!-- Remove old order info when not check out -->
        <c:if test="${sessionScope.ORDER_INFO != null}">
            <c:remove var="ORDER_INFO" scope="session"/>
        </c:if>
        <!-- Redirect if admin -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.ROLE eq 'Admin'}">
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
                    <!-- Customer part -->
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <!-- View cart -->
                        <li class="nav-item"><a class="nav-link" href="cart.jsp">View Cart <c:if test="${sessionScope.CART != null}">
                                    <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                                </c:if></a></li>
                            </c:if>
                    <!-- USER mean login user -->
                    <c:if test="${sessionScope.USER != null}">
                        <li class="nav-item dropdown">
                            <!-- show fullname -->
                            <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                            <ul class="dropdown-menu">
                                <!-- Curtomer part -->
                                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                    <!-- History -->
                                    <c:url var="historyLink" value="DispatchController">
                                        <c:param name="action" value="History"/>
                                    </c:url>
                                    <li><a class="dropdown-item" href="${historyLink}">History</a></li>
                                    </c:if>
                                <!-- all user part -->
                                <!-- Logout -->
                                <c:url var="logoutLink" value="DispatchController">
                                    <c:param name="action" value="Logout"/>
                                </c:url>
                                <li><a class="dropdown-item text-danger" href="${logoutLink}">Logout</a></li>
                            </ul>
                        </li>
                    </c:if>
                    <!-- Login -->
                    <c:if test="${sessionScope.USER == null}">
                        <c:url var="loginLink" value="login.jsp">
                            <c:param name="goBackPage" value="cart"/>
                        </c:url>
                        <li class="nav-item"><a class="nav-link mr-4 " href="${loginLink}">Login</a></li>
                        </c:if>
                </ul> 
            </div>
        </nav>
        <!-- container -->        
        <div class="container-fluid" style="padding-top: 80px;">
            <div class="row justify-content-center">
                <div class="col-xl-11">
                    <div class="row">
                        <!-- breadcrumb -->
                        <div class="col-auto">
                            <nav aria-label="breadcrumb" class="p-0">
                                <ol class="breadcrumb small" style="background-color: white;">
                                    <!-- Back to shop -->
                                    <li class="breadcrumb-item"><a class="text-muted" href="${homeLink}"><span class="mr-1">Back to shop</span></a></li>
                                    <!-- Your cart -->
                                    <li class="breadcrumb-item active text-info">Your Cart</li>
                                </ol>
                            </nav>
                        </div>
                    </div>
                    <div class="row">
                        <!-- left-side -->
                        <aside class="col-lg-9">
                            <div class="card position-relative d-flex flex-column bg-light border-success">
                                <div class="table-responsive">
                                    <!-- table for show cart -->
                                    <table class="table table-borderless table-cart">
                                        <thead class="text-muted">
                                            <tr class="small text-uppercase">
                                                <th scope="col">Food/Drink</th>
                                                <th scope="col" width="120">Amount</th>
                                                <th scope="col" width="120">Price</th>
                                                <th scope="col" class="text-right d-none d-md-block" width="200"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="cart" value="${sessionScope.CART}"/>
                                            <c:if test="${cart != null}">
                                                <c:set var="items" value="${cart.getItems()}"/>
                                                <c:if test="${not empty items}">
                                                    <c:forEach var="item" items="${items}">
                                                        <!-- Form -->
                                                    <form method="POST" action="UpdateCartController">
                                                        <tr>
                                                            <td> <!-- product image -->
                                                                <figure class="d-flex w-100 pl-1 pr-1 position-relative align-items-center">
                                                                    <c:url var="img" value="${item.key.imageLink}"/>
                                                                    <div><img src="${img}" class="img-sm mr-2 img-fluid img-thumbnail" alt="${item.key.name}"></div>
                                                                    <figcaption><p class="text-dark b text-body" data-abc="true">${item.key.name}</p></figcaption>
                                                                </figure>
                                                            </td>
                                                            <td> <!-- increase/decrease amount product -->
                                                                <div>
                                                                    <div class="d-flex justify-content-between">
                                                                        <!-- - -->
                                                                        <button type="submit" class="btn btn-outline-dark" name="action" value="decreaseAmount" >-</button>
                                                                        <input type="hidden" name="txtProductId" value="${item.key.id}" />
                                                                        <input type="number" class="amount" readonly min="0" value="${item.value}" style="width: 40px; text-align: center;"/>
                                                                        <!-- + -->
                                                                        <button type="submit" class="btn btn-outline-dark" name="action" value="increaseAmount">+</button>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td> <!-- price -->
                                                                <input type="hidden" class="price" value="${item.key.price}" />
                                                                <div class="price-wrap"><var class="d-block mr-1 b"><span class="total">${item.key.price}</span></var></div>
                                                            </td>
                                                            <!-- remove from cart -->
                                                            <c:url value="DispatchController" var="removeFromCartLink">
                                                                <c:param name="id" value="${item.key.id}"/>
                                                                <c:param name="action" value="Remove From Cart"/>
                                                            </c:url>
                                                            <td class="text-right d-none d-md-block"><a href="#" class="btn btn-info btn-round" data-abc="true" data-href="${removeFromCartLink}" data-toggle="modal" data-target="#confirm-delete">Remove</a></td>
                                                        </tr>
                                                    </form>
                                                </c:forEach>
                                            </c:if>
                                            <!-- Confirm delete product modal -->
                                            <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title w-100 text-center text-danger">Confirm delete product</h5>
                                                        </div>
                                                        <div class="modal-body text-center">
                                                            Are you sure want to remove this product from your cart?
                                                        </div>
                                                        <div class="modal-footer d-flex justify-content-center">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                            <a class="btn btn-danger btn-ok">Remove</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- empty cart -->
                                            <c:if test="${empty items}">
                                                <tr>
                                                    <td colspan="3">
                                                        <font color="red">
                                                        Empty cart
                                                        </font>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:if>
                                        <!-- not add any product -->
                                        <c:if test="${cart == null}">
                                            <tr>
                                                <td colspan="3">
                                                    <font color="red">
                                                    You didn't order anything
                                                    </font>
                                                </td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </aside>
                        <!-- right side -->                    
                        <aside class="col-lg-3">
                            <div class="card">
                                <div class="card-body flex-column p-1">
                                    <dl class="d-flex mb-1"> <!-- total amount-->
                                        <dt>Add (<span id="total-amount"></span> things)</dt>
                                        <dd class="text-right text-danger ml-3"></dd>
                                    </dl>
                                    <dl class="d-flex mb-1"> <!-- total price -->
                                        <input type="hidden" name="txtTotalPrice" id="tmp-total-price" value="" />
                                        <dt>Total price: <span id="total-price"></span></dt>
                                        <dd class="text-right text-dark b ml-3"><strong></strong></dd>
                                    </dl>
                                    <!-- checkout -->
                                    <c:url var="checkOutLink" value="checkOut.jsp">
                                        <c:param name="txtAddress" value="${param.txtAddress}"/>
                                        <c:param name="txtCustomerName" value="${param.txtCustomerName}"/>
                                        <c:param name="txtPhoneNumber" value="${param.txtPhoneNumber}"/>
                                        <c:param name="txtNote" value="${param.txtNote}"/>
                                    </c:url>
                                    <hr> <a href="${checkOutLink}" class="btn btn-lg btn-out btn-info btn-block p-2 w-100" data-abc="true" <c:if test="${empty items}">style="pointer-events: none; background-color: gray;"</c:if>>Checkout</a>
                                    </div>
                                </div>
                            </aside>                    
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript" src="js/my-js.js"></script>
            <script type="text/javascript">
                window.onload = function () {
                    calculateEachTotalPrice();
                    calculateTotalAmount();
                    calculateTotalPrice();
                };

                $('#confirm-delete').on('show.bs.modal', function (e) {
                    $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
                });
            </script>
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

