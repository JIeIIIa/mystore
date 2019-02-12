<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="checkboxVisibility"
       value="${requestScope.checkboxVisibility}"
       scope="page"/>

<div class="row">
    <div class="col-12">
        <table class="table table-striped">
            <thead class="thead-dark">
            <tr class="row">
                <th scope="col" class="col-3">Vendor code</th>
                <th scope="col" class="col">Product name</th>
                <th scope="col" class="col-3">Price</th>

                <c:if test="${checkboxVisibility}">
                    <th scope="col" class="col-1">Sel</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${requestScope.products}" var="p">
                <c:set var="valid" scope="page" value="${p.isValid()}"/>
                <c:choose>
                    <c:when test="${valid}">
                        <tr class="row" data-vendor-code="${p.vendorCode}">
                    </c:when>
                    <c:otherwise>
                        <tr class="row text-danger font-weight-bold">
                    </c:otherwise>
                </c:choose>

                    <td class="col-3 vendor-code">
                        <c:out value="${p.vendorCode}"/>
                    </td>
                    <td class="col">
                        <c:if test="${valid}">
                            <c:out value="${p.product.name}"/>
                        </c:if>
                    </td>
                    <td class="col-3">
                        <c:if test="${valid}">
                            <c:out value="${p.product.priceAsString}"/>
                        </c:if>
                    </td>
                    <c:if test="${checkboxVisibility}">
                        <td class="col-1">
                            <label hidden="hidden" for="${p.vendorCode}"></label>
                            <input type="checkbox" id="${p.vendorCode}">
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>