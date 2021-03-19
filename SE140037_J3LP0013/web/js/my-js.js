/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function formatCurrency(val) {
    val = val.toFixed(2);
    return "$" + val;
}

function formatPrice() {
    var val;
    var listTmpPrice = document.getElementsByClassName("tmpPrice");
    var listPrice = document.getElementsByClassName("price");
    for (var i = 0; i < listPrice.length; i++) {
        var price = parseFloat(listTmpPrice[i].value);
        val = formatCurrency(price);
        listPrice[i].innerHTML = val;
    }
}

function calculateEachTotalPrice() {
    var val = 0;
    var listTotal = document.getElementsByClassName("total");
    var listPrice = document.getElementsByClassName("price");
    var listAmount = document.getElementsByClassName("amount");
    for (var i = 0; i < listTotal.length; i++) {
        var price = parseFloat(listPrice[i].value);
        var amount = parseInt(listAmount[i].value, 10);
        val = price * amount;
        val = formatCurrency(val);
        listTotal[i].innerHTML = val;
    }
}

function calculateTotalAmount() {
    var val = 0;
    var listAmount = document.getElementsByClassName("amount");
    for (var i = 0; i < listAmount.length; i++) {
        val += parseInt(listAmount[i].value, 10);
    }
    document.getElementById("total-amount").innerHTML = val;
}

function calculateTotalPrice() {
    var val = 0;
    var listTotal = document.getElementsByClassName("total");
    var listPrice = document.getElementsByClassName("price");
    var listAmount = document.getElementsByClassName("amount");
    var shippingFee = document.getElementById("tmp-shipping");

    for (var i = 0; i < listTotal.length; i++) {
        var price = parseFloat(listPrice[i].value);
        var amount = parseInt(listAmount[i].value, 10);
        val += price * amount;
    }
    document.getElementById("total-price").innerHTML = formatCurrency(val);

    var shipping = 0;

    if (shippingFee.value !== null) {
        shipping = parseFloat(shippingFee.value);
        if (shipping > 0) {
            val += shipping;
        }
    }
    document.getElementById("total-shipping-price").innerHTML = formatCurrency(val);
    document.getElementById("shipping").innerHTML = formatCurrency(shipping);
}

