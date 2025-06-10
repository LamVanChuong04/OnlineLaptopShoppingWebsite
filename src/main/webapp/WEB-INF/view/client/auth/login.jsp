<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Login - FIEshop</title>
                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.3.0/css/all.css"
                    crossorigin="anonymous">
                <link rel="stylesheet" href="/css/demo.css">
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

                <div class="login-box">
                    <h3>ĐĂNG NHẬP</h3>
                    <form method="post" action="/login" class="login-form">
                        <c:if test="${param.error != null}">
                            <div class="alert" style="color: red;">Invalid email or password.</div>
                        </c:if>
                        <c:if test="${param.logout != null}">
                            <div class="alert" style="color: green;">Logout success.</div>
                        </c:if>

                        <label for="username">Email</label>
                        <input type="email" id="username" name="username" placeholder="name@example.com" required>

                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Password" required>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                        <button type="submit">Login</button>

                        <div class="small">
                            Don't have an account? <a href="/register">Register here</a>
                        </div>
                    </form>
                </div>

            </body>

            </html>