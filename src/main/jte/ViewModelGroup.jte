@param String representativeModelNum
@param java.util.List<String> allModelNums


<div class="w-30 ba bw1 pa1 ma3 bg-white">
    <h3>${representativeModelNum}</h3>
    <div hx-get="/view-model/${representativeModelNum}" hx-swap="outerHTML"
         hx-target="this" hx-trigger="load"></div>
    <div>Total: ${allModelNums.size()} models</div>
    <ul>
        @for(String modelNum : allModelNums)
            <li>${modelNum}</li>
        @endfor
    </ul>
</div>


<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<title>test</title>--%>
<%--<meta name="viewport" content="width=device-width, initial-scale=1"/>--%>
<%--<link rel="stylesheet" href="https://unpkg.com/tachyons/css/tachyons.min.css"/>--%>
<%--<script src="https://unpkg.com/htmx.org@1.6.1"></script>--%>
<%--<body class="bg-dark-gray">--%>


<%--</body>--%>
<%--</html>--%>
