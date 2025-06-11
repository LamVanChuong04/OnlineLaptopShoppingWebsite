<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <title>Register - FIEshop</title>
                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.3.0/css/all.css"
                    crossorigin="anonymous">
                <style>
                    body {
                        margin: 0;
                        font-family: Arial, sans-serif;
                        background: linear-gradient(90deg, #09bce4 0%, #2659f3 100%);
                        color: #fff;
                    }

                    .register-container {
                        max-width: 500px;
                        margin: 60px auto;
                        background: #fff;
                        color: #000;
                        padding: 30px;
                        border-radius: 12px;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                    }

                    h3 {
                        text-align: center;
                        margin-bottom: 20px;
                    }

                    label {
                        display: block;
                        margin-top: 12px;
                        font-weight: bold;
                    }

                    input[type="text"],
                    input[type="email"],
                    input[type="password"] {
                        width: 100%;
                        padding: 10px;
                        margin-top: 4px;
                        font-size: 14px;
                        border-radius: 5px;
                        border: 1px solid #ccc;
                    }

                    .invalid-feedback {
                        color: red;
                        font-size: 13px;
                    }

                    .form-row {
                        display: flex;
                        gap: 10px;
                    }

                    .form-col {
                        flex: 1;
                    }

                    .btn {
                        margin-top: 20px;
                        width: 100%;
                        background-color: #2659f3;
                        color: #fff;
                        padding: 12px;
                        border: none;
                        border-radius: 6px;
                        font-weight: bold;
                        cursor: pointer;
                    }

                    .btn:hover {
                        background-color: #1f49d8;
                    }

                    .footer-link {
                        text-align: center;
                        margin-top: 20px;
                    }

                    .footer-link a {
                        color: #2659f3;
                        text-decoration: none;
                        font-weight: bold;
                    }

                    .footer-link a:hover {
                        text-decoration: underline;
                    }
                    .container {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 10px 40px;
                        background-color: #fff;
                    }

                    .logo {
                        height: 50px;
                    }

                    .search-box {
                        display: flex;
                        align-items: center;
                        width: 700px;
                    }

                    .search-box input {
                        flex: 1;
                        padding: 8px;
                        border: 1px solid #ccc;
                        border-radius: 4px 0 0 4px;
                    }

                    .search-box button {
                        padding: 8px 12px;
                        background-color: #003d99;
                        color: white;
                        border: none;
                        border-radius: 0 4px 4px 0;
                        cursor: pointer;
                    }

                    .header-right {
                        display: flex;
                        gap: 20px;
                    }

                    .header-item {
                        display: flex;
                        align-items: center;
                        color: #003366;
                    }

                    .icon-circle {
                        background-color: #003d99;
                        color: #fff;
                        padding: 10px;
                        border-radius: 50%;
                        margin-right: 8px;
                    }

                    .cart-icon {
                        position: relative;
                    }

                    .cart-badge {
                        position: absolute;
                        top: -8px;
                        right: -10px;
                        background-color: red;
                        color: #fff;
                        font-size: 12px;
                        padding: 2px 6px;
                        border-radius: 50%;
                    }
                </style>
            </head>

            <body>
                <header class="header">
                    <div class="container">
                        <div class="header-left">
                            <img src="/images/logo.png" alt="Thiên Long" class="logo" />
                        </div>
                        <div class="header-center">
                            <div class="search-box">
                                <input type="text" placeholder="Tìm kiếm sản phẩm..." />
                                <button><i class="fa fa-search"></i></button>
                            </div>
                        </div>
                        <div class="header-right">
                            <div class="header-item">
                                <i class="fa fa-phone icon-circle"></i>
                                <div class="info">
                                    <strong>1900 866 819</strong><br />
                                    <span>Hỗ trợ khách hàng</span>
                                </div>
                            </div>
                            <div class="header-item">
                                <i class="fa fa-user icon-circle"></i>
                                <div class="info">
                                    <a href="/login"><strong>Đăng nhập</strong></a><br />
                                    <a href="/register"><span>Đăng ký</span></a>
                                </div>
                            </div>
                            <div class="header-item cart-icon">
                                <i class="fa fa-shopping-cart"></i>
                                <span class="cart-badge">0</span>
                            </div>
                        </div>
                    </div>
                </header>
                <div class="register-container">
                    <h3>CREATE ACCOUNT</h3>

                    <form:form method="post" action="/register" modelAttribute="register">
                        <c:set var="errorFirstName">
                            <form:errors path="firstName" cssClass="invalid-feedback" />
                        </c:set>
                        <c:set var="errorLastName">
                            <form:errors path="lastName" cssClass="invalid-feedback" />
                        </c:set>
                        <c:set var="errorEmail">
                            <form:errors path="email" cssClass="invalid-feedback" />
                        </c:set>
                        <c:set var="errorPassword">
                            <form:errors path="password" cssClass="invalid-feedback" />
                        </c:set>

                        <div class="form-row">
                            <div class="form-col">
                                <label>First Name</label>
                                <form:input class="${not empty errorFirstName ? 'is-invalid' : ''}" path="firstName"
                                    placeholder="Enter your first name" />
                                ${errorFirstName}
                            </div>
                            <div class="form-col">
                                <label>Last Name</label>
                                <form:input class="${not empty errorLastName ? 'is-invalid' : ''}" path="lastName"
                                    placeholder="Enter your last name" />
                                ${errorLastName}
                            </div>
                        </div>

                        <label>Email</label>
                        <form:input class="${not empty errorEmail ? 'is-invalid' : ''}" path="email"
                            placeholder="name@example.com" />
                        ${errorEmail}

                        <label>Password</label>
                        <form:input type="password" path="password" placeholder="Create a password" />

                        <label>Confirm Password</label>
                        <form:input class="${not empty errorPassword ? 'is-invalid' : ''}" type="password"
                            path="confirmPassword" placeholder="Confirm password" />
                        ${errorPassword}

                        <button class="btn" type="submit">Create Account</button>
                    </form:form>

                    <div class="footer-link">
                        <p>Already have an account? <a href="/login">Go to login</a></p>
                    </div>
                </div>
            </body>

            </html>