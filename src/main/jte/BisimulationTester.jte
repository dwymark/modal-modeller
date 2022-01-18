<!DOCTYPE html>
<html lang="en">
<title>Bisimulation Tester</title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://unpkg.com/tachyons/css/tachyons.min.css"/>
<script src="https://unpkg.com/htmx.org@1.6.1"></script>
<body>
    <div class="ba b--dotted bg-washed-blue bw1 sans-serif mw5 mw8-ns center pa2 mt1 flex">
        <div class="flex flex-column w-100">
            <div class="pa2 tc">
                <div hx-get="/view-bisimulation/4w2054/3w38" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
            </div>

            <div class="flex">
                <div hx-get="/create-model/4w2054" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
                <div hx-get="/create-model/3w38" hx-swap="outerHTML"
                     hx-target="this" hx-trigger="load" ></div>
            </div>

        </div>

    </div>
</body>
</html>
