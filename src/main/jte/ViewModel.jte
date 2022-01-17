@param String modelNum
@param boolean error

@if(!error)
    <img src="/images/${modelNum}.svg" class="w-100 h4" alt="model ${modelNum}"/>
@else
    Error encountered while rendering ${modelNum}
@endif