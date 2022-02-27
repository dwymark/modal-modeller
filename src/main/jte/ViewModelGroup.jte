@param String representativeModelNum
@param java.util.List<String> allModelNums

<div class="w-30 ba bw1 bg-white">
    <h2>${representativeModelNum}</h2>
    <div hx-get="/view-model/${representativeModelNum}" hx-swap="outerHTML"
         hx-target="this" hx-trigger="load"></div>
    <div>Total: ${allModelNums.size()} models</div>
    <ul>
        @for(String modelNum : allModelNums)
            <li>${modelNum}</li>
        @endfor
    </ul>
</div>
