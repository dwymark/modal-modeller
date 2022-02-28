@param String representativeModelNum
@param java.util.List<String> allModelNums


<div class="w-30 ba bw1 pa1 ma3 bg-white">
    <h3>${representativeModelNum}</h3>
    <div hx-get="/view-model/${representativeModelNum}" hx-swap="outerHTML"
         hx-target="this" hx-trigger="load"></div>
    <div>Total: ${allModelNums.size()} models</div>
    <ul class="h4" style="overflow: hidden;overflow-y:scroll">
        @for(String modelNum : allModelNums)
            <li>${modelNum}</li>
        @endfor
    </ul>
</div>
