<!DOCTYPE html>
<html lang="en">
<title>Model Minimizer</title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://unpkg.com/tachyons/css/tachyons.min.css"/>
<script src="https://unpkg.com/htmx.org@1.6.1"></script>
<script>
    function extractModelNumber(idx) {
        let elements = document.querySelectorAll('.modal-model');
        if (elements.length >= idx - 1)
            return elements[idx].dataset.modelNumber;
        return null;
    }
</script>
<body class="bg-dark-gray">
    <div class="ba b--dotted bg-orange bw1 sans-serif mw8-ns center pa2 mt1 flex">
        <div class="flex flex-column w-100">
            <div class="flex">
                <div hx-get="/create-model/4w2054" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
                <div class="ba bw1 bg-white sans-serif center"
                     hx-post="/view-minimized" hx-swap="innerHTML"
                     hx-vals="js:{modelNum: extractModelNumber(0)}"
                     hx-target="this" hx-trigger="load delay:1s, change from:document delay:1s, click from:document delay:1s" ></div>
            </div>
            <div class="pa2 tc">
                <div id="bisimulation" hx-target="this" hx-trigger="load delay:3s, change from:document delay:2s, click from:document delay:2s" hx-vals="js:{leftModelNum: extractModelNumber(0), rightModelNum: extractModelNumber(1)}"
                     hx-post="/view-bisimulation" hx-swap="innerHTML"></div>
            </div>
        </div>
    </div>
</body>
</html>
