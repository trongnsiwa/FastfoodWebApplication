<%-- 
    Document   : shoppingHistory
    Created on : Jan 13, 2021, 2:20:22 PM
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
        <title>Hana Shop - Shopping History</title>
    </head>
    <body>
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
        <!-- redirect if admin -->
        <c:url value="LoadHomeController" var="homeLink"/>
        <c:if test="${sessionScope.ROLE eq 'Admin'}">
            <c:redirect url="${homeLink}"/>
        </c:if>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <!-- navbar-brand -->
            <a class="navbar-brand" href="${homeLink}">Hana Shop</a>
            <!-- left navbar -->
            <div class="collapse navbar-collapse">
                <!-- home -->
                <div class="navbar-nav">
                    <a class="nav-link nav-item active bg-success text-white" href="${homeLink}">Home</a>
                </div>
            </div>
            <!-- right navbar -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
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
        <div class="container" style="padding-top: 80px;">
            <div class="row align-items-center">
                <div class="col-xl-12">
                    <div class="card position-relative d-flex flex-column bg-light border-info">
                        <div class="table-responsive">
                            <!-- show table -->
                            <table class="table border-left-0 border-top-0 border-right-0 table-cart">
                                <thead class="text-muted">
                                    <tr>
                                        <th colspan="2" scope="col">
                                            <div class="text-center">
                                                <!-- form -->
                                                <form action="DispatchController" class="d-flex flex-row">
                                                    <div class="w-100 mx-auto">
                                                        <div class="form-group mb-1"> <!-- search bar -->
                                                            <i class="fa fa-search position-absolute" style="padding: 10px; min-width: 40px; "></i>
                                                            <input type="text" name="txtSearchOrder" value="${param.txtSearchOrder}" class="form-control w-100 align-content-center" style="padding-left: 35px;"placeholder="Search">
                                                        </div>
                                                        <div class="d-flex mt-2">
                                                            <div class="form-group"> <!-- select day -->
                                                                <select name="cbDay" class="form-control">
                                                                    <option value="">Select Day</option>
                                                                    <c:forEach begin="1" end="31" step="1" var="day">
                                                                        <option value="${day}" <c:if test="${param.cbDay == day}">selected</c:if>>
                                                                            ${day}
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group ml-2"> <!-- select month -->
                                                                <select name="cbMonth" class="form-control">
                                                                    <option value="">Select Month</option>
                                                                    <option value="1" <c:if test="${param.cbMonth == 1}">selected</c:if>>January</option>
                                                                    <option value="2" <c:if test="${param.cbMonth == 2}">selected</c:if>>Febuary</option>
                                                                    <option value="3" <c:if test="${param.cbMonth == 3}">selected</c:if>>March</option>
                                                                    <option value="4" <c:if test="${param.cbMonth == 4}">selected</c:if>>April</option>
                                                                    <option value="5" <c:if test="${param.cbMonth == 5}">selected</c:if>>May</option>
                                                                    <option value="6" <c:if test="${param.cbMonth == 6}">selected</c:if>>June</option>
                                                                    <option value="7" <c:if test="${param.cbMonth == 7}">selected</c:if>>July</option>
                                                                    <option value="8" <c:if test="${param.cbMonth == 8}">selected</c:if>>August</option>
                                                                    <option value="9" <c:if test="${param.cbMonth == 9}">selected</c:if>>September</option>
                                                                    <option value="10" <c:if test="${param.cbMonth == 10}">selected</c:if>>October</option>
                                                                    <option value="11" <c:if test="${param.cbMonth == 11}">selected</c:if>>November</option>
                                                                    <option value="12" <c:if test="${param.cbMonth == 12}">selected</c:if>>December</option>
                                                                    </select>
                                                                </div>
                                                                <div class="form-group ml-2"> <!-- select year -->
                                                                    <select name="cbYear" class="form-control">
                                                                        <option value="">Select Year</option>
                                                                    <c:forEach begin="2020" end="2021" step="1" var="year">
                                                                        <option value="${year}" <c:if test="${param.cbYear == year}">selected</c:if>>
                                                                            ${year}
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <button type="submit" class="btn btn-outline-success mr-1" value="Search History" name="action">Search</button>
                                                        <input type="reset" class="btn btn-outline-secondary mt-1" value="Reset" />
                                                    </div>
                                                </form>
                                            </div>
                                        </th>
                                    </tr>
                                    <!-- table header -->
                                    <tr class="small text-uppercase bg-info text-white">
                                        <th scope="col">Food/Drink</th>
                                        <th scope="col" class="text-right">Price</th>
                                    </tr>
                                </thead>
                                <!-- show history order -->
                                <c:set var="orders" value="${requestScope.HISTORY_ORDERS}"/>
                                <c:if test="${orders != null}">
                                    <c:forEach var="order" items="${orders}">
                                        <c:set var="orderItems" value="${order.getOrderItems()}"/>
                                        <tbody class="border-top-1 border-info"> 
                                            <!-- header row -->
                                            <tr class="small">
                                                <td> <!-- created date -->
                                                    <p>${order.createdDate}</p>
                                                </td> <!-- order id -->
                                                <td class="text-right">
                                                    <p>ORDER ID: <span class="font-italic">${order.id}</span></p>
                                                </td>
                                            </tr>
                                            <!-- body row -->
                                            <!-- show orders -->
                                            <c:forEach var="item" items="${orderItems}">
                                                <tr>
                                                    <td>
                                                        <figure class="d-flex w-100 pl-1 pr-1 position-relative align-items-center">
                                                            <c:url var="img" value="${item.imageLink}"/> <!-- image -->
                                                            <div><img src="${img}" class="img-sm mr-2 img-fluid img-thumbnail" alt="${item.productName}"></div>
                                                            <figcaption class="pl-1 pr-1"><p class="text-dark b text-body" data-abc="true">${item.productName}</p> <!-- product name -->
                                                                <p class="text-muted small">x ${item.amount}</p>
                                                            </figcaption>
                                                        </figure>
                                                    </td>
                                                    <td class="text-right"> <!-- price -->
                                                        <input type="hidden" class="tmpPrice" value="${item.price}" />
                                                        <div class="price-wrap"><var class="b"><span class="price">${item.price}</span></var></div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <!-- footer row -->
                                            <tr>
                                                <td> <!-- left side -->
                                                    <div>
                                                        <p><span class="small text-muted">Customer name:</span> ${order.customerName}</p>
                                                        <p><span class="small text-muted">Address:</span> ${order.address}</p>
                                                        <p><span class="small text-muted">Phone:</span> ${order.phone}</p>
                                                    </div>
                                                </td>
                                                <td class="text-right mr-3"> <!-- right side -->
                                                    <div align="right" style="float: right;" class="mb-5">
                                                        <p><span class="small text-muted">Payment method:</span> ${order.paymentMethod}</p>
                                                        <input type="hidden" class="tmpPrice" value="${order.deliveryFee}" />
                                                        <p><span class="small text-muted">Delivery fee:</span> <span class="price">${order.deliveryFee}</span></p>
                                                        <input type="hidden" class="tmpPrice" value="${order.totalPrice}" />
                                                        <p><span class="small text-muted">Total price:</span> <span class="font-weight-bold text-info price" style="font-size: 25px;">${order.totalPrice}</span></p>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${orders == null}">
                                    <tbody>
                                        <tr>
                                            <td class="text-center" colspan="2">
                                                <font color="red">
                                                No result was found
                                                </font>
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- paging -->
                    <div class="d-flex w-100 justify-content-center mt-2 mb-4">
                        <c:if test="${not empty requestScope.pageNo}">
                            <c:set var="pageNo" value="${requestScope.pageNo}" />
                            <c:if test="${pageNo != 1}">
                                <c:url var="prevLink" value="SearchHistoryOrderController">
                                    <c:param name="txtSearchOrder" value="${param.txtSearchOrder}"/>
                                    <c:param name="pageNo" value="${pageNo}"/>
                                    <c:param name="cbDay" value="${param.cbDay}"/>
                                    <c:param name="cbMonth" value="${param.cbMonth}"/>
                                    <c:param name="cbYear" value="${param.cbYear}"/>
                                    <c:param name="action" value="Prev" />
                                </c:url>
                                <a href="${prevLink}" class="btn btn-primary">Prev</a>
                            </c:if>
                            <input type="number" class="form-control m-0 mx-2 text-center" min="1" value="${pageNo}" style="width: 50px;" readonly="readonly"/>
                            <c:if test="${empty requestScope.isLastList}">
                                <c:url var="nextLink" value="SearchHistoryOrderController">
                                    <c:param name="txtSearchOrder" value="${param.txtSearchOrder}"/>
                                    <c:param name="pageNo" value="${pageNo}"/>
                                    <c:param name="cbDay" value="${param.cbDay}"/>
                                    <c:param name="cbMonth" value="${param.cbMonth}"/>
                                    <c:param name="cbYear" value="${param.cbYear}"/>
                                    <c:param name="action" value="Next" />
                                </c:url>
                                <a href="${nextLink}" class="btn btn-primary">Next</a>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <!-- javascript -->
        <script type="text/javascript" src="js/my-js.js"></script>
        <script>
            window.onload = function () {
                formatPrice();
            };
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