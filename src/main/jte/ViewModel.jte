@param String modelNum
@param boolean error

@if(!error)
    <img src="/images/${modelNum}.svg" class="w-80" alt="model ${modelNum}"/>
@else
    Error encountered while rendering ${modelNum}
@endif