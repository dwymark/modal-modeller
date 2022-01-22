<!DOCTYPE html>
<html lang="en">
<title>Bisimulation Tester</title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://unpkg.com/tachyons/css/tachyons.min.css"/>
<script src="https://unpkg.com/htmx.org@1.6.1"></script>
<script>
    //todo: save left and right model numbers in the hypertext; extract them with javascript
    function extractModelNumber(idx) {
        let elements = document.querySelectorAll('.modal-model');
        if (elements.length === 2)
            return elements[idx].dataset.modelNumber;
        return null;
    }
</script>
<body class="bg-dark-gray">
    <div class="ba b--dotted bg-blue bw1 sans-serif mw8-ns center pa2 mt1 flex">
        <div class="flex flex-column w-100">
            <div class="flex">
                <div hx-get="/create-model/4w2054" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
                <div hx-get="/create-model/3w38" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
            </div>
            <div class="pa2 tc">
                <div id="bisimulation" hx-target="this" hx-trigger="load delay:1s, change from:document delay:1s, click from:document delay:1s" hx-vals="js:{leftModelNum: extractModelNumber(0), rightModelNum: extractModelNumber(1)}"
                     hx-post="/view-bisimulation" hx-swap="innerHTML"></div>
            </div>
        </div>
    </div>
</body>
</html>
