<%-- 
    Document   : checkOut
    Created on : Jan 10, 2021, 10:24:06 AM
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
        <title>Hana Shop - Check out</title>
    </head>
    <body>
        <style>
            .form-group i {
                padding: 10px;
                min-width: 40px;
                position: absolute;
            }

            .icon {
                padding-left: 35px;
            }

            .img-sm {
                width: 62px;
                height: 62px;
            }
        </style>
        <!-- redirect if admin -->
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
                    <!-- curtomer part -->
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <!-- view cart -->
                        <c:url var="cartLink" value="cart.jsp">
                            <c:param name="txtAddress" value="${param.txtAddress}"/>
                            <c:param name="txtCustomerName" value="${param.txtCustomerName}"/>
                            <c:param name="txtPhoneNumber" value="${param.txtPhoneNumber}"/>
                            <c:param name="txtNote" value="${param.txtNote}"/>
                        </c:url>
                        <li class="nav-item"><a class="nav-link" href="${cartLink}">View Cart <c:if test="${sessionScope.CART != null}">
                                    <span class="badge badge-info">${sessionScope.CART.getItems().size()}</span>
                                </c:if></a></li>
                            </c:if>
                    <!-- USER mean login user -->
                    <c:if test="${sessionScope.USER != null}">
                        <li class="nav-item dropdown">
                            <!-- show fullname -->
                            <a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown">${sessionScope.FULLNAME}</a>
                            <ul class="dropdown-menu">
                                <!-- customer part -->
                                <c:if test="${sessionScope.ROLE ne 'Admin'}">
                                    <!-- history -->
                                    <c:url var="historyLink" value="DispatchController">
                                        <c:param name="action" value="History"/>
                                    </c:url>
                                    <li><a class="dropdown-item" href="${historyLink}">History</a></li>
                                    </c:if>
                                <!-- all user part -->
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
                        <c:url var="loginLink" value="login.jsp">
                            <c:param name="goBackPage" value="checkOut"/>
                        </c:url>
                        <li class="nav-item"><a class="nav-link mr-4 " href="${loginLink}">Login</a></li>
                        </c:if>
                </ul> 
            </div>
        </nav>
        <!-- container -->
        <div class="container-fluid" style="padding-top: 80px;">
            <div class="row justify-content-center">
                <!-- left side -->
                <div class="col-xl-11">
                    <div class="row">
                        <div class="col-12">
                            <!-- breadcrumb -->
                            <nav aria-label="breadcrumb" class="p-0">
                                <ol class="breadcrumb small" style="background-color: white;">
                                    <li class="breadcrumb-item"><a class="text-muted" href="${homeLink}"><span class="mr-1">Back to shop</span></a></li>
                                        <c:url var="cartLink" value="cart.jsp">
                                            <c:param name="txtAddress" value="${param.txtAddress}"/>
                                            <c:param name="txtCustomerName" value="${param.txtCustomerName}"/>
                                            <c:param name="txtPhoneNumber" value="${param.txtPhoneNumber}"/>
                                            <c:param name="txtNote" value="${param.txtNote}"/>
                                        </c:url>
                                    <li class="breadcrumb-item"><a class="text-muted" href="${cartLink}"><span class="mr-1">Your Cart</span></a></li>
                                    <li class="breadcrumb-item active text-info">Checkout</li>
                                </ol>
                            </nav>
                        </div>
                    </div>
                    <!-- alert when user not login -->
                    <div id="login-alert" style="display: none;">
                        <div class="alert alert-danger" role="alert" id="login-alert-2">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            Please login to order!!
                        </div>
                    </div>
                    <!-- error -->
                    <c:set var="errors" value="${requestScope.outOfStock}"/>
                    <c:if test="${not empty requestScope.outOfStock}">
                        <font color="red">
                        ${requestScope.outOfStock}
                        </font><br>
                    </c:if>
                    <!-- form -->
                    <form method="POST" action="DispatchController" onsubmit="return checkLogin();">
                        <div class="row justify-content-around">
                            <div class="col-md-8">
                                <!-- confirm order information -->
                                <div class="card border-2 shadow-lg mx-2 my-2">
                                    <div class="card-header pb-0">
                                        <h4 class="card-title">1. Confirm order information</h4>
                                        <hr class="my-0">
                                    </div>
                                    <div class="card-body">
                                        <!-- address -->
                                        <div class="form-group w-100 mb-3">
                                            <i class="fa fa-address-book"></i>
                                            <input type="text" class="form-control w-100 align-content-center icon" name="txtAddress" value="${param.txtAddress}" placeholder="Enter your delivery address" required="required"/>
                                        </div>
                                        <!-- fullname -->
                                        <div class="form-group w-100 mb-3">
                                            <i class="fa fa-user-circle"></i>
                                            <input type="text" class="form-control w-100 align-content-center icon" name="txtCustomerName" value="<c:if test="${not empty sessionScope.FULLNAME}">${sessionScope.FULLNAME}</c:if>" placeholder="Enter your full name"/>
                                            </div>
                                            <!-- phone number -->
                                            <div class="form-group w-100 mb-3">
                                                <i class="fa fa-phone"></i>
                                                <input type="text" class="form-control w-100 align-content-center icon"  name="txtPhoneNumber" value="${param.txtPhoneNumber}" placeholder="Enter your phone number" pattern="\d{10}|(?:\d{3}-){2}\d{4}|\(\d{3}\)\d{3}-?\d{4}" required="required" oninvalid="setCustomValidity('Invalid phone. Phone number must match (xxx-xxx-xxxx)')" oninput="setCustomValidity('')"/>
                                        </div>
                                        <!-- note -->
                                        <div class="form-group w-100 mb-3">
                                            <textarea class="form-control" name="txtNote" placeholder="Note">${param.txtNote}</textarea>
                                        </div>
                                    </div>
                                </div>
                                <!-- payment method -->
                                <div class="card border-2 mt-3 shadow-lg mx-2 my-2">
                                    <div class="card-header pb-0">
                                        <h4 class="card-title">2. Payment Method</h4>
                                        <hr class="my-0">
                                    </div>
                                    <div class="card-body">
                                        <input type="radio" id="cod" name="rdMethod" value="COD" checked>
                                        <label for="cod">Cash On Delivery (COD)</label>
                                    </div>
                                </div>
                            </div>
                            <!-- right-side -->
                            <div class="col-md-4">
                                <div class="card border-2 shadow-lg mx-2 my-2">
                                    <div class="card-body">
                                        <c:set var="cart" value="${sessionScope.CART}"/>
                                        <c:if test="${cart != null}">
                                            <c:set var="items" value="${cart.getItems()}"/>
                                            <c:if test="${not empty items}">
                                                <!-- show product in cart -->
                                                <div class="row justify-content-between small mb-3 text-info">
                                                    <div class="col-auto col-md-7">
                                                        Food/Drink
                                                    </div>
                                                    <div class="pl-0 flex-sm-column col-auto my-auto">
                                                        Amount
                                                    </div>
                                                    <div class="pl-0 flex-sm-row col-auto my-auto">
                                                        Price
                                                    </div>
                                                </div>
                                                <c:forEach var="item" items="${items}">
                                                    <div class="row justify-content-between">
                                                        <div class="col-auto col-md-7"> 
                                                            <figure class="d-flex pl-1 pr-1 position-relative align-items-center">
                                                                <c:url var="img" value="${item.key.imageLink}"/> <!-- image -->
                                                                <div><img src="${img}" class="img-sm mr-2 img-fluid img-thumbnail" alt="${item.key.name}"></div> 
                                                                <figcaption><p class="text-dark b text-body" data-abc="true">${item.key.name}</p></figcaption> <!-- product name -->
                                                            </figure>
                                                        </div>
                                                        <!-- amount of product -->
                                                        <div class="pl-0 flex-sm-column col-auto my-auto">
                                                            <input type="hidden" class="amount" value="${item.value}" />
                                                            <span>${item.value}</span>
                                                        </div>
                                                        <!-- price -->
                                                        <div class="pl-0 flex-sm-column col-auto my-auto">
                                                            <input type="hidden" class="price" value="${item.key.price}" />
                                                            <div><var class="d-block mr-1 b"><span class="total">${item.key.price}</span></var></div>
                                                        </div>
                                                    </div>
                                                    <hr class="my-2">
                                                </c:forEach>
                                                <!-- below right-side -->
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="row justify-content-between">
                                                            <div class="col-auto"> <!-- total amount -->
                                                                <input type="hidden" name="txtAmount" id="tmp-total-amount" value=""/>
                                                                <p class="mb-1">Add (<span id="total-amount"></span> things)</p>
                                                            </div>
                                                            <div class="flex-sm-column col-auto"> <!-- total price -->
                                                                <div><var class="d-block mr-1 b"><span id="total-price"></span></var></div>
                                                            </div>
                                                        </div>
                                                        <!-- shipping fee -->
                                                        <div class="row justify-content-between">
                                                            <div class="col">
                                                                <p class="mb-1">Shipping</p>
                                                            </div>
                                                            <div class="flex-sm-column col-auto">
                                                                <input type="hidden" id="tmp-shipping" value="2"/>
                                                                <div><var class="d-block mr-1 b"><span id="shipping">2</span></var></div>
                                                            </div>
                                                        </div>
                                                        <!-- total price with shipping -->
                                                        <hr class="my-2">
                                                        <div class="row justify-content-between">
                                                            <div class="col-4">
                                                                <p class="mb-1">Total</p>
                                                            </div>
                                                            <div class="flex-sm-column col-auto">
                                                                <div><var class="d-block mr-1 b" style="font-size: 20px; font-weight: bold;"><span id="total-shipping-price"></span></var></div>
                                                            </div>
                                                        </div>
                                                        <hr class="my-2">
                                                        <div class="row mt-3">
                                                            <div class="col">
                                                                <input type="hidden" id="user" value="${sessionScope.USER}"/>
                                                                <input type="submit" id="btn-confirm" class="btn btn-lg btn-info btn-block" value="Order Now" name="action" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- confirm checkout cart modal -->                    
                                            <c:set var="order" value="${sessionScope.ORDER_INFO}"/>
                                            <input type="hidden" id="preview-order" value="${order}"/>
                                            <c:if test="${order != null}">
                                                <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="confirm-modal">
                                                    <div class="modal-dialog modal-dialog-centered border-0">
                                                        <div class="modal-content">
                                                            <!-- header -->
                                                            <div class="modal-header">
                                                                <h3 class="modal-title w-100 text-center text-info" id="model-label">Confirm order information</h3>
                                                            </div>
                                                            <!-- body -->
                                                            <div class="modal-body">
                                                                <div>
                                                                    <p style="font-size: 17px;" class="ml-2"><i class="fa fa-address-card mr-2"></i> <b>${order.address}</b></p>
                                                                </div>
                                                                <hr class="my-2">
                                                                <div>
                                                                    <p style="font-size: 17px;" class="ml-2"><i class="fa fa-user mr-2"></i> <b>${sessionScope.FULLNAME}</b></p>
                                                                    <p style="font-size: 17px;" class="ml-2"><i class="fa fa-phone mr-2"></i> <b>${order.phone}</b></p>
                                                                </div>
                                                                <hr class="my-2">
                                                                <div>
                                                                    <p style="font-size: 20px;" class="ml-2 pt-2 text-right"><b>Total price: $${order.totalPrice}</b></p>
                                                                </div>
                                                            </div>
                                                            <!-- footer -->
                                                            <div class="d-flex">
                                                                <div class="modal-footer w-100 align-items-center">
                                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                                    <input type="submit" name="action" class="btn btn-info" value="Confirm"/>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- js part -->
        <script type="text/javascript" src="js/my-js.js"></script>
        <script type="text/javascript">
                        window.onload = function () {
                            calculateEachTotalPrice();
                            calculateTotalAmount();
                            calculateTotalPrice();

                            var previewOrder = document.getElementById("preview-order").value;
                            if (previewOrder !== "") {
                                $('#confirm-modal').modal('show');
                            }
                        };

                        function checkLogin() {
                            var user = document.getElementById("user").value;
                            if (user === "") {
                                if ($("#login-alert").find("div#login-alert-2").length === 0) {
                                    $("#login-alert").append("<div class='alert alert-danger alert-dismissable' id='myAlert2'> <button type='button' class='close' data-dismiss='alert'  aria-hidden='true'>&times;</button> Please login to order!!</div>");
                                }
                                $("#login-alert").css("display", "");
                                setTimeout(function () {
                                    $(".alert").alert("close");
                                }, 2000);
                                return false;
                            }
                        }
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