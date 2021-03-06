<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>my:Store) - Basket</title>
    <link rel="stylesheet" href="/mystore/webjars/bootstrap/4.2.1/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/mystore/css/main.css"/>
</head>
<body>
<nav class="navbar navbar-expand-md fixed-top navbar-dark bg-dark">
    <a class="navbar-brand" href="/mystore">my:Store)</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false"
            aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/mystore">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link disabled" href="/mystore/shop">Products</a>
            </li>
        </ul>
    </div>
</nav>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-10 col-md-8 col-lg-6 text-center">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-primary">Basket</h1>
                </div>
            </div>
            <jsp:include page="products.jsp"/>
            <div class="row justify-content-end">
                <div class="col-4">
                    <button type="button" class="btn btn-primary w-100" id="buy">Buy</button>
                </div>
            </div>
        </div>

    </div>
</div>
<script src="/mystore/webjars/jquery/3.3.1/jquery.min.js"></script>
<script src="/mystore/webjars/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="/mystore/webjars/bootstrap/4.2.1/js/bootstrap.min.js"></script>
<script src="/mystore/js/basket.js"></script>
</body>
</html>