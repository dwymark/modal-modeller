@param String modelNum
@param int numWorlds

<div class="ba bg-white bw1 w-50 tc mr1 ml1 pa2" hx-target="this" hx-swap="outerHTML">
    <div hx-get="/view-model/${modelNum}" hx-swap="outerHTML"
         hx-target="this" hx-trigger="load"></div>
    <div class="pv2 f5 tl courier">
        Number of worlds
    </div>
    <div class="pt1 flex">
        <div class="w-third"><button class="pa2 w-third" hx-get="/create-model/${modelNum}/-1">-</button></div>
        <div class="w-third pa2 f4">${numWorlds}</div>
        <div class="w-third"><button class="pa2 w-third">+</button></div>
    </div>
    <div class="pv2 f5 tl courier">
        Toggle connection
    </div>
    <div class="pt1 flex">
        <div class="w-third"><input class="pa1 w2"/></div>
        <div class="w-third pa2 f4"> => </div>
        <div class="w-third"><input class="pa1 w2"/></div>
    </div>
    <div class="pv2 f5 tl courier">
        Model number
    </div>
    <div class="pt1">
        <input class="pa1 w-70" value="3w123"/>
    </div>
</div>