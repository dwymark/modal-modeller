@param java.util.List<java.util.List<String>> modelNumsList

<!DOCTYPE html>
<html lang="en">
<title>test</title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://unpkg.com/tachyons/css/tachyons.min.css"/>
<script src="https://unpkg.com/htmx.org@1.6.1"></script>
<body class="bg-dark-gray">

<div class="ba b--dotted bg-green bw1 sans-serif mw8-ns center pa2 mt3 flex flex-wrap">
@for(var modelNums : modelNumsList)
    !{var concatenatedModelNums = String.join("_", modelNums);}

    <div hx-get="/view-model-group/${concatenatedModelNums}" hx-swap="outerHTML"
         hx-target="this" hx-trigger="load" ></div>
@endfor
</div>

</body>
</html>
